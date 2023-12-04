package cn.twt.open.controller.commoninfo;

import cn.twt.open.service.commoninfo.SemesterService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lino
 */
@RestController
@RequestMapping("/api")
public class SemesterController {

    @Resource
    SemesterService semesterService;

    @GetMapping("/semester")
    public APIResponse getCurrentSemester(){
        return semesterService.getCurrentSemesterInfo();
    }
}
