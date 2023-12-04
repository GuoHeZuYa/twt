package cn.twt.open.controller.commoninfo;

import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.annotation.manage.Manage;
import cn.twt.open.constant.UserRole;
import cn.twt.open.controller.BaseController;
import cn.twt.open.service.commoninfo.MajorService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lino
 * @date 2020-12
 * @description 专业相关接口：增加、删除、编辑、查看
 */

@RestController
@RequestMapping("/api")
public class MajorController extends BaseController {
    @Resource
    MajorService majorService;

    @GetMapping("/major/all")
    public APIResponse getAllMajors(){
        return majorService.listAllMajors();
    }

    @GetMapping("/major/department/{departmentId}")
    public APIResponse listMajorByDepartmentId(@PathVariable("departmentId") int departmentId){
        return majorService.listMajorsByDepartmentId(departmentId);
    }

    @PostMapping("/major")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    @Manage
    public APIResponse addMajor(@RequestParam("name") String name,
                                @RequestParam("code") String code,
                                @RequestParam("departmentId") int departmentId){
        return majorService.addMajor(name, code, departmentId);
    }

    @PutMapping("/major")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    @Manage
    public APIResponse updateMajor(@RequestParam("id") int id,
                                   @RequestParam("name") String name,
                                   @RequestParam("code") String code,
                                   @RequestParam("departmentId") int departmentId){
        return majorService.updateMajor(id, name, code, departmentId);
    }
}
