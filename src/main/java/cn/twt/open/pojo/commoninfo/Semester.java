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
public class Semester {
    String semesterName;
    Timestamp semesterStartAt;
}
