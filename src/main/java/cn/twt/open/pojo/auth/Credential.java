package cn.twt.open.pojo.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Lino
 * @date 2020-10-30
 * @description 对应user表
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credential {

    int id;

    String userNumber;

    String nickname;

    String password;

    String telephone;

    String email;

    int role;

    String token;

    Timestamp createdAt;

    Timestamp updatedAt;
}
