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
    //手机号码
    private String phone;
    //员工性别
    private Integer sex;
    //员工生日
    private String borndate;
    //邮箱
    private String email;
    //部门
    private Integer department;
    private String departmentName;
    //角色
    private Integer role;
    private String roleName;
    //上级领导
    private Integer superiorId;
    private String createTime;
    private String updateTime;

}
