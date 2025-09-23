package com.csi.service.impl;

import com.csi.domain.Customer;
import com.csi.domain.PublicCustomerPool;
import com.csi.domain.vo.*;
import com.csi.mapper.CustomerMapper;
import com.csi.mapper.EmployeeMapper;
import com.csi.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    private EmployeeMapper employeeMapper;

    // ================== 客户基础查询 ==================
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomer() {
        return customerMapper.getAllCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllSaleCustomer() {
        return customerMapper.getAllSaleCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAssignedCustomer() {
        return customerMapper.getAssignedCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getUnAssignedCustomer() {
        return customerMapper.getUnAssignedCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getNoIntentionCustomer() {
        return customerMapper.getNoIntentionCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getInfoIncorrectCustomer() {
        return customerMapper.getInfoIncorrectCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomerBySource(String source) {
        return customerMapper.getCustomerBySource(source);
    }

    @Override
    public int changeCustomerStatus(@Param("id") int id, @Param("status") int status) {
        return customerMapper.changeCustomerStatus(id, status);
    }

    @Override
    public Customer findById(int id) {
        return customerMapper.findById(id);
    }

    // ================== 客户新增 ==================
    @Override
    public int addCustomer(Customer customer) {
        Customer result = customerMapper.checkCustomerPhone(customer.getPhone());
        if (result != null) {
            return 0;
        } else {
            customer.setIsPool(0);
            customer.setStatus(1);
            // 前提：扫码自动绑定此员工id
            customer.setCreatorId(2002);
            // 根据市场部员工id找到市场部经理id
            customer.setAssigneeId(employeeMapper.findLeaderIdByEmployeeId(customer.getCreatorId()));

            customerMapper.addCustomer(customer);
            return 1;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PageInfo<Customer> getPersonalCustomerByPage(Integer employeeId, int page, int size) {
        // 开启分页
        PageHelper.startPage(page, size);
        // 查询数据，使用assigneeId作为筛选条件
        List<Customer> customers = customerMapper.getPersonalCustomer(employeeId);
        // 封装分页结果
        return new PageInfo<>(customers);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getPersonalCustomer(int id) {
        return customerMapper.getPersonalCustomer(id);
    }

    @Override
    public int add5Customer(Customer customer) {
        return customerMapper.addCustomer(customer);
    }

    // ================== 客户更新 ==================
    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public List<Customer> getCustomerBySdrStatus() {
        return customerMapper.getCustomerBySdrStatus();
    }

    @Override
    public int updateCustomerSdrStatus(Customer customer) {
        Integer sdrStatus = customer.getSdrStatus();
        String sdrNotes = customer.getSdrNotes();

        if (sdrStatus == null) {
            sdrStatus = 1; // 默认未联系
        }
        if (sdrNotes == null) {
            sdrNotes = "";
        }

        return customerMapper.updateCustomerSdrStatus(sdrStatus, sdrNotes, customer.getId());
    }

    // ================== 条件查询 / 客户池 ==================
    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomerByCondition(String name, String phone, String source, Integer status) {
        return customerMapper.getCustomerByCondition(name, phone, source, status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getPoolCustomer() {
        return customerMapper.getPoolCustomer();
    }

    @Override
    public List<Customer> getUnAssignedCustomerList(Integer status) {
        return customerMapper.getUnAssignedCustomerList(status);
    }

    @Override
    public void assignCustomers(Integer employeeId, List<Integer> customerIds) {
        customerMapper.assignCustomers(employeeId, customerIds);
    }

    @Override
    public List<Customer> getUnAssignedList() {
        return customerMapper.selectUnAssignedList();
    }

    // ================== 分页查询 ==================
    @Override
    public PageInfo<CustomerVO> getCustomersByEmployee(Long employeeId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(customerMapper.findByEmployee(employeeId));
    }

    @Override
    public PageInfo<CustomerVO> getAllCustomers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(customerMapper.findAll());
    }

    // ================== 客户详情 ==================

    @Override
    public PageInfo<CustomerVO> listByEmployee(int employeeId, int pageNum, int pageSize, String name, String phone) {
        int offset = (pageNum - 1) * pageSize;
        List<CustomerVO> customers = customerMapper.listByEmployee(employeeId, name, phone, offset, pageSize);
        return new PageInfo<>(customers);
    }

    @Override
    public int addToPool(Long id, String reason) {
        // 1. 置空客户负责人 & 标记进入客户池
        customerMapper.updateIsPoolAndClearAssignee(id);

        // 2. 保存放入客户池的原因
        customerMapper.insertPoolReason(id, reason);

        return 1;
    }


    @Override
    public boolean lockCustomer(Integer customerId, Integer employeeId) {
        int rows = customerMapper.updateAssignee(customerId, employeeId);
        return rows > 0;
    }



    @Override
    public int addToPool(Long customerId, String reason, Long operatorId) {
        // 1. 置空 assigneeId
        customerMapper.updateIsPoolAndClearAssignee(customerId);

        // 2. 插入原因
        PublicCustomerPool pool = new PublicCustomerPool();
        pool.setCustomerId(customerId);
        pool.setReason(reason);
        pool.setOperatorId(operatorId);
        pool.setCreateTime(LocalDateTime.now().toString());

        return customerMapper.insertPoolReason(pool);
    }

    @Override
    public List<Customer> getCustomerList(String name, String phone) {
        return customerMapper.selectCustomerList(name, phone);
    }

    @Override
    public CustomerDetailVO getCustomerDetail(Long customerId) {
        CustomerDetailVO detail = customerMapper.findDetail(customerId);
        if (detail != null) {
            List<OrderVO> orders = customerMapper.getOrdersByCustomerId(customerId);
            List<OpportunityVO> opps = customerMapper.getOpportunitiesByCustomerId(customerId);
            detail.setOrders(orders);
            detail.setOpportunities(opps);
        }
        return detail;
    }
    @Override
    @Transactional
    public void lockCustomer(LockCustomerRequest request) {
        Long customerId = request.getCustomerId();
        Long employeeId = request.getEmployeeId();
        Integer days = request.getDays();

        // 1. 更新客户表，分配给员工
        customerMapper.updateAssigneeAndPool(customerId, employeeId);

        // 2. 更新公共池表，设置锁定人和截止时间
        LocalDateTime lockedUntil = LocalDateTime.now().plusDays(days);
        customerMapper.lockCustomer(customerId, employeeId, lockedUntil);
    }

    @Override
    public List<CustomerPoolVO> getCustomerPoolList() {
        return customerMapper.selectCustomerPoolList();
    }

}
