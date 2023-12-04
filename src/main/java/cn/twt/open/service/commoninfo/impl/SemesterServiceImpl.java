package cn.twt.open.service.commoninfo.impl;

import cn.twt.open.constant.ErrorCode;
import cn.twt.open.dao.commoninfo.SemesterDao;
import cn.twt.open.dto.commoninfo.SemesterDto;
import cn.twt.open.pojo.commoninfo.Semester;
import cn.twt.open.service.commoninfo.SemesterService;
import cn.twt.open.utils.APIResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * @author Lino
 */
@Service
public class SemesterServiceImpl implements SemesterService {

    @Resource
    SemesterDao semesterDao;

    @Override
    public APIResponse getCurrentSemesterInfo() {
        try {
            Semester currentSemester = semesterDao.getCurrentSemesterInfo();
            // 秒级时间戳
            Long timestamp = currentSemester.getSemesterStartAt().getTime() / 1000;
            LocalDate date = currentSemester.getSemesterStartAt().toLocalDateTime().toLocalDate();
            SemesterDto dto = new SemesterDto(currentSemester.getSemesterName(), date, timestamp);
            return APIResponse.success(dto);
        } catch (Exception e){
            LOGGER.error("error",e);
            return APIResponse.error(ErrorCode.LOGIC_ERROR);
        }


    }
}
