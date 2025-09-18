package com.csi.util;

import com.csi.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PermissionUtils {

    @Autowired
    private PermissionService permissionService;

    /**
     * 检查员工是否具有指定权限
     *
     * @param employeeId 员工ID
     * @param roleName 角色名称
     * @return 是否具有权限
     */
    public boolean hasPermission(Integer employeeId, String roleName) {
        // 获取员工的所有权限（包括角色权限和临时权限）
        Set<String> permissions = permissionService.getEmployeePermissions(employeeId);
        
        // 获取员工角色的基础权限
        Set<String> rolePermissions = permissionService.getRolePermissions(getEmployeeRoleId(employeeId));
        
        // 合并权限
        permissions.addAll(rolePermissions);
        
        return permissions.contains(roleName);
    }

    // 这里需要根据员工ID获取其角色ID，实际项目中应该通过员工Mapper查询
    private Integer getEmployeeRoleId(Integer employeeId) {
        // 暂时返回固定值，实际应该查询数据库
        return 1; // 示例角色ID
    }
}
