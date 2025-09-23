package com.csi.mapper;

import com.csi.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper {
    List<Role> findAll(@Param("roleName") String roleName,@Param("isDelete") Integer isDelete);
    Role findById(Integer id);
    void save(Role role);
    void update(Role role);
    void deleteById(Integer id);
}
