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

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(String signedDate) {
        this.signedDate = signedDate;
    }

    public Integer getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(Integer afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    public String getAfterSaleApplyTime() {
        return afterSaleApplyTime;
    }

    public void setAfterSaleApplyTime(String afterSaleApplyTime) {
        this.afterSaleApplyTime = afterSaleApplyTime;
    }

    public String getAfterSaleCompleteTime() {
        return afterSaleCompleteTime;
    }

    public void setAfterSaleCompleteTime(String afterSaleCompleteTime) {
        this.afterSaleCompleteTime = afterSaleCompleteTime;
    }

    public Integer getAfterSaleHandlerId() {
        return afterSaleHandlerId;
    }

    public void setAfterSaleHandlerId(Integer afterSaleHandlerId) {
        this.afterSaleHandlerId = afterSaleHandlerId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAfterSaleNotes() {
        return afterSaleNotes;
    }

    public void setAfterSaleNotes(String afterSaleNotes) {
        this.afterSaleNotes = afterSaleNotes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
