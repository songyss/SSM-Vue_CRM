package com.csi.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class SalesEmergency implements Serializable {
    private Integer id; // 主键ID
    private String emergencyNo; // 突发编号
    private String type; // 类型：DISCOUNT-折扣、PAYMENT-付款、DELIVERY-交付、CONTRACT-合同
    private Integer salespersonId; // 销售人员ID
    private Integer customerId; // 客户ID
    private Integer relatedId; // 关联业务ID
    private String title; // 突发状况标题
    private String description; // 详细描述
    private String customerRequest; // 客户要求
    private Integer urgency; // 紧急程度：1-特急、2-紧急、3-普通、4-低
    private String solutionContent; // 解决方案
    private Integer proposedBy; // 提议人ID（逻辑外键→employees.id）
    private Double costImpact; // 成本影响金额
    private String riskLevel; // 风险等级（高/中/低）
    private String status; // 状态：DRAFT-草稿、PENDING-待审批、APPROVED-已批准、REJECTED-已拒绝、SOLVED-已解决
    private Integer currentHandlerId; // 当前处理人ID（逻辑外键→employees.id）
    private Date occurTime; // 发生时间
    private Date deadline; // 处理截止时间（null=无强制时限）
    private Date createdTime; // 创建时间
    private Date updatedTime; // 最后更新时间
    private Date solvedTime;  // 解决时间
    private Integer isDeleted; // 删除标志：0-未删除、1-已删除
}
