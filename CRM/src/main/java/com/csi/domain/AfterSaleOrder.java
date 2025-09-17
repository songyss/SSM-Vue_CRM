package com.csi.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 售后订单实体类
 * */
@Data
public class AfterSaleOrder implements Serializable {
    //编号
    private Integer id;
    //订单号
    private String orderNumber;
    //客户ID
    private Integer customerId;
    //订单总金额
    private double totalAmount;
    //签约日期
    private String signedDate;
    //售后订单状态
    private Integer afterSaleStatus;
    //售后申请时间
    private String afterSaleApplyTime;
    //售后完成时间
    private String afterSaleCompleteTime;
    //售后处理人ID
    private Integer afterSaleHandlerId;
    //合同文件路径
    private String fileUrl;
    //订单备注
    private String notes;
    //售后处理备注
    private String afterSaleNotes;
    //创建时间
    private String createTime;
    //更新时间
    private String updateTime;

    //售后订单状态
    private String statusName;

    //客户名称
    private String customerName;

}
