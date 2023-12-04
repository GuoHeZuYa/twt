package cn.twt.open.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    Integer id;
    String title;
    String content;
    String url;
    Timestamp createdAt;
}
