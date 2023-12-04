package cn.twt.open.service.auth;

import cn.twt.open.dto.auth.RegisterDto;
import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lino
 */
public interface AuthService extends BaseService {
    public APIResponse authenticate(String account, String password);
    public APIResponse registerUser(RegisterDto registerDto, HttpServletRequest request);
    public APIResponse sendMsg(String phone, HttpServletRequest request, boolean isRegistering, String token);
    public APIResponse verify(String phone, String code, HttpServletRequest request);
    public APIResponse getAvailableType(String token);
    public APIResponse upgradeAccount(String token, int id);
    public APIResponse checkUserNumberAndUsername(String userNumber, String username);
    public APIResponse checkInfo(String idNumber, String email, String phone);
    public APIResponse logoff(String token);
    public APIResponse checkHeSuan(String token);
    public APIResponse tokenUpdate(String token);

    APIResponse upgradeTest();
}
