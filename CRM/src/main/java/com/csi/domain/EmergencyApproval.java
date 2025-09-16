package com.csi.domain;

import java.io.Serializable;
import java.sql.Date;

public class EmergencyApproval implements Serializable {

    private Integer id; // 主键ID
    private Integer emergencyId; // 突发状况ID
    private Integer approverId; // 审批人ID
    private String approverRole; // 审批人角色
    private String approvalResult;// 审批结果：PASS-通过、REJECT-拒绝、RETURN-退回
    private String approvalComment; // 审批意见/备注
    private Date createdTime; // 审批记录创建时间
}
