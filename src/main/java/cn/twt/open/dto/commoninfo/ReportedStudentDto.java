package cn.twt.open.dto.commoninfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportedStudentDto {
    private String userNumber;
    private String name;
    private String temperature;
    private String address;
    private String reportStatus;
    private String healthCodeUrl;
    private String travelCodeUrl;
    private Timestamp uploadAt;
}
