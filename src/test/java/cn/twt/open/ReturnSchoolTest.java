package cn.twt.open;

import cn.twt.open.dao.commoninfo.ReturnSchoolDao;
import cn.twt.open.dto.commoninfo.ReportedStudentDto;
import cn.twt.open.dto.commoninfo.ReturnSchoolInfoDto;
import cn.twt.open.pojo.commoninfo.ReturnSchoolInfo;
import cn.twt.open.service.commoninfo.ReturnSchoolService;
import cn.twt.open.utils.APIResponse;
import cn.twt.open.utils.DateUtils;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ReturnSchoolTest {

    @Resource
    ReturnSchoolDao returnSchoolDao;

    @Resource
    ReturnSchoolService returnSchoolService;

    @Test
    void test() throws ParseException {
        List<String> userNumbers = Lists.newArrayList("3019233035", "3018216093");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = "2021-08-07";
        Date time = simpleDateFormat.parse(date);
        System.out.println(time);
        Timestamp startTime = new Timestamp(time.getTime());
        Timestamp endTime = new Timestamp(time.getTime()+ DateUtils.ONE_DAY);
        System.out.println(startTime);
        System.out.println(endTime);
        List<ReturnSchoolInfo> recordByUserNumberListAndDate = returnSchoolDao.getRecordByUserNumberListAndDate(userNumbers, startTime, endTime);
        APIResponse aaa = returnSchoolService.getStudentsReportByAssistant("aaa", date);
        List<ReportedStudentDto> res = (List<ReportedStudentDto>) aaa.getResult();
        System.out.println(res.size());
        for (ReportedStudentDto r : res){
            System.out.println(r);
        }
    }
}
