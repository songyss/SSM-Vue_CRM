package com.csi.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomerVO implements Serializable {
    private Long id;
    private String name;
    private String phone;
    private Integer sex;
    private Integer status;
    private String company;
    private String position;
    private String remark;

    // 单个订单号和商机名
    private String orderNumber;
    private String opportunityName;

    // 一对多关系（备用）
    private List<OrderVO> orders;
    private List<OpportunityVO> opportunities;

}
