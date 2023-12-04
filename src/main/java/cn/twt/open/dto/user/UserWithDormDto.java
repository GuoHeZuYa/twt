package cn.twt.open.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserWithDormDto {

    UserDto userDto;
    UserDormInfoDto userDormInfoDto;

}
