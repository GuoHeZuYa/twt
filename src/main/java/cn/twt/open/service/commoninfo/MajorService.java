package cn.twt.open.service.commoninfo;

import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

/**
 * @author Lino
 */
public interface MajorService extends BaseService {
    /**
     * 查询所有专业
     * @return APIResponse
     */
    APIResponse listAllMajors();

    /**
     * 根据学院id查询对应的所有专业
     * @param departmentId 学院id
     * @return APIResponse
     */
    APIResponse listMajorsByDepartmentId(int departmentId);

    /**
     * 添加一个专业
     * @param name 专业名称
     * @param code 专业代码
     * @param departmentId 专业所属的学院id
     * @return
     */
    APIResponse addMajor(String name, String code, int departmentId);

    /**
     * 更新专业信息
     * @param name
     * @param code
     * @param departmentId
     * @return
     */
    APIResponse updateMajor(int id, String name, String code, int departmentId);
}
