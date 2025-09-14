package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Role implements Serializable {

    //角色id
    private Integer roleId;
    //角色名称
    private String roleName;

}
