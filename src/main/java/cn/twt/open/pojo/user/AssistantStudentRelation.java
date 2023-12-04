package cn.twt.open.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssistantStudentRelation {
    private Integer id;
    private String assistantUserNumber;
    private String studentUserNumber;
}
