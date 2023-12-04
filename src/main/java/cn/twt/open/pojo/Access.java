package cn.twt.open.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Access {
    int id;
    String site;
    String appKey;
    String appSecret;
}
