package cn.twt.open.controller.commoninfo;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.constant.UserRole;
import cn.twt.open.controller.BaseController;
import cn.twt.open.dto.commoninfo.ReturnSchoolInfoDto;
import cn.twt.open.service.commoninfo.ReturnSchoolService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author Lino
 * 健康信息填报Controller，之所以叫ReturnSchoolInfo是因为之前只用于返校时提交健康信息
 * 现用于疫情信息填报，特做说明
 */
@RestController
@RequestMapping("/api")
public class ReturnSchoolInfoController extends BaseController {

    @Resource
    ReturnSchoolService returnSchoolService;

    /**
     * 提交健康信息
     * @param token
     * @param returnSchoolInfoDto
     * @return
     */
    @JwtToken
    @PostMapping("/returnSchool/record")
    public APIResponse collectInformation(@RequestHeader("token") String token, ReturnSchoolInfoDto returnSchoolInfoDto){
        if (Objects.isNull(returnSchoolInfoDto)){
            throw new RuntimeException("请求参数为空");
        }
        LOGGER.info(returnSchoolInfoDto.toString());
        return returnSchoolService.collectInformation(token, returnSchoolInfoDto);
    }

    /**
     * 查询自己的健康信息
     * @param token
     * @return
     */
    @JwtToken
    @GetMapping("/returnSchool/record")
    public APIResponse getInfo(@RequestHeader("token") String token){
        return returnSchoolService.getInfo(token);
    }

    /**
     * 检查当日是否提交健康信息
     * @param token
     * @return
     */
    @JwtToken
    @GetMapping("/returnSchool/status")
    public APIResponse todayReportStatus(@RequestHeader("token") String token){
        return returnSchoolService.todayReportStatus(token);
    }

    /**
     * 辅导员查看自己所管理已填报的学生的健康信息
     * @param token
     * @param date yyyy-MM-dd
     * @return
     */
    @JwtToken(roles = {
            UserRole.ASSISTANT
    })
    @GetMapping("/returnSchool/assistant/report")
    public APIResponse getStudentsReportByAssistant(@RequestHeader("token") String token,
                                                    @RequestParam("date") String date){
        return returnSchoolService.getStudentsReportByAssistant(token ,date);
    }

    @JwtToken(roles = {
            UserRole.ASSISTANT
    })
    @GetMapping("/returnSchool/exportExcel/{start}/{end}")
    public void exportHealthInfoExcel(@RequestHeader("token") String token,
                                      @PathVariable("start") String start,
                                      @PathVariable("end") String end,
                                      HttpServletResponse response) {
        returnSchoolService.exportHealthInfoExcel(token, start, end, response);
    }

}
