package com.csi.service;

import com.csi.domain.Role;
import com.csi.domain.RolePermission;
import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();
    Role findById(Integer id);
    void saveRole(Role role);
    void updateRole(Role role);
    void deleteRole(Integer id);
    
    List<RolePermission> findPermissionsByRoleId(Integer roleId);
    void assignPermissionToRole(Integer roleId, Integer permissionId);
    void removePermissionFromRole(Integer roleId, Integer permissionId);
}
