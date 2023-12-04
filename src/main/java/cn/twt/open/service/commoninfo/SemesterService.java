package cn.twt.open.service.commoninfo;

import cn.twt.open.service.BaseService;
import cn.twt.open.utils.APIResponse;

/**
 * @author Lino
 */
public interface SemesterService extends BaseService {
    public APIResponse getCurrentSemesterInfo();
}
