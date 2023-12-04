package cn.twt.open.service.commoninfo;

import cn.twt.open.dto.commoninfo.ReturnSchoolInfoDto;
import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

import javax.servlet.http.HttpServletResponse;

public interface ReturnSchoolService extends BaseService {
    APIResponse collectInformation(String token, ReturnSchoolInfoDto returnSchoolInfoDto);
    APIResponse getInfo(String token);
    APIResponse todayReportStatus(String token);
    APIResponse getStudentsReportByAssistant(String token, String date);
    void exportHealthInfoExcel(String token, String start, String end, HttpServletResponse response);
}
