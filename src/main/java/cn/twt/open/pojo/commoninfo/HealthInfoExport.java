package cn.twt.open.pojo.commoninfo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
public class HealthInfoExport {

    @Excel(name = "学号")
    private String userNumber;

    @Excel(name = "姓名")
    private String realname;

    @Excel(name = "学院")
    private String department;

    @Excel(name = "专业")
    private String major;

    @Excel(name = "省")
    private String provinceName;

    @Excel(name = "市")
    private String cityName;

    @Excel(name = "区/县")
    private String regionName;

    @Excel(name = "体温")
    private String temperature;

    @Excel(name = "上传时间", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Timestamp uploadAt;
}
