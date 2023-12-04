package cn.twt.open.dao.commoninfo;

import cn.twt.open.dto.commoninfo.MajorDto;
import cn.twt.open.pojo.commoninfo.Major;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Lino
 */
@Mapper
@Repository
public interface MajorDao {
    /**
     * 查询所有专业
     * @return List<MajorDto>
     */
    List<MajorDto> listAllMajors();

    /**
     * 查询学院id对应的所有专业
     * @param departmentId
     * @return List<MajorDto>
     */
    List<Major> listMajorsByDepartmentId(int departmentId);

    /**
     * 添加一个专业
     * @param name
     * @param code
     * @param departmentId
     * @return int
     */
    int addMajor(String name, String code, int departmentId);

    /**
     * 修改专业信息
     * @param id
     * @param name
     * @param code
     * @param departmentId
     * @return
     */
    int updateMajor(int id, String name, String code, int departmentId);

    /**
     * 根据id获取专业
     * @param id
     * @return
     */
    Major getMajorById(int id);
}
