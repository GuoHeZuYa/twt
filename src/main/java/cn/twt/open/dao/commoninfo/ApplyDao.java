package cn.twt.open.dao.commoninfo;

import cn.twt.open.dto.commoninfo.ChangeMajorDto;
import cn.twt.open.dto.commoninfo.ChangeMajorResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lino
 */
@Repository
@Mapper
public interface ApplyDao {
    List<ChangeMajorDto> getAllApplication(int destDepartmentId);
    int updateUserInfo(@Param("list") List<ChangeMajorResult> resultList);
    int updateApplyStatus(@Param("status") int status,
                          @Param("list") List<ChangeMajorResult> resultList);
    int addAnOperateLog(String id, String userNumber, String action);
}
