package cn.twt.open.dao.notification;

import cn.twt.open.pojo.notification.HeSuanNotification;
import cn.twt.open.pojo.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
@Mapper
public interface HeSuanNoticeDao {
    ArrayList<String> getUserNumbers(String campus, String methodtype, String type);
    Integer insertHeSuanNotificationLog(@Param("heSuanNotification") HeSuanNotification heSuanNotification);
}
