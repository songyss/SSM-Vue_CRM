package com.csi.service.impl;

import com.csi.domain.Role;
import com.csi.domain.RolePermission;
import com.csi.mapper.RoleMapper;
import com.csi.mapper.RolePermissionMapper;
import com.csi.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> findAllRoles() {
        return roleMapper.findAll();
    }

    @Override
    public Role findById(Integer id) {
        return roleMapper.findById(id);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.save(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.update(role);
    }

    @Override
    public void deleteRole(Integer id) {
        roleMapper.deleteById(id);
    }

    @Override
    public List<RolePermission> findPermissionsByRoleId(Integer roleId) {
        return rolePermissionMapper.findByRoleId(roleId);
    }

    @Override
    public void assignPermissionToRole(Integer roleId, Integer permissionId) {
        RolePermission rp = new RolePermission();
        rp.setRoleId(roleId);
        rp.setPermissionId(permissionId);
        rp.setIsDelete(0);
        rolePermissionMapper.save(rp);
    }

    @Override
    public void removePermissionFromRole(Integer roleId, Integer permissionId) {
        rolePermissionMapper.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }
}
