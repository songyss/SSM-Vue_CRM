package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Department implements Serializable {

    //部门id
    private Integer departmentId;
    //部门名称
    private String departmentName;

}
