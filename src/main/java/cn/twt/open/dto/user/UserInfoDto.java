package cn.twt.open.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    String telephone;
    String verifyCode;
    String email;
    Timestamp updatedAt;
}
