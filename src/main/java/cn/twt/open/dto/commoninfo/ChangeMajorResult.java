package cn.twt.open.dto.commoninfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMajorResult {
    int destDepartmentId;
    int destMajorId;
    String userNumber;
}
