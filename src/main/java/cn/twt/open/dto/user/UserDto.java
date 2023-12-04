package cn.twt.open.dto.user;

import cn.twt.open.pojo.commoninfo.StudentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    String userNumber;

    String nickname;

    String telephone;

    String email;

    String token;

    String role;

    String realname;

    String gender;

    String department;

    String major;

    String stuType;

    String avatar;

    String campus;

    String idNumber;

//    宿舍信息
    String area;

    String building;

    String floor;

    String room;

    String bed;

    // 是否需要升级
    List<StudentType> upgradeNeed;
}
