package com.csi.mapper;

import com.csi.domain.Permission;
import com.csi.domain.vo.PermissionVO;

import java.util.List;

public interface PermissionMapper {
    List<PermissionVO> findAllByModelId(int modelId);
    Permission findById(Integer id);
    void save(Permission permission);
    void update(Permission permission);
    void deleteById(Integer id);
}
