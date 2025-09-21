package com.csi.domain;

import com.csi.domain.vo.PermissionVO;
import lombok.Data;

import java.util.List;
@Data
public class PermissionTreeNode {

    private Integer id;
    private String modelName; // 模块名
    private List<PermissionVO> permissions; // 权限列表

}
