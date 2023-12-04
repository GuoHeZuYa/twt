package cn.twt.open.controller.auth;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.controller.BaseController;
import cn.twt.open.service.auth.PasswordService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 仅用于账号系统自用的密码相关接口
 */

@RestController
@RequestMapping("/api")
public class PasswordController extends BaseController {

    @Resource
    PasswordService passwordService;

    @PostMapping("/password/reset/msg")
    public APIResponse sendMsg(@RequestParam("phone") String phone,
                               HttpServletRequest httpServletRequest){
        return passwordService.sendMsg(phone, httpServletRequest);
    }

    @PostMapping("/password/reset/verify")
    public APIResponse verifyCode(@RequestParam("phone") String phone,
                                  @RequestParam("code") String code,
                                  HttpServletRequest httpServletRequest){
        return passwordService.verify(phone,code,httpServletRequest);
    }

    @PostMapping("/password/reset")
    public APIResponse resetPassword(String phone, String password, HttpServletRequest httpServletRequest) {
        return passwordService.resetPassword(phone, password, httpServletRequest);
    }

    /**
     * 这是在登录的情况下修改密码，前面可以看作是忘记密码后的修改密码
     */
    @JwtToken
    @PutMapping("/password/person/reset")
    public APIResponse resetPassword(@RequestHeader("token") String token,
                                     @RequestParam("password") String password) {
        return passwordService.resetPassword(token,password);
    }

    @PostMapping("/password/resetByBasicInfo")
    public APIResponse resetPasswordByBasicInfo(@RequestParam("userNumber") String userNumber,
                                                @RequestParam("idNumber") String idNumber,
                                                @RequestParam("newPassword")String newPassword) {
        return passwordService.resetPasswordByBasicInfo(userNumber, idNumber,newPassword);
    }

}
