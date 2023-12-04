package cn.twt.open.service.notification;

import cn.twt.open.dto.notification.*;
import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService extends BaseService {
    APIResponse sendNotification2AllUsers(String token, NotificationDto notificationDto);
    APIResponse sendNotification2SpecificUsers(String token, SpecificNotificationDto specificNotificationDto);
    APIResponse storeCid(String cid, String token);
    APIResponse getHistoryNotification();
    APIResponse getHistoryNotificationByPage(int pageNum,int pageSize);
    APIResponse getMyNotification(String token);
    APIResponse sendNotification2Users(String token, UserNumberNotificationDto userNumberNotificationDto);
    APIResponse heSuanNotice(String token,HeSuanNoticeDto heSuanNoticeDto);
}
