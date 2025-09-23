package com.csi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Department implements Serializable {

    private Integer departmentId;
    private String departmentName;
    private String phone;
    private Integer superDepartId;
    private Integer isDelete;
    private String createTime;
    private String modifyTime;

}
