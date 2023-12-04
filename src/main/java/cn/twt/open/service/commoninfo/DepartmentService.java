package cn.twt.open.service.commoninfo;

import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

/**
 * @author Lino
 */
public interface DepartmentService extends BaseService {
    /**
     * 查询所有学院
     * @return APIResponse
     */
    APIResponse listAllDepartment();

    /**
     * 添加一个学院
     * @param name 名称
     * @param code 代码
     * @return APIResponse
     */
    APIResponse addDepartment(String name, String code);

    /**
     * 更新学院信息
     * @param id 学院id
     * @param name 学院名称
     * @param code 学院代码
     * @return
     */
    APIResponse updateDepartment(int id, String name, String code);
}
