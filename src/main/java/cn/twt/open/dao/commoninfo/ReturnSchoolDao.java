package cn.twt.open.dao.commoninfo;

import cn.twt.open.pojo.commoninfo.ReturnSchoolInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface ReturnSchoolDao {

    int insertOneRecord(@Param("rs")ReturnSchoolInfo returnSchoolInfo);

    List<ReturnSchoolInfo> fetchRecordByUserNumber(@Param("userNumber") String userNumber);

    ReturnSchoolInfo fetchLatestRecordByUserNumber(@Param("userNumber") String userNumber);

    List<ReturnSchoolInfo> getRecordByUserNumberList(@Param("userNumbers") List<String> userNumbers);

    List<ReturnSchoolInfo> getRecordByUserNumberListAndDate(@Param("userNumbers") List<String> userNumbers,
                                                            @Param("startTime") Timestamp startTime,
                                                            @Param("endTime") Timestamp endTime);

    List<String> getTodayStudents(@Param("startTime") Timestamp startTime,
                                  @Param("endTime") Timestamp endTime);
}
