package com.csi.service;

import com.csi.domain.ApprovalOrders;

import java.util.List;

public interface ApprovalOrdersService {

    int insert(ApprovalOrders record);//新增销售申请

    int updateStatus(ApprovalOrders record);//更新申请表状态

    List<ApprovalOrders> selectAll();//显示所有申请表

    List<ApprovalOrders> selectPersonal(int approverId);//显示个人申请
}
