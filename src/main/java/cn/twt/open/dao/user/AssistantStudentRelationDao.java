package cn.twt.open.dao.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Mapper
@Repository
public interface AssistantStudentRelationDao {
    public List<String> getManagedStudentsByAssistantUserNumber(@Param("userNumber") String userNumber);
    public List<String> getAllAssistantUserNumber();
    public List<String> getAllUserNumber();
}
