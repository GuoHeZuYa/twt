package cn.twt.open.service.commoninfo.impl;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.commoninfo.ApplyDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dto.commoninfo.ChangeMajorDto;
import cn.twt.open.dto.commoninfo.ChangeMajorResult;
import cn.twt.open.dto.commoninfo.MajorApplicationDto;
import cn.twt.open.pojo.user.User;
import cn.twt.open.service.commoninfo.ApplyService;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.JwtUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Lino
 */
@Service
public class ApplyServiceImpl implements ApplyService {
    @Resource
    UserDao userDao;
    @Resource
    ApplyDao applyDao;

    private static ReentrantLock lock = new ReentrantLock();

    @Override
    public APIResponse getAllApplication(String token) {
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            User userInfo = userDao.queryBasicInfo(userNumber);
            int departmentId = userInfo.getDepartmentId();
            List<ChangeMajorDto> allApplication = applyDao.getAllApplication(departmentId);
            return APIResponse.success(allApplication);
        } catch (Exception e){
            LOGGER.error("错误",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Async("asyncTaskExecutor")
    @Override
    public void handleApplication(String token, MajorApplicationDto majorApplicationDto) {
        lock.lock();
        try {
            String userNumber = JwtUtils.getUserNumber(token);
            List<ChangeMajorResult> list = majorApplicationDto.getList();
            int status = majorApplicationDto.getStatus();
            if (status == 1) {
                applyDao.updateUserInfo(list);
            }
            int affectedRows = applyDao.updateApplyStatus(status,list);
            if (affectedRows != 0) {
                String action = status == 1 ? "批准" : "驳回";
                action = action.concat(list.toString()).concat(" 转专业");
                String id = UUID.randomUUID().toString().replaceAll("-","");
                applyDao.addAnOperateLog(id,userNumber, action);
            }
        } finally {
            lock.unlock();
        }
    }
}
