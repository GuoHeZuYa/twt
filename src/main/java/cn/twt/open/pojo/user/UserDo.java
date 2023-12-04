package cn.twt.open.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDo {
    String userNumber;

    String nickname;

    String telephone;

    String email;

    int role;

    String realname;

    int gender;

    String department;

    String major;

    String stuType;

    String avatar;

    String campus;

    String token;

    String idNumber;

    //    宿舍信息
    String area;

    String building;

    String floor;

    String room;

    String bed;

    public void formatNullToEmptyString() {
        this.department = Objects.isNull(this.department) ? StringUtils.EMPTY : this.department;
        this.major = Objects.isNull(this.major) ? StringUtils.EMPTY : this.major;
        this.stuType = Objects.isNull(this.stuType) ? "学生" : this.stuType;
        this.area = Objects.isNull(this.area) ? "未查询到宿舍信息" : this.area;
        this.building = Objects.isNull(this.building) ? "未查询到宿舍信息" : this.building;
        this.floor = Objects.isNull(this.floor) ? "未查询到宿舍信息" : this.floor;
        this.room = Objects.isNull(this.room) ? "未查询到宿舍信息" : this.room;
        this.bed = Objects.isNull(this.bed) ? "未查询到宿舍信息" : this.bed;
    }
}
