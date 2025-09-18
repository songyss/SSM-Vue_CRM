package com.csi.mapper;

import com.csi.domain.Role;
import java.util.List;

public interface RoleMapper {
    List<Role> findAll();
    Role findById(Integer id);
    void save(Role role);
    void update(Role role);
    void deleteById(Integer id);
}
