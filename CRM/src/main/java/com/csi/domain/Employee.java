package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Employee implements Serializable {

    //员工id
    private Integer id;
    //账号
    private String username;
    //密码
    private String password;
    //姓名
    private String name;
    //部门
    private Department department;
    //角色
    private Role role;
    //上级领导
    private Integer superiorId;

}
