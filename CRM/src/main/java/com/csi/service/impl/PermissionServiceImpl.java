package com.csi.service.impl;

import com.csi.domain.Permission;
import com.csi.domain.RolePermission;
import com.csi.mapper.PermissionMapper;
import com.csi.mapper.RolePermissionMapper;
import com.csi.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Permission> findAllPermissions() {
        return null;
    }

    @Override
    public Permission findById(Integer id) {
        return null;
    }

    @Override
    public void savePermission(Permission permission) {
        permissionMapper.save(permission);
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionMapper.update(permission);
    }

    @Override
    public void deletePermission(Integer id) {
        permissionMapper.deleteById(id);
    }

    
    @Override
    public List<Permission> findPermissionsByModuleId(Integer modelId) {
        return findAllPermissions().stream()
                .filter(p -> p.getModelId().equals(modelId))
                .collect(Collectors.toList());
    }
}
