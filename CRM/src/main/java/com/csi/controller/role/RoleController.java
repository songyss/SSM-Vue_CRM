package com.csi.controller.role;

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
    @GetMapping
    public R getAllRoles() {
        try {
            List<Role> roles = roleService.findAllRoles();
            return R.ok(roles);
        } catch (Exception e) {
            return R.error();
        }
    }

    // 根据ID获取角色
    @GetMapping("/get")
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
    public R deleteRole(@RequestParam("id") Integer id) {
        try {
            roleService.deleteRole(id);
            return R.okMessage("角色删除成功");
        } catch (Exception e) {
            return R.error();
        }
    }

    // 获取角色的所有权限
    @GetMapping("/permissions")
    public R getRolePermissions(@RequestParam("roleId") Integer roleId) {
        try {
            List<com.csi.domain.RolePermission> permissions = roleService.findPermissionsByRoleId(roleId);
            return R.ok(permissions);
        } catch (Exception e) {
            return R.error();
        }
    }

    // 为角色分配权限
    @PostMapping("/assign")
    public R assignPermissionToRole(@RequestParam("roleId") Integer roleId,
                                    @RequestParam("permissionId") Integer permissionId) {
        try {
            roleService.assignPermissionToRole(roleId, permissionId);
            return R.okMessage("权限分配成功");
        } catch (Exception e) {
            return R.error();
        }
    }

    // 移除角色权限
    @PostMapping("/remove")
    public R removePermissionFromRole(@RequestParam("roleId") Integer roleId,
                                      @RequestParam("permissionId") Integer permissionId) {
        try {
            roleService.removePermissionFromRole(roleId, permissionId);
            return R.okMessage("权限移除成功");
        } catch (Exception e) {
            return R.error();
        }
    }
}
