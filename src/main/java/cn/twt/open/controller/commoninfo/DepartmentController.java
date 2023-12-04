package cn.twt.open.controller.commoninfo;


import cn.twt.open.annotation.jwt.JwtToken;
import cn.twt.open.annotation.manage.Manage;
import cn.twt.open.constant.UserRole;
import cn.twt.open.controller.BaseController;
import cn.twt.open.service.commoninfo.DepartmentService;
import cn.twt.open.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lino
 * @date 2020-12
 * @description 学院相关接口：增加、删除、编辑、查看(所有)
 */

@RestController
@RequestMapping("/api")
public class DepartmentController extends BaseController {

    @Resource
    DepartmentService departmentService;

    @GetMapping("/department/all")
    public APIResponse getAllDepartment(){
        return departmentService.listAllDepartment();
    }

    @PostMapping("/department")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    @Manage
    public APIResponse addDepartment(@RequestParam("name") String name,
                                     @RequestParam("code") String code){
        return departmentService.addDepartment(name,code);
    }

    @PutMapping("/department/{id}")
    @JwtToken(roles = {
            UserRole.TWT_ADMIN
    })
    @Manage
    public APIResponse updateDepartment(@PathVariable("id") int id,
                                        @RequestParam("name") String name,
                                        @RequestParam("code") String code){
        return departmentService.updateDepartment(id, name, code);
    }

}
