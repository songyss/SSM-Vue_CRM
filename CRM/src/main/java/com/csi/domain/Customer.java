package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Customer implements Serializable {

    //客户id
    private Integer id;
    //客户名称
    private String name;
    //客户手机号
    private String phone;
    //客户所在公司
    private String company;
    //客户职位
    private String position;
    //客户来源
    private String source;
    //销售更改状态
    //private Status status;
    //电话销售更改状态
    //private SdrStatus sdrStatus;
    //电话销售
    private String sdrNotes;
    //负责的销售id
    private Integer assigneeId;
    //创建人id
    private Integer creatorId;
    //最后跟进时间
    private String lastFollowUPTime;


}
