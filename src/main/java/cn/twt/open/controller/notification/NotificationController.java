package cn.twt.open.controller.notification;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.constant.UserRole;
import cn.twt.open.dto.notification.*;
import cn.twt.open.pojo.notification.Notification;
import cn.twt.open.service.notification.NotificationService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Resource
    NotificationService notificationService;

    @PostMapping("/notification/all")
    @JwtToken(roles = {
            UserRole.NOTIFICATION_MAN
    })
    public APIResponse sendNotification2AllUsers(@RequestHeader("token") String token,
                                                 NotificationDto notificationDto){
        return notificationService.sendNotification2AllUsers(token,notificationDto);
    }

    @PostMapping("/notification/toUser")
    @JwtToken(roles = {
            UserRole.NOTIFICATION_MAN
    })
    public APIResponse sendNotification2Users(@RequestHeader("token") String token,
                                              UserNumberNotificationDto userNumberNotificationDto) {
        return notificationService.sendNotification2Users(token, userNumberNotificationDto);
    }

    @PostMapping("/notification/cid")
    @JwtToken
    public APIResponse storeCid(@RequestParam("cid") String cid,
                                @RequestHeader("token") String token){
        return notificationService.storeCid(cid,token);
    }

    @PostMapping("/notification/specific")
    @JwtToken(roles = {
            UserRole.NOTIFICATION_MAN
    })
    public APIResponse sendNotification2SpecificUsers(@RequestHeader("token") String token,
                                                      @RequestBody SpecificNotificationDto specificNotificationDto){
        return notificationService.sendNotification2SpecificUsers(token,specificNotificationDto);
    }

    @GetMapping("/notification/history/all")
    @JwtToken(roles = {
            UserRole.NOTIFICATION_MAN
    })
    public APIResponse getHistoryNotification(){
        return notificationService.getHistoryNotification();
    }

    @GetMapping("/notification/history/user")
    @JwtToken
    public APIResponse getMyNotification(@RequestHeader("token") String token){
        return notificationService.getMyNotification(token);
    }


    @GetMapping("/notification/history/page")
    @JwtToken(roles = {
            UserRole.NOTIFICATION_MAN
    })
    public APIResponse getHistoryNotificationByPage(@RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize){
        return notificationService.getHistoryNotificationByPage(pageNum,pageSize);
    }

    @PostMapping("/notification/he_suan_notice")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    public APIResponse heSuanNotice(@RequestHeader("token") String token, HeSuanNoticeDto heSuanNoticeDto){
        return notificationService.heSuanNotice(token, heSuanNoticeDto);
    }


}
