package cn.twt.open.controller.user;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.constant.UserRole;
import cn.twt.open.controller.BaseController;
import cn.twt.open.dto.user.UserInfoDto;
import cn.twt.open.pojo.user.User;
import cn.twt.open.service.auth.AuthService;
import cn.twt.open.service.user.UserService;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.JwtUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Lino
 */
@RestController
@RequestMapping("/api")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Resource
    AuthService authService;

    /**
     * 获取单个用户
     * @param token token
     * @return
     */
    @GetMapping("/user/single")
    @JwtToken
    public APIResponse fetchSingleUser(@RequestHeader("token") String token){
        String userNumber = JwtUtils.getUserNumber(token);
        return userService.fetchSingleUser(userNumber);
    }

    @GetMapping("/admin/user/{userNumber}")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN,
            UserRole.ASSISTANT
    })
    public APIResponse fetchUserByUserNumber(@PathVariable("userNumber") String userNumber){
        return userService.fetchSingleUser(userNumber);
    }

    @PostMapping("/admin/user")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN,
            UserRole.ASSISTANT
    })
    public APIResponse fetchUsersByUsersNumbers(String userNumbers){
        return userService.fetchUsers(userNumbers);
    }

    /**
     * 获取所有用户
     * @return
     */
    @GetMapping("/user/all")
    public APIResponse fetchAllUsers(){
        return userService.fetchAllUsers();
    }

    /**
     * 根据类型获取用户
     * @param type 用户类型
     * @return
     */
    @GetMapping("/user/{type}")
    public APIResponse fetchSpecificTypeUsers(@PathVariable("type") Integer type){
        return userService.fetchSpecificTypeUsers(type);
    }

    /**
     * 完善用户信息(仅完善信息使用)
     */
    @PutMapping("/user/single")
    @JwtToken
    public APIResponse updateUserInfo(@RequestHeader("token")String token,
                                      HttpServletRequest request,
                                      UserInfoDto dto){
        return userService.updateUserInfo(token, dto, request);
    }

    /**
     * 更新信息时发的短信
     * @param phone
     * @param request
     * @return
     */
    @PostMapping("/user/phone/msg")
    @JwtToken
    public APIResponse sendRegisterMsg(@RequestParam("phone") String phone,
                                       @RequestHeader("token") String token,
                                       HttpServletRequest request){
        return authService.sendMsg(phone,request,true, token);
    }

    @PutMapping("/user/single/phone")
    @JwtToken
    public APIResponse updateUserPhone(@RequestHeader("token") String token,
                                       @RequestParam("phone") String phone,
                                       @RequestParam("code") String verifyCode,
                                       HttpServletRequest request){
        return userService.updateUserPhone(token, phone, verifyCode, request);
    }

    @PutMapping("/user/single/email")
    @JwtToken
    public APIResponse updateUserEmail(@RequestHeader("token") String token,
                                       @RequestParam("email") String email){
        return userService.updateUserEmail(token, email);
    }

    @PostMapping("/user/avatar")
    @JwtToken
    public APIResponse uploadUserAvatar(@RequestHeader("token") String token,
                                        @RequestParam("avatar") MultipartFile avatar){
        return userService.uploadUserAvatar(token, avatar);
    }

    /**
     * 超管操作重置用户密码
     * 先不加@Manage注解
     */
    @PostMapping("/super/resetpwd")
//    @JwtToken(roles = {
//            UserRole.TWT_ADMIN,
//            UserRole.ASSISTANT
//    })
    public APIResponse resetUserPassword(@RequestParam("userNumber") String userNumber){
        return userService.resetUserPasswordToDefault(userNumber);
    }

    /**
     * 获取我的转专业申请信息
     * @param token token
     * @return APIResponse
     */
    @GetMapping("/user/major")
    @JwtToken
    public APIResponse getMyChangeMajor(@RequestHeader("token") String token){
        return userService.getMyChangeMajor(token);
    }

    @PostMapping("/user/major")
    @JwtToken
    public APIResponse changeMajor(@RequestHeader("token") String token,
                                   @RequestParam("destDepartmentId") int destDepartmentId,
                                   @RequestParam("destMajorId") int destMajorId){
        return userService.changeMajor(token, destDepartmentId, destMajorId);
    }

    @PutMapping("/user/single/username")
    @JwtToken
    public APIResponse updateUsername(@RequestHeader("token") String token,
                                      @RequestParam("username") String username){
        String legalUsername = Jsoup.clean(username, Whitelist.relaxed());
        return userService.updateUsername(token, legalUsername, username);
    }

    @PutMapping("/admin/user/single")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    public APIResponse updateUserInfo(User user){
        return userService.updateOrCreateUserInfo(user, 0);
    }

    @PostMapping("/admin/user/single")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    public APIResponse createUserInfo(User user){
        return userService.updateOrCreateUserInfo(user, 1);
    }

    @GetMapping("/user/check")
    public APIResponse checkIdNumberIsSame(@RequestParam("stu_num_a")String stuNumA,
            @RequestParam("stu_num_b")String stuNumB){
        return userService.checkIdNumIsSame(stuNumA,stuNumB);
    }



}
