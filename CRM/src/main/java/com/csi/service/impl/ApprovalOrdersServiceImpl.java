package com.csi.service.impl;

import com.csi.domain.ApprovalOrders;
import com.csi.mapper.ApprovalOrdersMapper;
import com.csi.service.ApprovalOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ApprovalOrdersServiceImpl implements ApprovalOrdersService {
    @Autowired
    private ApprovalOrdersMapper approvalOrdersMapper;

    @Override
    public int insert(ApprovalOrders record) {
        int insert = approvalOrdersMapper.insert(record);
        return insert;
    }

    @Override
    public int updateStatus(ApprovalOrders record) {
        int i = approvalOrdersMapper.updateStatus(record);
        return i;
    }

    @Override
    public List<ApprovalOrders> selectAll() {
        List<ApprovalOrders> approvalOrders = approvalOrdersMapper.selectAll();
        return approvalOrders;
    }

    @Override
    public List<ApprovalOrders> selectByApproverId(int approverId) {
        List<ApprovalOrders> approvalOrders = approvalOrdersMapper.selectPersonal(approverId);
        return approvalOrders;
    }
    
    @Override
    public List<ApprovalOrders> selectByApplicantId(int applicantId) {
        List<ApprovalOrders> approvalOrders = approvalOrdersMapper.selectByApplicant(applicantId);
        return approvalOrders;
    }
}
