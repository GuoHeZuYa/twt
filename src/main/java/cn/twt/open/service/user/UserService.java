package cn.twt.open.service.user;

import cn.twt.open.dto.notification.StuTypeFilterDto;
import cn.twt.open.dto.user.UserInfoDto;
import cn.twt.open.pojo.user.User;
import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lino
 */
public interface UserService extends BaseService {
    /**
     * 获取单个用户
     * @param userNumber
     * @return
     */
    public APIResponse fetchSingleUser(String userNumber);

    /**
     * 根据学号获取批量用户
     * @param userNumbers
     * @return
     */
    public APIResponse fetchUsers(String userNumbers);

//    /**
//     * 获取单个用户, 包含宿舍信息
//     * @param userNumber
//     * @return
//     */
//    public APIResponse fetchSingleUserWithDormInfo(String userNumber);

    /**
     * 获取所有用户
     * @return
     */
    public APIResponse fetchAllUsers();

    /**
     * 获取指定类型的用户
     * @param type
     * @return
     */
    public APIResponse fetchSpecificTypeUsers(Integer type);

    /**
     * 更新用户信息
     * @param token
     * @param dto
     * @param request
     * @return
     */
    public APIResponse updateUserInfo(String token, UserInfoDto dto, HttpServletRequest request);

    /**
     * 超管重置用户密码
     * @param userNumber
     * @return
     */
    public APIResponse resetUserPassword(String userNumber,String newPassword);


    APIResponse resetUserPasswordToDefault(String userNumber);


    /**
     * 获取我的转专业申请信息
     * @param token
     * @return
     */
    public APIResponse getMyChangeMajor(String token);

    /**
     * 申请转专业
     * @param token
     * @param destDepartmentId 目的学院id
     * @param destMajorId 目的专业id
     * @return
     */
    public APIResponse changeMajor(String token, int destDepartmentId, int destMajorId);

    /**
     * 单独修改手机号
     * @param token
     * @param phone
     * @param verifyCode
     * @param request
     * @return
     */
    public APIResponse updateUserPhone(String token, String phone, String verifyCode, HttpServletRequest request);

    /**
     * 单独修改邮箱（后续加验证码）
     * @param token
     * @param email
     * @return
     */
    public APIResponse updateUserEmail(String token, String email);

    /**
     * 修改用户名
     * @param token
     * @param username
     * @return
     */
    public APIResponse updateUsername(String token, String legalUsername, String username);

    /**
     * 创建或更新用户
     * @param user
     * @return
     */
    public APIResponse updateOrCreateUserInfo(User user, int status);

    List<String> getSpecificUserNumber(StuTypeFilterDto stuTypeFilterDto);

    public APIResponse uploadUserAvatar(String token, MultipartFile file);

    APIResponse checkIdNumIsSame(String numA,String numB);
}
