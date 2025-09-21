package com.csi.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionVO implements Serializable {

    private Integer id;
    private String permissionName;
    private String permissionDesc;
    private String url;

}
