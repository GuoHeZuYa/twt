package cn.twt.open.service.auth.impl;

import cn.twt.open.constant.AuthConstant;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.auth.AuthDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dto.auth.RegisterDto;
import cn.twt.open.dto.user.UserDto;
import cn.twt.open.pojo.notification.HeSuanNotification;
import cn.twt.open.pojo.user.UserHeSuanGroup;
import cn.twt.open.service.auth.AuthService;
import cn.twt.open.pojo.auth.Credential;
import cn.twt.open.pojo.commoninfo.StudentType;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerTypePredicate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author Lino
 */

@Service
public class AuthServiceImpl implements AuthService {
    @Resource
    AuthDao authDao;
    @Resource
    UserDao userDao;
    @Resource
    UserService userService;
    @Resource
    MessageUtils messageUtils;
    @Resource
    CacheUtils cacheUtils;

    @Resource
    RestTemplate restTemplate;

    private static final ReentrantLock lock = new ReentrantLock();

    private static final String REDIS_KEY_PREFIX = "userInfo-";

    /**
     * 通过普通方式（学号、昵称、邮箱）登录
     *
     * @param account  登录名
     * @param password 密码
     * @return APIResponse
     */
    @Override
    public APIResponse authenticate(String account, String password) {
        try {
            // 查出用户登录信息
            List<Credential> credential = authDao.getUserCredentialByAccount(account);
            if (CollectionUtils.isEmpty(credential)) {
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            } else {
                Credential userCredential = credential.get(0);
                // 验证密码
                if ("".equals(password) || !EncryptUtils.verifyBCryptHash(password, userCredential.getPassword())) {
                    return APIResponse.error(ErrorCode.PASSWORD_ERROR);
                } else {
                    // 查出用户信息并返回
                    UserDto userDto = (UserDto) userService.fetchSingleUser(userCredential.getUserNumber()).getResult();
                    if (userDto != null) {
                        String token = JwtUtils.genJwtToken(userCredential.getUserNumber(), userCredential.getRole());
                        userDto.setToken(token);
                        authDao.updateUserToken(userCredential.getUserNumber(), token);
                        // 查询是否需要更新
                        userDto.setUpgradeNeed(userDao.getUserType(userDto.getUserNumber()));
                        return APIResponse.success(userDto);
                    } else {
                        return APIResponse.error(ErrorCode.NO_SUCH_USER);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("数据库错误", e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public APIResponse tokenUpdate(String token) {
        try {
            String senderUserNumber = JwtUtils.getUserNumber(token);
            Integer senderUseRole = JwtUtils.getUserType(token);
            String newToken = JwtUtils.genJwtToken(senderUserNumber, senderUseRole);
            authDao.updateUserToken(senderUserNumber, newToken);
            return APIResponse.success(newToken);
        } catch (Exception e) {
            LOGGER.error("token生成失败", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse upgradeTest() {
        try {
            String newToken = JwtUtils.genJwtToken("BS_num", 1);
            authDao.updateCredential("MA_num", "BS_num", newToken);
            return APIResponse.success("已经将MA_num账号的学号降至BS_num");
        } catch (Exception e) {
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }

    }

    /**
     * 注册一个新用户(实现方法比较辣鸡，但至少线程安全)
     *
     * @param registerDto 用户信息dto
     * @return APIResponse
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse registerUser(RegisterDto registerDto, HttpServletRequest request) {
        lock.lock();
        try {
            // 先判断用户是否是天大人
            if (isUserExist(registerDto.getUserNumber(), registerDto.getIdNumber())) {
                // 验证手机验证码
                APIResponse result = MessageUtils.verifyCode(registerDto.getPhone(), registerDto.getVerifyCode(), request);
                if (result.getError_code() != ErrorCode.OK.getCode()) {
                    return result;
                }
                registerDto.setPassword(EncryptUtils.getBCryptString(registerDto.getPassword()));
                // 数据库加了unique约束，不用手动检查是否重复
                String legalUsername = Jsoup.clean(registerDto.getNickname(), Whitelist.relaxed());
                if (legalUsername.equals(registerDto.getNickname())) {
                    if (registerDto.getNickname().length() > 16) {
                        return APIResponse.error(ErrorCode.NICKNAME_TOO_LONG);
                    } else {
                        authDao.registerUser(registerDto);
                    }
                } else {
                    return APIResponse.error(ErrorCode.ILEGAL_NICKNAME);
                }
                return APIResponse.success(null);
            } else {
                return APIResponse.error(ErrorCode.ID_NUMBER_NOT_MATCH);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse sendMsg(String phone, HttpServletRequest request, boolean isRegistering, String token) {
        lock.lock();
        try {
            if (!StringUtils.isNumeric(phone) || phone.length() != AuthConstant.PHONE_LEN) {
                return APIResponse.error(ErrorCode.NOT_A_PHONE);
            }
            List<Credential> user = authDao.getUserCredentialByAccount(phone);
            if (isRegistering) {
                if (user.size() != 0) {
                    if (token != null) {
                        String userNumber = JwtUtils.getUserNumber(token);
                        if (!user.get(0).getUserNumber().equals(userNumber)) {
                            return APIResponse.error(ErrorCode.PHONE_DUPLICATED);
                        }
                    } else {
                        return APIResponse.error(ErrorCode.PHONE_DUPLICATED);
                    }
                }
            } else {
                if (user.size() == 0) {
                    return APIResponse.error(ErrorCode.NO_SUCH_USER);
                }
            }
            // 生成验证码
            String code = MessageUtils.generateVerificationCode(6);
            Result<SmsSingleSend> result = messageUtils.sendVerificationCode(phone, code);
            if (result.getCode() == 0) {
                request.getSession().setAttribute(AuthConstant.CODE_NAME, code);
                request.getSession().setAttribute(AuthConstant.PHONE, phone);
                return APIResponse.success(null);
            } else {
                LOGGER.error(result.toString());
                return APIResponse.error(ErrorCode.SEND_MSG_FAILED);
            }
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public APIResponse verify(String phone, String code,
                              HttpServletRequest request) {
        APIResponse result = MessageUtils.verifyCode(phone, code, request);
        if (result.getError_code() != 0) {
            return APIResponse.error(ErrorCode.VERIFICATION_FAILED);
        } else {
            try {
                List<Credential> credential = authDao.getUserCredentialByAccount(phone);
                Credential userCredential = credential.get(0);
                APIResponse res = userService.fetchSingleUser(userCredential.getUserNumber());
                if (res.getError_code() == NumberUtils.INTEGER_ZERO) {
                    UserDto userDto = (UserDto) res.getResult();
                    String token = JwtUtils.genJwtToken(userCredential.getUserNumber(), userCredential.getRole());
                    userDto.setToken(token);
                    authDao.updateUserToken(userCredential.getUserNumber(), token);
                    return APIResponse.success(userDto);
                } else {
                    return APIResponse.error(ErrorCode.NO_SUCH_USER);
                }
            } catch (Exception e) {
                LOGGER.error("数据库错误", e);
                return APIResponse.error(ErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public APIResponse getAvailableType(String token) {
        String userNumber = JwtUtils.getUserNumber(token);

        try {
            List<StudentType> userType = userDao.getUserType(userNumber);
            return APIResponse.success(userType);
        } catch (Exception e) {
            LOGGER.error("数据库错误", e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    /**
     * 账号升级
     *
     * @param token
     * @param id
     * @return 返回新的jwt token
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse upgradeAccount(String token, int id) {


        String userNumber = JwtUtils.getUserNumber(token);
        lock.lock();
        try {

            String userNumberKey = "userNumber";
            Map<String, Object> newUserInfo = userDao.getUserNewNumber(userNumber, id);
            // System.out.println("newUserInfo: "+newUserInfo);
            if (newUserInfo != null) {
                if (newUserInfo.containsKey(userNumberKey)) {

                    //新学号
                    String newUserNumber = (String) newUserInfo.get(userNumberKey);
                    if (!newUserNumber.equals(userNumber)) {
                        int userRole = (int) newUserInfo.get("role");
                        String newJwtToken = JwtUtils.genJwtToken(newUserNumber, userRole);
                        authDao.updateCredential(userNumber, newUserNumber, newJwtToken);
                        cacheUtils.cacheDel(REDIS_KEY_PREFIX + userNumber);

                        //调用青年湖底的接口，升级账号
                        upgradeInQnhd(userNumber, newUserNumber, token);

                        return APIResponse.success(newJwtToken);
                    }
                }
            }
            return APIResponse.error(ErrorCode.UPGRADE_FAILED);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse checkUserNumberAndUsername(String userNumber, String username) {
        try {
            int userNumberCount = authDao.getUserNumberCount(userNumber);
            int usernameCount = authDao.getUsernameCount(username);
            if (userNumberCount > 0) {
                return APIResponse.error(ErrorCode.USER_EXIST);
            }
            if (usernameCount > 0) {
                return APIResponse.error(ErrorCode.NICKNAME_DUPLICATED);
            }
            return APIResponse.success(null);
        } catch (Exception e) {
            LOGGER.error("error", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse checkInfo(String idNumber, String email, String phone) {
        try {
            int idNumberCount = userDao.getIdNumberCount(idNumber);
            int emailCount = authDao.getEmailCount(email);
            int phoneCount = authDao.getPhoneCount(phone);
            if (idNumberCount == 0) {
                return APIResponse.error(ErrorCode.USER_NOT_IN_SCHOOL);
            }
            if (emailCount > 0) {
                return APIResponse.error(ErrorCode.EMAIL_DUPLICATED);
            }
            if (phoneCount > 0) {
                return APIResponse.error(ErrorCode.PHONE_DUPLICATED);
            }
            return APIResponse.success(null);
        } catch (Exception e) {
            LOGGER.error("error", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public APIResponse logoff(String token) {
        lock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            authDao.strictDeleteUser(userNumber);
            cacheUtils.cacheDel(REDIS_KEY_PREFIX + userNumber);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse checkHeSuan(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<UserHeSuanGroup> groupResults = authDao.getHeSuanGroup(userNumber);
            if (CollectionUtils.isEmpty(groupResults)) {
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            } else {
                UserHeSuanGroup userType1 = groupResults.get(0);
                UserHeSuanGroup userType2 = groupResults.get(1);
                List<HeSuanNotification> log1Res = authDao.checkHeSuan(userType1.getType(), userType1.getCampus(), userType1.getMethodtype());
                List<HeSuanNotification> log2Res = authDao.checkHeSuan(userType2.getType(), userType2.getCampus(), userType2.getMethodtype());
                if (!log1Res.isEmpty()) {
                    return APIResponse.success(log1Res.get(0));
                } else if (!log2Res.isEmpty()) {
                    return APIResponse.success(log2Res.get(0));
                } else {
                    return APIResponse.success("无核酸");
                }
            }
        } catch (Exception e) {
            LOGGER.error("数据库错误", e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }

    }

    private boolean isUserExist(String userNumber, String idNumber) {
        int count = authDao.getUserCount(userNumber, idNumber);
        return count > 0;
    }

    /**
     * 调用qnhd的登录接口，返回token
     *
     * @param token
     * @return
     */
    private String loginQnhd(String token) {
        HttpHeaders headers = new HttpHeaders();
        String url = "http://202.113.13.171:7013/api/v1/f/auth/token?token={token}";
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        ResponseEntity<String> resEntity = restTemplate.getForEntity(url, String.class, param);

        LOGGER.error("result: " + resEntity);
        System.out.println("result: " + resEntity);

        String responseString = resEntity.getBody();
        JSONObject response = JSONObject.parseObject(responseString);
        JSONObject data = (JSONObject) response.get("data");
        return (String) data.get("token");
    }


    public void upgradeInQnhd(String oldNum, String newNum, String token) {

        String newToken = loginQnhd(token);

        // 请求地址
        String url = "http://202.113.13.171:7013/api/v1/f/user/update_num";

        // 请求头设置,x-www-form-urlencoded格式的数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("token", newToken);

        //提交参数设置
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("old", oldNum);
        map.add("new", newNum);

        // 组装请求体
        HttpEntity<MultiValueMap<String, String>> request =
                new HttpEntity<>(map, headers);

        // 发送post请求，并打印结果，以String类型接收响应结果JSON字符串
        String result = restTemplate.postForObject(url, request, String.class);

        System.out.println("debug: " + result);
    }


}
