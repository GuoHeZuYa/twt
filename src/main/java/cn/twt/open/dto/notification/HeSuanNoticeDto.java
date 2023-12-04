package cn.twt.open.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeSuanNoticeDto {
        String percentage;
        String campus;
        String type;
        String title;
        String content;
        String url;
        Timestamp startTime;
        Timestamp endTime;
}



