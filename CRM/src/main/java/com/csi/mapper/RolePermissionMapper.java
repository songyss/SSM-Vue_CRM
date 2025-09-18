package com.csi.mapper;

import com.csi.domain.RolePermission;
import java.util.List;

public interface RolePermissionMapper {
    List<RolePermission> findAll();
    List<RolePermission> findByRoleId(Integer roleId);
    List<RolePermission> findByPermissionId(Integer permissionId);
    void save(RolePermission rolePermission);
    void update(RolePermission rolePermission);
    void deleteById(Integer id);
    void deleteByRoleIdAndPermissionId(Integer roleId, Integer permissionId);
    void deleteByRoleId(Integer roleId);
    List<RolePermission> findByRoleIdAndModuleId(Integer roleId, Integer modelId);
}
