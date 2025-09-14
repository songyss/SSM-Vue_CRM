package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class Status implements Serializable {

    //销售改变的状态
    private Integer statusId;
    //状态名称
    private String statusName;

}
