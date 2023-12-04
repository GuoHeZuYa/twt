package cn.twt.open.cronJob.service;

import cn.twt.open.constant.ReportStatusEnum;
import cn.twt.open.dao.user.AssistantStudentRelationDao;
import cn.twt.open.dao.user.UserDao;
import cn.twt.open.dto.commoninfo.ReportedStudentDto;
import cn.twt.open.pojo.user.UserDo;
import cn.twt.open.service.commoninfo.impl.ReturnSchoolServiceImpl;
import cn.twt.open.utils.MessageUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lino
 */
@Service
@Slf4j
public class ReturnSchoolCronService {


    @Resource
    AssistantStudentRelationDao assistantStudentRelationDao;

    @Resource
    ReturnSchoolServiceImpl returnSchoolServiceImpl;

    @Resource
    MessageUtils messageUtils;

    @Resource
    UserDao userDao;

    public void sendMsgToAllAssistants() throws ParseException {
        List<String> allAssistantUserNumber = assistantStudentRelationDao.getAllAssistantUserNumber();
        List<UserDo> allAssistantDo = allAssistantUserNumber.stream()
                .map(userNumber -> {
                    List<UserDo> userDos = userDao.queryUserInfo(userNumber, null);
                    if (CollectionUtils.isEmpty(userDos)){
                        return new UserDo();
                    }
                    userDos.get(0).formatNullToEmptyString();
                    return userDos.get(0);
                })
                .filter(userDo -> StringUtils.isNotEmpty(userDo.getTelephone()))
                .collect(Collectors.toList());
        for (UserDo assistant : allAssistantDo){
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = simpleDateFormat.format(date);
            List<ReportedStudentDto> allStudents = returnSchoolServiceImpl.getAllStudentAboutHealthReport(assistant.getUserNumber(),today);
            long unReportedNum = allStudents.stream().filter(reportedStudentDto ->
                    reportedStudentDto
                            .getReportStatus()
                            .equals(ReportStatusEnum.UN_REPORT.getDescription()))
                    .count();
            if (unReportedNum != NumberUtils.LONG_ZERO){
                messageUtils.sendHealthReportConditionMessage(assistant.getTelephone(), String.valueOf(unReportedNum));
            } else {
                log.info("userNumber: {} 的学生全部填报完成", assistant.getUserNumber());
            }
        }
    }
}
