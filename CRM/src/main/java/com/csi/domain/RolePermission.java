package com.csi.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class RolePermission implements Serializable {
    private Integer id;
    private Integer roleId;
    private Integer modelId;
    private Integer permissionId;
    private String createTime;
    private String updateTime;
    private Integer isDelete;
}
