package cn.twt.open.service.user.impl;

import cn.hutool.core.util.RandomUtil;
import cn.twt.open.cache.redis.RedisCacheManager;
import cn.twt.open.constant.AuthConstant;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.constant.Gender;
import cn.twt.open.constant.UserRole;
import cn.twt.open.dao.auth.PasswordDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dao.user.UserDormInfoDao;
import cn.twt.open.dto.commoninfo.ChangeMajorDto;
import cn.twt.open.dto.notification.StuTypeFilterDto;
import cn.twt.open.dto.user.UserDormInfoDto;
import cn.twt.open.dto.user.UserDto;
import cn.twt.open.dto.user.UserInfoDto;
import cn.twt.open.dto.user.UserWithDormDto;
import cn.twt.open.pojo.commoninfo.ChangeMajor;
import cn.twt.open.pojo.user.User;
import cn.twt.open.pojo.user.UserDo;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lino
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;

//    @Resource
//    UserDormInfoDao userDormInfoDao;

    @Resource
    PasswordDao passwordDao;

    @Resource
    CacheUtils cacheUtils;

    private static ReentrantLock lock = new ReentrantLock();
    private static ReentrantLock changeMajorLock = new ReentrantLock();
    private static ReentrantLock usernameLock = new ReentrantLock();

    private static final String REDIS_KEY_PREFIX = "userInfo-";


    @Override
    public APIResponse fetchSingleUser(String userNumber) {
        if (StringUtils.isEmpty(userNumber)) {
            return APIResponse.error(ErrorCode.NO_SUCH_USER);
        }
        UserDto cachedUserDto= cacheUtils.cacheGet(REDIS_KEY_PREFIX+userNumber, UserDto.class);
        if (Objects.nonNull(cachedUserDto)){
            return APIResponse.success(cachedUserDto);
        }
        try {
            List<UserDo> userDos = userDao.queryUserInfo(userNumber,null);
            if (!userDos.isEmpty()){
                UserDo userInfo = userDos.get(0);
                userInfo.formatNullToEmptyString();
                UserDto userDto = new UserDto();
                BeanUtils.copyProperties(userInfo, userDto);
                String role = UserRole.COMMON;
                if(userInfo.getRole() == UserRole.TWT_ADMIN){
                    role = UserRole.ADMIN;
                } else if(userInfo.getRole() == UserRole.ASSISTANT){
                    role = UserRole.SUB_ADMIN;
                }
                userDto.setRole(role);
                userDto.setGender(userInfo.getGender() == 1 ? Gender.MALE : Gender.FEMALE);
                // 设置缓存过期时间 10 天
                cacheUtils.cacheSet(REDIS_KEY_PREFIX+userNumber, userDto, 60 * 60 * 24 * 10);
                return APIResponse.success(userDto);
            } else {
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public APIResponse fetchUsers(String userNumbers) {
        if (StringUtils.isEmpty(userNumbers)) {
            return APIResponse.error(ErrorCode.NO_SUCH_USER);
        }
        List<String> userNumberList = Lists.newArrayList(userNumbers.replace(" ","").split(";|；",-1));
        List<UserDto> users = new LinkedList<>();
        try {
            for(String userNumber: userNumberList){
                List<UserDo> userSearchRes = userDao.queryUserInfo(userNumber, null);
                if(!userSearchRes.isEmpty()){
                    UserDo userInfo = userSearchRes.get(0);
                    userInfo.formatNullToEmptyString();
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(userInfo, userDto);
                    String role = UserRole.COMMON;
                    if(userInfo.getRole() == UserRole.TWT_ADMIN){
                        role = UserRole.ADMIN;
                    } else if(userInfo.getRole() == UserRole.ASSISTANT){
                        role = UserRole.SUB_ADMIN;
                    }
                    userDto.setRole(role);
                    userDto.setGender(userInfo.getGender() == 1 ? Gender.MALE : Gender.FEMALE);
                    users.add(userDto);
                }
//                else{
//                    System.out.println(userNumber);
//                }
            }
            return APIResponse.success(users);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

//    @Override
//    public APIResponse fetchSingleUserWithDormInfo(String userNumber) {
//        if (StringUtils.isEmpty(userNumber)) {
//            return APIResponse.error(ErrorCode.NO_SUCH_USER);
//        }
//        UserDto cachedUserDto= cacheUtils.cacheGet(REDIS_KEY_PREFIX+userNumber, UserDto.class);
//        if (Objects.nonNull(cachedUserDto)){
//            return APIResponse.success(cachedUserDto);
//        }
//        try {
//            List<UserDo> userDos = userDao.queryUserInfo(userNumber,null);
//            if (!userDos.isEmpty()){
//                UserDo userInfo = userDos.get(0);
//                userInfo.formatNullToEmptyString();
//                UserDto userDto = new UserDto();
//                BeanUtils.copyProperties(userInfo, userDto);
//                String role = UserRole.COMMON;
//                if(userInfo.getRole() == UserRole.TWT_ADMIN){
//                    role = UserRole.ADMIN;
//                } else if(userInfo.getRole() == UserRole.ASSISTANT){
//                    role = UserRole.SUB_ADMIN;
//                }
//                userDto.setRole(role);
//                userDto.setGender(userInfo.getGender() == 1 ? Gender.MALE : Gender.FEMALE);
//
//                List<UserDormInfoDto> userDormInfoDtos = userDormInfoDao.getDormInfoByUserNumber(userNumber);
//                UserDormInfoDto userDormInfoDto=new UserDormInfoDto();
//                if(!userDormInfoDtos.isEmpty()){
//                    userDormInfoDto = userDormInfoDtos.get(0);
//                }else{
//                    userDormInfoDto.setNotFound("没有查到宿舍信息");
//                }
//                userDto.setUserDormInfoDto(userDormInfoDto);
//                // 设置缓存过期时间 10 天
//                cacheUtils.cacheSet(REDIS_KEY_PREFIX+userNumber, userDto, 60 * 60 * 24 * 10);
//                return APIResponse.success(userDto);
//            } else {
//                return APIResponse.error(ErrorCode.NO_SUCH_USER);
//            }
//        } catch (Exception e){
//            LOGGER.error("数据库错误",e);
//            return APIResponse.error(ErrorCode.DATABASE_ERROR);
//        }
//    }

    @Override
    public APIResponse fetchAllUsers() {
        try {
            List<UserDo> userDos = userDao.queryUserInfo();
            List<UserDto> users = new LinkedList<>();
            for (UserDo u: userDos) {
                if (Objects.isNull(u.getDepartment())) {
                    u.setDepartment(StringUtils.EMPTY);
                }
                if (Objects.isNull(u.getMajor())) {
                    u.setMajor(StringUtils.EMPTY);
                }
                UserDto dto = new UserDto();
                BeanUtils.copyProperties(u, dto);
                String role = UserRole.COMMON;
                if(u.getRole() == UserRole.TWT_ADMIN){
                    role = UserRole.ADMIN;
                } else if(u.getRole() == UserRole.ASSISTANT){
                    role = UserRole.SUB_ADMIN;
                }
                dto.setRole(role);
                dto.setGender(u.getGender() == 1 ? Gender.MALE : Gender.FEMALE);
                users.add(dto);
            }
            return APIResponse.success(users);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public APIResponse fetchSpecificTypeUsers(Integer type) {
        try {
            List<UserDo> userDos = userDao.queryUserInfo(null, type);
            List<UserDto> users = new LinkedList<>();
            for (UserDo u: userDos) {
                u.formatNullToEmptyString();
                UserDto dto = new UserDto();
                BeanUtils.copyProperties(u, dto);
                dto.setRole(u.getRole() == UserRole.NORMAL_STUDENT ? UserRole.COMMON : UserRole.ADMIN);
                dto.setGender(u.getGender() == 1 ? Gender.MALE : Gender.FEMALE);
                users.add(dto);
            }
            return APIResponse.success(users);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = {DuplicateKeyException.class,RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateUserInfo(String token, UserInfoDto dto, HttpServletRequest request) {
        String userNumber = JwtUtils.getUserNumber(token);
        lock.lock();
        try {
            APIResponse result = MessageUtils.verifyCode(dto.getTelephone(),dto.getVerifyCode(),request);
            if (result.getError_code() != ErrorCode.OK.getCode()){
                return result;
            }
            String email = dto.getEmail();
            String telephone = dto.getTelephone();
            if (ValidateUtils.isEmail(email) && ValidateUtils.isPhone(telephone)){
                dto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                List<UserDo> userDos = userDao.queryUserInfo(userNumber, null);
                if (CollectionUtils.isEmpty(userDos)) {
                    return APIResponse.error(ErrorCode.NO_SUCH_USER);
                }
                UserDo user = userDos.get(0);
                String currentTelephone = user.getTelephone();
                String currentEmail = user.getEmail();
                if (telephone.equals(currentTelephone) && email.equals(currentEmail)) {
                    return APIResponse.success(null);
                }
                if (!telephone.equals(currentTelephone)) {
                    userDao.updateUserPhone(userNumber, telephone);
                }
                if (!email.equals(currentEmail)) {
                    userDao.updateUserEmail(userNumber,email);
                }
            } else {
                return APIResponse.error(ErrorCode.NOT_A_EMAIL_OR_PHONE);
            }
            return APIResponse.success(null);
        } finally {
            lock.unlock();
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
        }

    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse resetUserPassword(String userNumber,String newPass) {
        lock.lock();
        try {
            return resetPassword(userNumber,newPass);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse resetUserPasswordToDefault(String userNumber) {
        lock.lock();
        try {
            String newPass = "Tju123456";
            return resetPassword(userNumber,newPass);
        } finally {
            lock.unlock();
        }
    }

    private APIResponse resetPassword(String userNumber,String newPass){
        String passAfterEncrypt = EncryptUtils.getBCryptString(newPass);
        int affectedRows = passwordDao.updatePasswordWhenLogin(userNumber, passAfterEncrypt);
        if (affectedRows == 0){
            return APIResponse.error(ErrorCode.NO_SUCH_USER);
        } else {
            return APIResponse.success("");
        }
    }



    @Override
    public APIResponse getMyChangeMajor(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<ChangeMajorDto> myChangeMajor = userDao.getMyChangeMajor(userNumber);
            return APIResponse.success(myChangeMajor);
        } catch (Exception e){
            LOGGER.error("错误",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse changeMajor(String token, int destDepartmentId, int destMajorId) {
        changeMajorLock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<ChangeMajor> unPassedApply = userDao.getUnPassedApply(userNumber);
            if (unPassedApply.size() != 0){
                return APIResponse.error(ErrorCode.HAVE_UNPASSED_APPLY);
            }
            User userInfo = userDao.queryBasicInfo(userNumber);
            String username = userInfo.getRealname();
            int sourceDepartmentId = userInfo.getDepartmentId();
            int sourceMajorId = userInfo.getMajorId();
            int affectedRows = userDao.changeMajor(userNumber, username, sourceDepartmentId, sourceMajorId,destDepartmentId, destMajorId);
            return APIResponse.success(null);
        } finally {
            changeMajorLock.unlock();
        }
    }

    @Transactional(rollbackFor = {DuplicateKeyException.class,RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateUserPhone(String token, String phone, String verifyCode, HttpServletRequest request) {
        lock.lock();
        try {
            APIResponse result = MessageUtils.verifyCode(phone,verifyCode,request);
            if (result.getError_code() != ErrorCode.OK.getCode()){
                return result;
            }
            String userNumber = JwtUtils.getUserNumber(token);
            if (ValidateUtils.isPhone(phone)){
                userDao.updateUserPhone(userNumber, phone);
            } else {
                return APIResponse.error(ErrorCode.NOT_A_PHONE);
            }
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }

    @Transactional(rollbackFor = {DuplicateKeyException.class,RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateUserEmail(String token, String email) {
        lock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            if (ValidateUtils.isEmail(email)){
                userDao.updateUserEmail(userNumber, email);
            } else {
                return APIResponse.error(ErrorCode.NOT_A_EMAIL);
            }
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }

    @Transactional(rollbackFor = {DuplicateKeyException.class, RuntimeException.class}, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateUsername(String token, String legalUsername, String username) {
        usernameLock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            if (legalUsername.equals(username)){
                if (username.length() > 16){
                    return APIResponse.error(ErrorCode.NICKNAME_TOO_LONG);
                }
                userDao.updateUsername(userNumber, username);
            } else {
                return APIResponse.error(ErrorCode.ILEGAL_NICKNAME);
            }
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
            return APIResponse.success(null);
        } finally {
            usernameLock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateOrCreateUserInfo(User user, int status) {
        lock.lock();
        try {
            int row = userDao.countUserByUserNumber(user.getUserNumber());
            if (row > 0){
                if(status == 0){
                    userDao.updateUser(user);
                    cacheUtils.cacheDel(REDIS_KEY_PREFIX+user.getUserNumber());
                } else {
                    return APIResponse.error(ErrorCode.USERINFO_EXIST);
                }
            } else {
                if(status == 0){
                    return APIResponse.error(ErrorCode.NO_SUCH_USER);
                } else {
                    userDao.createUser(user);
                }
            }
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<String> getSpecificUserNumber(StuTypeFilterDto stuTypeFilterDto){
        try {
            return userDao.getSpecificUserNumber(stuTypeFilterDto);
        } catch (Exception e){
            LOGGER.error("get user number failed: ", e);
            return Lists.newArrayList();
        }
    }

    @Override
    public APIResponse uploadUserAvatar(String token, MultipartFile file) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            String avatarUrl = FileUploadUtils.uploadFile(file);
            if (Objects.isNull(avatarUrl)) {
                return APIResponse.error(ErrorCode.IMAGE_ILLEGAL);
            }
            userDao.updateUserAvatar(userNumber, avatarUrl);
            cacheUtils.cacheDel(REDIS_KEY_PREFIX+userNumber);
            return APIResponse.success(avatarUrl);
        } catch (Exception e) {
            LOGGER.error("uploadUserAvatar Error", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Override
    public APIResponse checkIdNumIsSame(String stuNumA, String stuNumB) {
        try {
            String IdNumA = userDao.queryIdNumberByStuNum(stuNumA);

            String IdNumB = userDao.queryIdNumberByStuNum(stuNumB);

            /**
             * 学号错误，查找不到相关信息
             */
            if(IdNumA==null||IdNumB==null){
                return APIResponse.error(ErrorCode.ERROR_NUMBER);
            }

            return APIResponse.success(IdNumA.equals(IdNumB));
        }catch (Exception e){
            LOGGER.error("checkIdNumIsSame error",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }
}
