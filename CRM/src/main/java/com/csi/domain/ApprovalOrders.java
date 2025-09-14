package com.csi.domain;
import java.math.BigDecimal;
import java.util.Date;
import java.io.Serializable;
import lombok.Data;


/**
 * 审批单表(ApprovalOrders)实体类
 *
 * @author admin
 * @since 2025-09-14 15:18:41
 */


@Data

public class ApprovalOrders implements Serializable {
    private static final long serialVersionUID = -84422275602689367L;
/**
     * 主键ID
     */
    private Integer id;

/**
     * 审批类型
     */
    private Integer type;
    private String typeName;

/**
     * 客户ID
     */
    private Integer customerId;

/**
     * 商机ID
     */
    private Integer opportunityId;

/**
     * 订单ID
     */
    private Integer orderId;

/**
     * 申请人ID
     */
    private Integer applicantId;

/**
     * 审批人ID
     */
    private Integer approverId;

/**
     * 申请内容
     */
    private String content;

/**
     * 申请金额
     */
    private double requestedAmount;

/**
     * 审批状态
     */
    private Integer status;
    private String statusName;

/**
     * 审批反馈
     */
    private String feedback;

/**
     * 创建时间
     */
    private String createTime;

/**
     * 更新时间
     */
    private String updateTime;



}

