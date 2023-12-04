package cn.twt.open.dto.commoninfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Lino
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorApplicationDto {
    List<ChangeMajorResult> list;
    int status;
}
