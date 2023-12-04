package cn.twt.open.dto.commoninfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SemesterDto {
    String semesterName;
    LocalDate semesterStartAt;
    Long semesterStartTimestamp;
}
