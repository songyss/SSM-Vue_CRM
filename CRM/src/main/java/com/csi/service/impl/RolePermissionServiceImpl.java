package com.csi.service.impl;

import com.csi.domain.Permission;
import com.csi.domain.RolePermission;
import com.csi.mapper.RolePermissionMapper;
import com.csi.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<RolePermission> selectPermissions(int roleId) {
        return rolePermissionMapper.selectPermissions(roleId);
    }

    @Override
    public String selectPermissionByList(int id) {
        return rolePermissionMapper.selectPermissionByList(id);
    }
}
