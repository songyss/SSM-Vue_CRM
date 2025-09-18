package com.csi.service.impl;

import com.csi.domain.Customer;
import com.csi.domain.Employee;
import com.csi.domain.OperateLogs;
import com.csi.mapper.CustomerMapper;
import com.csi.mapper.EmployeeMapper;
import com.csi.mapper.OperationLogsMapper;
import com.csi.service.MoveCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MoveCustomerServiceImpl implements MoveCustomerService {

    @Autowired
    private OperationLogsMapper operationLogsMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class) // 事务控制，符合需求“数据准确性”
    public void batchPush(List<Integer> customerIds, int targetManagerId, int operatorId,String ip) {
        // 1. 校验1：客户ID集合非空
        if (customerIds == null || customerIds.isEmpty()) {
            throw new RuntimeException("请选择至少1条客户");
        }

        // 2. 校验2：销售部经理ID合法性（需属于销售部且角色为经理，匹配需求2.4.1）
        Employee salesManager = employeeMapper.findById(targetManagerId);
        if (salesManager == null || 2 != salesManager.getDepartment() || 4 != salesManager.getRole()) {
            throw new RuntimeException("目标负责人不是销售部经理，无法推送");
        }

        // 3. 校验3：操作人是市场部经理（匹配需求2.2.1权限控制）
        Employee operator = employeeMapper.findById(operatorId);
        if (operator == null || 1 != operator.getDepartment() || 2 != operator.getRole()) {
            throw new RuntimeException("仅市场部经理可执行推送操作");
        }

        // 4. 批量更新客户负责人ID（核心：实现“推送”，匹配需求中“assignee_id变更”逻辑）
        for (int customerId : customerIds) {
            // 4.1 校验客户状态：仅sdr_status=3（有意向）可推送
            Customer customer = customerMapper.findById(customerId);
            if (customer == null) {
                throw new RuntimeException("客户ID：" + customerId + "不存在");
            }
            if (customer.getSdrStatus() != 3) {
                throw new RuntimeException("客户ID：" + customerId + "状态非“有意向”，无法推送");
            }

            // 4.2 保存旧值（用于操作日志）
            int oldAssigneeId = customer.getAssigneeId();

            // 4.3 更新客户负责人ID为销售部经理ID，同步更新时间
            customer.setAssigneeId(targetManagerId);
            customerMapper.updateCustomer(customer);

            // 5. 生成操作日志（匹配需求2.1.2操作日志记录规则）
            OperateLogs log = new OperateLogs();
            log.setOperatorId(operatorId);
            log.setOperation("批量推送客户至销售部经理");
            log.setTargetType("customer"); // 操作对象类型：客户
            log.setTargetId(customerId);
            log.setIpAddress(ip); // 实际需从请求中获取真实IP
            log.setOldValue("assignee_id:" + oldAssigneeId); // 旧负责人ID
            log.setNewValue("assignee_id:" + targetManagerId); // 新负责人ID
            operationLogsMapper.insert(log);
        }
    }

}
