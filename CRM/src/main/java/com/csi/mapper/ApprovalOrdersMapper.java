package com.csi.mapper;

import com.csi.domain.ApprovalOrders;
import java.util.List;

public interface ApprovalOrdersMapper {

    int insert(ApprovalOrders record);//新增销售申请

    int updateStatus(ApprovalOrders record);//更新申请表状态

    List<ApprovalOrders> selectAll();//显示所有申请表

    List<ApprovalOrders> selectPersonal(int approverId);//根据审批人ID显示申请表
    
    List<ApprovalOrders> selectByApplicant(int applicantId);//根据申请人ID显示申请表
}

