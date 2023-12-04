package cn.twt.open.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    String userNumber;
    String idNumber;
    String nickname;
    String email;
    String verifyCode;
    String phone;
    String password;
}
