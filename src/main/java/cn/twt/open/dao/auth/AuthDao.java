package cn.twt.open.dao.auth;

import cn.twt.open.dto.auth.RegisterDto;
import cn.twt.open.pojo.auth.Credential;
import cn.twt.open.pojo.notification.HeSuanNotification;
import cn.twt.open.pojo.user.UserHeSuanGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Lino
 */
@Mapper
@Repository
public interface AuthDao {
    public List<Credential> getUserCredentialByAccount(String account);
    public int updateUserToken(String userNumber, String token);
    public int getUserCount(String userNumber, String idNumber);
    public int getUserNumberCount(String userNumber);
    public int getUsernameCount(String username);
    public int getEmailCount(String email);
    public int getPhoneCount(String phone);
    public int registerUser(@Param("dto") RegisterDto dto);
    public int updateCredential(String oldUserNumber,
                                String userNumber,
                                String token);
    public int strictDeleteUser(@Param("userNumber") String userNumber);
    public List<UserHeSuanGroup> getHeSuanGroup(String userNumber);
    public List<HeSuanNotification> checkHeSuan(String type, String campus, String method);
}
