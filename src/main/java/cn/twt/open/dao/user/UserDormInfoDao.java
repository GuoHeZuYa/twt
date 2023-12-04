package cn.twt.open.dao.user;


import cn.twt.open.dto.user.UserDormInfoDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 郭金良
 * @since 2022-04-05
 */
@Mapper
public interface UserDormInfoDao {

    List<UserDormInfoDto> getDormInfoByUserNumber(String userNumber);

}
