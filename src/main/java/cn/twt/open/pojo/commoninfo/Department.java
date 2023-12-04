package cn.twt.open.pojo.commoninfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Lino
 */
@Data
public class Department {
    int id;

    String name;

    String code;

    @JsonIgnore
    Timestamp createdAt;

    @JsonIgnore
    Timestamp updatedAt;
}
