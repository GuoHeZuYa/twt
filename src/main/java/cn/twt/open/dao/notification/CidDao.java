package cn.twt.open.dao.notification;

import cn.twt.open.dto.notification.StuTypeFilterDto;
import cn.twt.open.pojo.notification.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Mapper
public interface CidDao {
    ArrayList<String> getAllCid();
    ArrayList<String> getCidByUserNumber(@Param("userNumbers") List<String> userNumber);
    String getCidBySingleUserNumber(@Param("userNumber") String userNumber);
    Integer insertCid(@Param("cid") String cid, @Param("userNumber") String userNumber);
    Integer countUserNumber(@Param("userNumber") String userNumber);
    Integer updateCid(@Param("cid") String cid, @Param("userNumber") String userNumber);
    ArrayList<String> getSpecificCid(@Param("filter") StuTypeFilterDto stuTypeFilterDto);
    ArrayList<Notification> getHistoryNotification();
}
