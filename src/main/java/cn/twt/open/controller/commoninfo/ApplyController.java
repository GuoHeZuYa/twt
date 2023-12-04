package cn.twt.open.controller.commoninfo;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.constant.UserRole;
import cn.twt.open.dto.commoninfo.MajorApplicationDto;
import cn.twt.open.service.commoninfo.ApplyService;
import cn.twt.open.utils.APIResponse;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Lino
 */
@RestController
@RequestMapping("/api")
@EnableAsync
public class ApplyController {
    @Resource
    ApplyService applyService;

    @GetMapping("/application")
    @JwtToken(roles = {
            UserRole.ASSISTANT
    })
    public APIResponse getAllApplication(@RequestHeader("token") String token){
        return applyService.getAllApplication(token);
    }

    @PostMapping("/application")
    @JwtToken(roles = {
            UserRole.ASSISTANT
    })
    public APIResponse handleApplication(@RequestHeader("token") String token,
                                         @RequestBody MajorApplicationDto majorApplication){
        applyService.handleApplication(token, majorApplication);
        return APIResponse.success(null);
    }
}
