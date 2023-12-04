package cn.twt.open.controller.auth;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.controller.BaseController;
import cn.twt.open.dto.auth.RegisterDto;
import cn.twt.open.service.auth.AuthService;
import cn.twt.open.utils.APIResponse;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Lino
 */
@RestController
@RequestMapping("/api")
public class AuthController extends BaseController {

    @Resource
    AuthService authService;

    @PostMapping("/auth/phone/msg")
    public APIResponse sendMsg(@RequestParam("phone") String phone,
                               HttpServletRequest request){
        return authService.sendMsg(phone,request,false,null);
    }

    @PostMapping("/auth/phone")
    public APIResponse phoneAuthenticate(@RequestParam("phone") String phone,
                                         @RequestParam("code") String code,
                                         HttpServletRequest request){
        return authService.verify(phone,code,request);
    }

    @PostMapping("/auth/common")
    public APIResponse commonAuthenticate(@RequestParam("account") String account,
                                          @RequestParam("password") String password){
        return authService.authenticate(account,password);
    }

    @PostMapping("/register/phone/msg")
    public APIResponse sendRegisterMsg(@RequestParam("phone") String phone,
                               HttpServletRequest request){
        return authService.sendMsg(phone,request,true,null);
    }

    @PostMapping("/register")
    public APIResponse register(RegisterDto registerDto, HttpServletRequest request){
        return authService.registerUser(registerDto, request);
    }

    @GetMapping("/upgrade")
    @JwtToken
    public APIResponse getAvailableType(@RequestHeader("token") String token){
        return authService.getAvailableType(token);
    }

    @PutMapping("/upgrade")
    @JwtToken
    public APIResponse upgradeAccount(@RequestHeader("token") String token,
                                      @RequestParam("typeId") int typeId){
        return authService.upgradeAccount(token, typeId);
    }

    @GetMapping("/register/checking/{userNumber}/{username}")
    public APIResponse checkUserNumberAndUsername(@PathVariable("userNumber") String userNumber,
                                                  @PathVariable("username") String username){
        return authService.checkUserNumberAndUsername(userNumber, username);
    }

    @PostMapping("/register/checking")
    public APIResponse checkInfo(@RequestParam("idNumber") String idNumber,
                                 @RequestParam("email") String email,
                                 @RequestParam("phone") String phone){
        return authService.checkInfo(idNumber, email, phone);
    }

    @JwtToken
    @PostMapping("/auth/logoff")
    public APIResponse logoff(@RequestHeader("token") String token) {
        return authService.logoff(token);
    }

    @GetMapping("/testApollo")
    public APIResponse testApollo() {
        Config config = ConfigService.getAppConfig();
        String key = "apollotest";
        String defaultValue = "fail";
        String value = config.getProperty(key,defaultValue);
        return APIResponse.success(value);
    }

    @GetMapping("/checkHeSuan")
    @JwtToken
    public APIResponse checkHeSuan(@RequestHeader("token") String token){
        return authService.checkHeSuan(token);
    }

    @PostMapping("/auth/updateToken")
    @JwtToken
    public APIResponse updateToken(@RequestHeader("token") String token){
        return authService.tokenUpdate(token);
    }


    @PostMapping("/auth/upgrade/test")
    public APIResponse upgradeTest(){
        return authService.upgradeTest();
    }

}
