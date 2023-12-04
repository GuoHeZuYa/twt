package cn.twt.open.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificNotificationDto {
    NotificationDto notificationDto;
    StuTypeFilterDto stuTypeFilterDto;
}
