package cn.twt.open.pojo.commoninfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Major {
    int id;

    String name;

    String code;

    @JsonIgnore
    int departmentId;

    @JsonIgnore
    Timestamp createdAt;

    @JsonIgnore
    Timestamp updatedAt;
}
