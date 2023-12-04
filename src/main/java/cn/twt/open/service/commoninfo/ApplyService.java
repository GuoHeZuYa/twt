package cn.twt.open.service.commoninfo;

import cn.twt.open.dto.commoninfo.MajorApplicationDto;
import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

/**
 * @author Lino
 */
public interface ApplyService extends BaseService {
    /**
     * 辅导员获取本学院所有转专业申请
     * @param token
     * @return
     */
    APIResponse getAllApplication(String token);

    /**
     * 批量处理转专业申请
     * @param token
     * @param majorApplicationDto
     * @return
     */
    void handleApplication(String token, MajorApplicationDto majorApplicationDto);
}
