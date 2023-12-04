package cn.twt.open.service.commoninfo.impl;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.commoninfo.DepartmentDao;
import cn.twt.open.dao.commoninfo.MajorDao;
import cn.twt.open.pojo.commoninfo.Department;
import cn.twt.open.pojo.commoninfo.Major;
import cn.twt.open.service.commoninfo.MajorService;
import cn.twt.open.utils.APIResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lino
 */
@Service
public class MajorServiceImpl implements MajorService {

    @Resource
    MajorDao majorDao;

    @Resource
    DepartmentDao departmentDao;

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public APIResponse listAllMajors() {
        try {
            return APIResponse.success(majorDao.listAllMajors());
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public APIResponse listMajorsByDepartmentId(int departmentId) {
        try {
            Department department = departmentDao.getDepartmentById(departmentId);
            if (department == null){
                return APIResponse.error(ErrorCode.NO_SUCH_DEPARTMENT);
            }
            String departmentName = department.getName();
            List<Major> majors = majorDao.listMajorsByDepartmentId(departmentId);
            HashMap<String, Object> result = new HashMap<>(3);
            result.put("departmentId",departmentId);
            result.put("departmentName",departmentName);
            result.put("majors", majors);
            return APIResponse.success(result);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse addMajor(String name, String code, int departmentId) {
        lock.lock();
        try {
            Department department = departmentDao.getDepartmentById(departmentId);
            if (department == null){
                return APIResponse.error(ErrorCode.NO_SUCH_DEPARTMENT);
            }
            majorDao.addMajor(name, code, departmentId);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }


    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateMajor(int id, String name, String code, int departmentId) {
        lock.lock();
        try {
            Department department = departmentDao.getDepartmentById(departmentId);
            if (department == null){
                return APIResponse.error(ErrorCode.NO_SUCH_DEPARTMENT);
            }
            majorDao.updateMajor(id, name, code, departmentId);
            return APIResponse.success(null);
        } finally {
            lock.unlock();
        }
    }
}
