package com.csi.service.impl;

import com.csi.domain.Employee;
import com.csi.domain.SalesEmergency;
import com.csi.mapper.EmployeeMapper;
import com.csi.mapper.SalesEmergencyMapper;
import com.csi.service.SalesEmergencyService;
import com.csi.util.ValidationUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SalesEmergencyServiceImpl implements SalesEmergencyService {
    @Autowired
    private SalesEmergencyMapper salesEmergencyMapper ;

    @Autowired
    private EmployeeMapper employeeService;


    //审批验证方法
    private void validateForApproval(SalesEmergency salesEmergency , Integer managerId){
        if (salesEmergency == null){
            throw new RuntimeException("记录不存在");
        }
        if(!"PENDING".equals(salesEmergency.getStatus())){
            throw new RuntimeException(("只有待审批的记录才能被审批"));
        }
        if(!salesEmergency.getCurrentHandlerId().equals(managerId)){
            throw new RuntimeException("无权审批此纪录") ;
        }
        ValidationUtils.validateFixed(salesEmergency);
    }

    //创建查询参数Map
    private Map<String , Object> createParams(Integer salespersonId , String status){
        Map<String,Object> params = new HashMap<>();
        params.put("salespersonId",salespersonId);
        params.put("status",status);
        return params;
    }



    @Override
    public void createEmergency(SalesEmergency emergency, Integer empId) {
        ValidationUtils.validateFixed(emergency);
        emergency.setSalespersonId(empId);
        emergency.setStatus("DRAFT");
        emergency.setCurrentHandlerId(empId);
        emergency.setProposedBy(empId);
        salesEmergencyMapper.insert(emergency);
    }

    @Override
    public void submitForApproval(Integer id, Integer empId) {
        SalesEmergency salesEmergency = salesEmergencyMapper.selectById(id);
        if (salesEmergency == null) {
            throw new RuntimeException("记录不存在");
        }
        if (!salesEmergency.getSalespersonId().equals(empId)) {
            throw new RuntimeException("只能提交自己的记录");
        }
        if (!"DRAFT".equals(salesEmergency.getStatus())) {
            throw new RuntimeException("只能提交草稿状态的记录");
        }

        // 查找直属上级
        Employee leader = employeeService.findLeaderByEmployeeId(empId);
        if (leader == null) {
            throw new RuntimeException("未找到直属上级");
        }

        // 更新状态和处理人
        salesEmergencyMapper.updateStatusAndHandler(id, "PENDING", leader.getId());
    }

    @Override
    public void approve(Integer id, Integer managerId) {
        SalesEmergency salesEmergency = salesEmergencyMapper.selectById(id) ;
        validateForApproval(salesEmergency,managerId);
        salesEmergencyMapper.updateStatusAndHandler(id,"APPROVED",managerId);
    }

    @Override
    public void reject(Integer id, Integer managerId) {
        SalesEmergency salesEmergency = salesEmergencyMapper.selectById(id) ;
        validateForApproval(salesEmergency,managerId);
        salesEmergencyMapper.updateStatusAndHandler(id,"REJECTED",managerId);
    }

    @Override
    public void markAsSolved(Integer id, Integer empId, String solutionDescription) {
        SalesEmergency salesEmergency = salesEmergencyMapper.selectById(id);
        if (salesEmergency == null) {
            throw new RuntimeException("记录不存在");
        }

        // 检查是否是自己的记录
        if (!salesEmergency.getSalespersonId().equals(empId)) {
            throw new RuntimeException("只能标记自己的记录为已解决");
        }

        // 检查当前状态
        if (!"APPROVED".equals(salesEmergency.getStatus())) {
            throw new RuntimeException("只有已批准状态的记录才能标记为已解决");
        }

        // 更新状态为已解决，并记录解决描述和时间
        salesEmergencyMapper.updateStatusToSolved(id, "SOLVED", solutionDescription, new Date());
    }

    @Override
    public int updateById(SalesEmergency emergency) {
        return salesEmergencyMapper.updateById(emergency);
    }

    @Override
    public List<SalesEmergency> listByCreator(Integer empId) {
        return salesEmergencyMapper.selectBySalespersonId(empId);
    }

    @Override
    public List<SalesEmergency> listPending() {
        return salesEmergencyMapper.selectByStatus("PENDING");
    }

    @Override
    public SalesEmergency getById(Integer id) {
        return salesEmergencyMapper.selectById(id);
    }

    @Override
    public List<SalesEmergency> listByHandler(Integer handlerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("handlerId", handlerId);
        return salesEmergencyMapper.selectByCondition(params);
    }

    @Override
    public List<SalesEmergency> listByCondition(Map<String, Object> params) {
        return salesEmergencyMapper.selectByCondition(params);
    }

    @Override
    public Map<String, Integer> getStatsBySalesperson(Integer salespersonId) {
        Map<String,Integer> stats = new HashMap<>();
        stats.put("draft",salesEmergencyMapper.countByStatus(createParams(salespersonId,"DRAFT")));
        stats.put("pending",salesEmergencyMapper.countByStatus(createParams(salespersonId,"PENDING")));
        stats.put("approved",salesEmergencyMapper.countByStatus(createParams(salespersonId,"APPROVED")));
        stats.put("rejected",salesEmergencyMapper.countByStatus(createParams(salespersonId,"REJECTED")));
        return stats ;
    }

    @Override
    public List<SalesEmergency> getPendingEmergenciesByManager(Integer managerId) {
        return salesEmergencyMapper.selectPendingByManagerId(managerId);
    }


}
