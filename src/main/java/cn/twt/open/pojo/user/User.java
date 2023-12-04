package cn.twt.open.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Lino
 * @date 2020-10-30
 * @description 对应userinfo表
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    Integer id;

    String userNumber;

    String realname;

    String campus;

    Integer gender;

    Integer departmentId;

    Integer majorId;

    Integer leaveSchool;

    Integer stuType;

    Timestamp createdAt;

    Timestamp updatedAt;

    String avatar;

    String idNumber;
}
