package cn.twt.open.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuTypeFilterDto {
    String campus;
    List<Integer> departmentId;
    List<Integer> majorId;
    List<Integer> stuType;
    List<String> grade;
    String tmpGrade;
}
