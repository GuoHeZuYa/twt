package cn.twt.open.service.commoninfo.impl;

import cn.twt.open.cache.redis.RedisCacheManager;
import cn.twt.open.constant.CacheKeys;
import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.commoninfo.DepartmentDao;
import cn.twt.open.dto.commoninfo.DepartmentDto;
import cn.twt.open.service.commoninfo.DepartmentService;
import cn.twt.open.utils.APIResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lino
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Resource
    DepartmentDao departmentDao;

    @Resource
    RedisCacheManager redisCacheManager;

    private static ReentrantLock departmentLock = new ReentrantLock();

    @Override
    public APIResponse listAllDepartment() {
        try {
            List data;
            if (Objects.isNull(redisCacheManager.get(CacheKeys.DEPARTMENT_KEY, List.class))){
                data = departmentDao.listAllDepartment();
                redisCacheManager.set(CacheKeys.DEPARTMENT_KEY, data);
            } else {
                System.out.println("缓存");
                data = redisCacheManager.get(CacheKeys.DEPARTMENT_KEY, List.class);
            }
            return APIResponse.success(data);
        } catch (Exception e){
            LOGGER.error("数据库错误",e);
            return APIResponse.error(ErrorCode.DATABASE_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse addDepartment(String name, String code) {
        departmentLock.lock();
        try {
            departmentDao.addDepartment(name, code);
            redisCacheManager.del(CacheKeys.DEPARTMENT_KEY);
            return APIResponse.success(null);
        } finally {
            departmentLock.unlock();
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public APIResponse updateDepartment(int id, String name, String code) {
        departmentLock.lock();
        try {
            departmentDao.updateDepartment(id, name, code);
            redisCacheManager.del(CacheKeys.DEPARTMENT_KEY);
            return APIResponse.success(null);
        } finally {
            departmentLock.unlock();
        }
    }


}
