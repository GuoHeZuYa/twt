package cn.twt.open.dao.user;

import cn.twt.open.dto.commoninfo.ChangeMajorDto;
import cn.twt.open.dto.notification.StuTypeFilterDto;
import cn.twt.open.dto.user.UserInfoDto;
import cn.twt.open.pojo.commoninfo.ChangeMajor;
import cn.twt.open.pojo.commoninfo.StudentType;
import cn.twt.open.pojo.user.User;
import cn.twt.open.pojo.user.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Lino
 */
@Mapper
@Repository
public interface UserDao {
    List<UserDo> queryUserInfo(String account, @Nullable Integer stuType);
    List<UserDo> queryUserInfo();
    List<StudentType> getUserType(String userNumber);
    Map<String ,Object> getUserNewNumber(String userNumber, int typeId);
    int updateUserInfo(String userNumber, UserInfoDto dto);
    List<ChangeMajorDto> getMyChangeMajor(String userNumber);
    List<ChangeMajor> getUnPassedApply(String userNumber);
    User queryBasicInfo(String userNumber);
    int changeMajor(String userNumber, String username, int sourceDepartmentId,
                    int sourceMajorId,int destDepartmentId, int destMajorId);
    int updateUserPhone(String userNumber, String phone);
    int updateUserEmail(String userNumber, String email);
    int updateUsername(String userNumber, String username);
    int updateUser(@Param("user") User user);
    int createUser(@Param("user") User user);
    int getIdNumberCount(String idNumber);
    int countUserByUserNumber(String userNumber);
    List<String> getSpecificUserNumber(@Param("filter") StuTypeFilterDto stuTypeFilterDto);
    User queryUserBaseInfo(@Param("userNumber") String userNumber);
    List<User> batchQueryUserBaseInfo(@Param("userNumbers") List<String> userNumber);
    int updateUserAvatar(@Param("userNumber") String userNumber,
                         @Param("avatarUrl") String avatarUrl);
    User queryUserByUserNumberAndIdNumber(@Param("userNumber") String userNumber,
                                          @Param("idNumber") String idNumber);
    String queryIdNumberByStuNum(@Param("userNumber")String userNumber);
}
