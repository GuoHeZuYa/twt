package cn.twt.open.pojo.commoninfo;

import cn.twt.open.dto.commoninfo.ReturnSchoolInfoDto;
import cn.twt.open.dto.user.UserDto;
import cn.twt.open.exception.FileEmptyException;
import cn.twt.open.utils.ConvertUtils;
import cn.twt.open.utils.DateUtils;
import cn.twt.open.utils.FileUploadUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ReturnSchoolInfo {
    @JsonIgnore
    private Integer id;

    private String userNumber;

    private String grade;

    private String stuType;

    private String department;

    private String major;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String address;

    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;

    private String healthCodeUrl;

    private String travelCodeUrl;

    private int curStatus;

    private String temperature;

    private Timestamp uploadAt;

    public static ReturnSchoolInfo toReturnSchoolInfo(UserDto userDto, ReturnSchoolInfoDto returnSchoolInfoDto) throws FileEmptyException {
        try {
            ReturnSchoolInfo returnSchoolInfo = new ReturnSchoolInfo();
            String healthCodeUrl = FileUploadUtils.uploadFile(returnSchoolInfoDto.getHealthCodeScreenshot());
            String travelCodeUrl = FileUploadUtils.uploadFile(returnSchoolInfoDto.getTravelCodeScreenshot());
            if (StringUtils.isEmpty(healthCodeUrl) || StringUtils.isEmpty(travelCodeUrl)){
                return null;
            }
            returnSchoolInfo.setUserNumber(userDto.getUserNumber());
            returnSchoolInfo.setGrade(ConvertUtils.toGrade(userDto.getUserNumber()));
            returnSchoolInfo.setStuType(userDto.getStuType());
            returnSchoolInfo.setDepartment(userDto.getDepartment());
            returnSchoolInfo.setMajor(userDto.getMajor());
            returnSchoolInfo.setProvinceName(returnSchoolInfoDto.getProvinceName());
            returnSchoolInfo.setCityName(returnSchoolInfoDto.getCityName());
            returnSchoolInfo.setRegionName(returnSchoolInfoDto.getRegionName());
            returnSchoolInfo.setAddress(returnSchoolInfoDto.getAddress());
            returnSchoolInfo.setLongitude(returnSchoolInfoDto.getLongitude());
            returnSchoolInfo.setLatitude(returnSchoolInfoDto.getLatitude());
            returnSchoolInfo.setHealthCodeUrl(healthCodeUrl);
            returnSchoolInfo.setTravelCodeUrl(travelCodeUrl);
            returnSchoolInfo.setCurStatus(returnSchoolInfoDto.getCurStatus());
            returnSchoolInfo.setTemperature(returnSchoolInfoDto.getTemperature());
            returnSchoolInfo.setUploadAt(new Timestamp(System.currentTimeMillis()));
            return returnSchoolInfo;
        } catch (NullPointerException e){
            log.error("有字段为空", e);
            return null;
        }
    }

}
