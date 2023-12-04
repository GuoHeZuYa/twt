package cn.twt.open.dao.notification;

import cn.twt.open.dto.notification.NotificationDto;
import cn.twt.open.pojo.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
@Mapper
public interface NotificationDao {
    Integer insertNotificationLog(@Param("notification") Notification notification,
                              @Param("status") Integer status);
    Integer insertNotification(@Param("notification") Notification notification,
                               @Param("userNumbers")List<String> userNumbers);
    Integer insertNotification2All(@Param("notification") Notification notification);
    ArrayList<NotificationDto> getNotificationByUserNumber(String userNumber);
}
