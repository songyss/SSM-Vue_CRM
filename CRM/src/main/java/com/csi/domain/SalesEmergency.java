package com.csi.domain;

import lombok.Data;

@Data
public class SalesEmergency {
    private Integer id;
    private String emergencyNo;
    private String type;
    private Integer salespersonId;
    private Integer customerId;
    private Integer relatedId;
    private String title;
    private String description;
    private String customerRequest;
    private Integer urgency;
    private String solutionContent;
    private Integer proposedBy;
    private Double costImpact;
    private String riskLevel;
    private String status;
    private Integer currentHandlerId;
    private String occurTime;
    private String deadline;
    private String createdTime;
    private String updatedTime;
    private String solvedTime;
    private Boolean isDeleted;
    private String submittedToName;

    // ===== 扩展字段 =====
    private String salespersonName;    // e1.name
    private String customerName;       // c.name
    private String currentHandlerName; // e2.name
    private String proposedByName;     // e3.name
    private String approvalComment;    // 审批意见
}
