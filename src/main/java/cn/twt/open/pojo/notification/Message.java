package cn.twt.open.pojo.notification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    Integer id;
    String operator;
    String targetUser;
    String title;
    String content;
    String url;
    Timestamp createdAt;
    @JsonIgnore
    String read;
}
