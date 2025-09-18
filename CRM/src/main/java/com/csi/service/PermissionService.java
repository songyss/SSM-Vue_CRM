package com.csi.service;

import com.csi.domain.Permission;
import java.util.List;
import java.util.Set;

public interface PermissionService {
    List<Permission> findAllPermissions();
    Permission findById(Integer id);
    void savePermission(Permission permission);
    void updatePermission(Permission permission);
    void deletePermission(Integer id);
    

    
    List<Permission> findPermissionsByModuleId(Integer modelId);
}
