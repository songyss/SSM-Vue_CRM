package com.csi.controller.permission;

import com.csi.domain.Permission;
import com.csi.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ResponseBody
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Permission> permissions = permissionService.findAllPermissions();
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", permissions);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/list/{moduleId}")
    public Map<String, Object> listByModuleId(@PathVariable Integer moduleId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Permission> permissions = permissionService.findPermissionsByModuleId(moduleId);
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", permissions);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Permission permission) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (permission.getId() == null) {
                permissionService.savePermission(permission);
                result.put("code", 200);
                result.put("msg", "权限添加成功");
            } else {
                permissionService.updatePermission(permission);
                result.put("code", 200);
                result.put("msg", "权限更新成功");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "操作失败: " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            permissionService.deletePermission(id);
            result.put("code", 200);
            result.put("msg", "权限删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败: " + e.getMessage());
        }
        return result;
    }
}
