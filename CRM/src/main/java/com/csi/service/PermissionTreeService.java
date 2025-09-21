package com.csi.service;

import com.csi.domain.PermissionTreeNode;

import java.util.List;

public interface PermissionTreeService {

    List<PermissionTreeNode> buildPermissionTree();

}
