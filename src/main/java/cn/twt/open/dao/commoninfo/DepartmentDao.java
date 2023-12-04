package cn.twt.open.dao.commoninfo;

import cn.twt.open.dto.commoninfo.DepartmentDto;
import cn.twt.open.pojo.commoninfo.Department;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lino
 */
@Repository
@Mapper
public interface DepartmentDao {
    /**
     * 查询所有学院
     * @return List<DepartmentDto>
     */
    List<DepartmentDto> listAllDepartment();

    /**
     * 添加一个学院
     * @param name
     * @param code
     * @return
     */
    int addDepartment(String name, String code);

    /**
     * 更新学院信息
     * @param id
     * @param name
     * @param code
     * @return
     */
    int updateDepartment(int id, String name, String code);

    /**
     * 根据id获取学院信息
     * @param id
     * @return Department
     */
    Department getDepartmentById(int id);
}
