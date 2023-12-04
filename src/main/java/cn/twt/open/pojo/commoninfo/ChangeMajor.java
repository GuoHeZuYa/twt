package cn.twt.open.pojo.commoninfo;

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
public class ChangeMajor {
    int id;
    String username;
    int sourceDepartment;
    int sourceMajor;
    int destDepartment;
    int destMajor;
    Timestamp createdAt;
    /**
     * 0 申请中 1 申请通过 2 申请未通过
     */
    int status;
}
