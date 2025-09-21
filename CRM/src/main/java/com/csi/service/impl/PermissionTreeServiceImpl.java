package com.csi.service.impl;

import com.csi.domain.Model;
import com.csi.domain.Permission;
import com.csi.domain.PermissionTreeNode;
import com.csi.domain.vo.PermissionVO;
import com.csi.mapper.ModelMapper;
import com.csi.mapper.PermissionMapper;
import com.csi.service.PermissionTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionTreeServiceImpl implements PermissionTreeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    public List<PermissionTreeNode> buildPermissionTree() {
        // 1. 查询所有模块
        List<Model> models = modelMapper.findAll();

        // 2. 构建树结构
        List<PermissionTreeNode> treeNodes = new ArrayList<>();

        for (Model model : models) {
            PermissionTreeNode node = new PermissionTreeNode();
            node.setId(model.getId());
            node.setModelName(model.getModelName());

            // 查询该模块下的所有权限
            List<PermissionVO> permissions = permissionMapper.findAllByModelId(model.getId());
            node.setPermissions(permissions);

            treeNodes.add(node);
        }

        return treeNodes;
    }
}
