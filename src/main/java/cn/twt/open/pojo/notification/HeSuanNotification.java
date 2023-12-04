package cn.twt.open.pojo.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeSuanNotification {
    Integer id;
    String operator;
    String percentage;
    String campus;
    String type;
    String title;
    String content;
    String url;
    Timestamp createdAt;
    Timestamp startTime;
    Timestamp endTime;
}
