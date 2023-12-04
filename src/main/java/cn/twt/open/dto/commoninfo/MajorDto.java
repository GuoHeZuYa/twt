package cn.twt.open.dto.commoninfo;

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
public class MajorDto {
    int id;

    String name;

    String code;

    String departmentName;

    String departmentCode;
}
