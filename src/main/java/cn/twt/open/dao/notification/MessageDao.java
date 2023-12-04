package cn.twt.open.dao.notification;

import cn.twt.open.dto.notification.MessageDto;
import cn.twt.open.pojo.notification.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDao {
    List<Message> getMessageByUserNumber(@Param("userNumber") String userNumber);
    List<Message> getHistoryMessage();
    int addMessage2AllUsers(@Param("message") Message message);
    int addMessage2SomeUsers(@Param("users") List<String> userNumber, @Param("message") Message message);
}
