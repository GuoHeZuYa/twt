package cn.twt.open.service.auth;

import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

import javax.servlet.http.HttpServletRequest;

public interface PasswordService extends BaseService {
    public APIResponse sendMsg(String phone, HttpServletRequest httpServletRequest);
    public APIResponse verify(String phone, String code, HttpServletRequest httpServletRequest);
    public APIResponse resetPassword(String phone, String password, HttpServletRequest httpServletRequest);
    public APIResponse resetPassword(String token, String password);
    public APIResponse resetPasswordByBasicInfo(String userNumber, String idNumber,String newPass);
}
