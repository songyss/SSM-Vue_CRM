package com.csi.mapper;

import com.csi.domain.Permission;
import java.util.List;

public interface PermissionMapper {
    List<Permission> findAll();
    Permission findById(Integer id);
    void save(Permission permission);
    void update(Permission permission);
    void deleteById(Integer id);
}
