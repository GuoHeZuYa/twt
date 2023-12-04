package cn.twt.open.service.auth.impl;

import cn.twt.open.constant.AuthConstant;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.auth.AuthDao;
import cn.twt.open.dao.auth.PasswordDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.service.auth.PasswordService;
import cn.twt.open.pojo.auth.Credential;
import cn.twt.open.pojo.user.User;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.EncryptUtils;
import cn.twt.open.utils.JwtUtils;
import cn.twt.open.utils.MessageUtils;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class PasswordServiceImpl implements PasswordService {
    @Resource
    AuthDao authDao;

    @Resource
    PasswordDao passwordDao;

    @Resource
    UserService userService;

    @Resource
    UserDao userDao;

    @Resource
    MessageUtils messageUtils;

    private static ReentrantLock lock = new ReentrantLock();

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse sendMsg(String phone, HttpServletRequest httpServletRequest) {
        lock.lock();
        try {
            if (!StringUtils.isNumeric(phone) || phone.length() != AuthConstant.PHONE_LEN){
                return APIResponse.error(ErrorCode.NOT_A_PHONE);
            }
            List<Credential> user = authDao.getUserCredentialByAccount(phone);
            if (user.size() == 0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            // 生成验证码
            String code = MessageUtils.generateVerificationCode(6);
            Result<SmsSingleSend> result = messageUtils.sendVerificationCode(phone, code);
            if (result.getCode() == 0) {
                httpServletRequest.getSession().setAttribute(AuthConstant.CODE_NAME, code);
                httpServletRequest.getSession().setAttribute(AuthConstant.PHONE, phone);
                return APIResponse.success(null);
            } else {
                LOGGER.error(result.toString());
                return APIResponse.error(ErrorCode.SEND_MSG_FAILED);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse verify(String phone, String code, HttpServletRequest httpServletRequest) {
        return MessageUtils.verifyCode(phone,code, httpServletRequest);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse resetPassword(String phone, String password, HttpServletRequest httpServletRequest) {
        lock.lock();
        try {
            if (!(boolean)httpServletRequest.getSession().getAttribute(AuthConstant.IS_VERIFIED)){
                return APIResponse.error(ErrorCode.VERIFICATION_FAILED);
            }
            List<Credential> user = authDao.getUserCredentialByAccount(phone);
            if (user.size() == 0){
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            } else {
                password = EncryptUtils.getBCryptString(password);
                int affectedRows = passwordDao.updatePassword(phone, password);
                if (affectedRows == 1){
                    return APIResponse.success(null);
                } else {
                    return APIResponse.error(ErrorCode.LOGIC_ERROR);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse resetPassword(String token, String password) {
        lock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            password = EncryptUtils.getBCryptString(password);
            passwordDao.updatePasswordWhenLogin(userNumber,password);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public APIResponse resetPasswordByBasicInfo(String userNumber, String idNumber,String newPassword) {
        try {

            User user = userDao.queryUserByUserNumberAndIdNumber(userNumber, idNumber);
            int userNumberCount = authDao.getUserNumberCount(userNumber);
            if (Objects.isNull(user) || NumberUtils.INTEGER_ZERO.equals(userNumberCount)) {
                return APIResponse.error(ErrorCode.NO_SUCH_USER);
            }
            return userService.resetUserPassword(userNumber,newPassword);
        } catch (Exception e) {
            LOGGER.error("resetPasswordByBasicInfo Exception", e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }
}
