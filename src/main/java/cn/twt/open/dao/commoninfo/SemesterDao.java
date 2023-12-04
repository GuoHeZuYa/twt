package cn.twt.open.dao.commoninfo;

import cn.twt.open.pojo.commoninfo.Semester;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SemesterDao {
    Semester getCurrentSemesterInfo();
}
