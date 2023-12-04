package cn.twt.open.dto.commoninfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnSchoolInfoDto {
    public String provinceName;
    public String cityName;
    public String regionName;
    public String address;
    /**
     * 经度
     */
    public String longitude;
    /**
     * 纬度
     */
    public String latitude;
    public MultipartFile healthCodeScreenshot;
    public MultipartFile travelCodeScreenshot;
    public int curStatus;
    public String temperature;
}
