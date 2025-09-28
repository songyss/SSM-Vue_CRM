package com.csi.controller.role;

import com.csi.annotation.OperateLog;
import com.csi.domain.Role;
import com.csi.service.RoleService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // 获取所有角色
    @GetMapping("/list")
    @OperateLog(operation = "获取所有角色", targetType = "角色")
    public R getAllRoles(
            @RequestParam(value = "roleName", required = false) String roleName,
            @RequestParam(value = "isDelete", required = false) Integer isDelete
    ) {
        try {
            List<Role> roles = roleService.findAllRoles(roleName, isDelete);
            return R.ok(roles);
        } catch (Exception e) {
            return R.error();
        }
    }

    // 根据ID获取角色
    @GetMapping("/get")
    @OperateLog(operation = "根据ID获取角色", targetType = "角色")
    public R getRoleById(@RequestParam("id") Integer id) {
        try {
            Role role = roleService.findById(id);
            if (role != null) {
                return R.ok(role);
            } else {
                return R.error();
            }
        } catch (Exception e) {
            return R.error();
        }
    }

    // 创建新角色
    @PostMapping("/create")
    @OperateLog(operation = "创建新角色", targetType = "角色")
    public R createRole(@RequestBody Role role) {
        try {
            role.setIsDelete(0);
            roleService.saveRole(role);
            return R.okMessage("角色创建成功");
        } catch (Exception e) {
            return R.error();
        }
    }

    // 更新角色
    @PostMapping("/update")
    @OperateLog(operation = "更新角色", targetType = "角色")
    public R updateRole(@RequestParam("id") Integer id, @RequestBody Role role) {
        try {
            role.setId(id);
            roleService.updateRole(role);
            return R.okMessage("角色更新成功");
        } catch (Exception e) {
            return R.error();
        }
    }

    // 删除角色
    @PostMapping("/delete")
    @OperateLog(operation = "删除角色", targetType = "角色")
    public R deleteRole(@RequestParam("id") Integer id) {
        try {
            roleService.deleteRole(id);
            return R.okMessage("角色删除成功");
        } catch (Exception e) {
            return R.error();
        }
    }


}
