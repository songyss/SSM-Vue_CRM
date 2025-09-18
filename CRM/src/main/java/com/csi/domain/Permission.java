package com.csi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Permission implements Serializable {

    private Integer id;
    private String permissionName;
    private String permissionDesc;
    private String url;
    private Integer modelId;
    private String createTime;
    private String updateTime;
    private Integer isDelete;

}
