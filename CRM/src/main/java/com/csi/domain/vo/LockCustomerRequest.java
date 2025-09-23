package com.csi.domain.vo;


import lombok.Data;

@Data
public class LockCustomerRequest {
    private Long customerId;   // 客户ID
    private Long employeeId;   // 锁定人ID
    private Integer days = 7;  // 默认锁定天数（可传参）
}