package com.csi.mapper;

import com.csi.domain.Permission;
import com.csi.domain.RolePermission;

import java.util.List;

public interface RolePermissionMapper {

    List<RolePermission> selectPermissions(int roleId);

    //通过切割完的permission的permission_id集合查询
    String selectPermissionByList(int id);

    int getModelIdByPermissionId(int permissionId);

    int save(RolePermission rolePermission);

    int remove(int roleId);
}
