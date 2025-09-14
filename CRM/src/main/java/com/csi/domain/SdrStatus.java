package com.csi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SdrStatus implements Serializable {

    //电话销售改变的状态
    private Integer sdrStatusId;
    //状态名称
    private String sdrStatusName;

}
