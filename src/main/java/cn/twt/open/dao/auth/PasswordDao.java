package cn.twt.open.dao.auth;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PasswordDao {
    public int updatePassword(String phone, String password);
    public int updatePasswordWhenLogin(String userNumber, String password);
}
