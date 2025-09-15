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
    //客户性别
    private Integer sex;
    //客户生日
    private String borndate;
    //客户所在公司
    private String company;
    //客户职位
    private String position;
    //客户来源
    private String source;
    //销售更改状态
    private Integer status;
    private String statusName;
    //销售备注
    private String notes;
    //电话销售更改状态
    private Integer sdrStatus;
    private String sdrStatusName;
    //电话销售备注
    private String sdrNotes;
    //负责的销售
    private Integer assigneeId;
    private String name1;
    //创建人
    private Integer creatorId;
    private String name2;
    //最后跟进时间
    private String lastFollowUPTime;
    //是否放进客户池
    private int isPool;
    //放入客户池时间
    private String poolTime;
    //关联活动id
    private Integer activityId;

}
