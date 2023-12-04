package cn.twt.open.dto.commoninfo;

import cn.twt.open.pojo.commoninfo.Department;
import cn.twt.open.pojo.commoninfo.Major;
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
public class ChangeMajorDto {
    int id;
    String userNumber;
    String username;
    Department sourceDepartment;
    Major sourceMajor;
    Department destDepartment;
    Major destMajor;
    Timestamp createdAt;
    /**
     * 0 申请中 1 申请通过 2 申请未通过
     */
    int status;
}
