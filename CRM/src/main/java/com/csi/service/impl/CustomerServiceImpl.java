package com.csi.service.impl;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import com.csi.mapper.CustomerMapper;
import com.csi.mapper.EmployeeMapper;
import com.csi.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

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
        int i = customerMapper.changeCustomerStatus(id, status);
        return i;
    }

    @Override
    public Customer findById(int id) {
        return customerMapper.findById(id);
    }

    @Override
    public int addCustomer(Customer customer){
        Customer result=customerMapper.checkCustomerPhone(customer.getPhone());
        if(result!=null){
            return 0;
        }else {
            customer.setIsPool(0);
            customer.setStatus(1);
            //前提是扫码自动绑定此员工id
            customer.setCreatorId(2002);
            //根据市场部员工id找到市场部经理id
            customer.setAssigneeId(employeeMapper.findLeaderIdByEmployeeId(customer.getCreatorId()));

            customerMapper.addCustomer(customer);

            return 1;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getPersonalCustomer(int id) {
        List<Customer> customers = customerMapper.getPersonalCustomer(id);
        return customers;
    }
    
    /**
     * 分页查询个人客户
     * @param employeeId 员工ID
     * @param page 页码
     * @param size 每页大小
     * @return 分页结果
     */
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

    /*@Override
    public List<CustomerFollows> getPersonalCustomerByTime(String time) {
        List<CustomerFollows> customer = customerMapper.getPersonalCustomerByTime(time);
        return customer;
    }*/

    @Override
    public int add5Customer(Customer customer) {
        int i = customerMapper.addCustomer(customer);
        return i;
    }

    @Override
    public int updateCustomer(Customer customer) {
        int i = customerMapper.updateCustomer(customer);
        return i;
    }


    @Override
    public List<Customer> getCustomerBySdrStatus() {
        return customerMapper.getCustomerBySdrStatus();
    }

    @Override
    public int updateCustomerSdrStatus(Customer customer) {
        // 修复空指针异常：添加空值检查
        Integer sdrStatus = customer.getSdrStatus();
        String sdrNotes = customer.getSdrNotes();

        if (sdrStatus == null) {
            sdrStatus = 1; // 默认设为1（未联系）
        }
        if (sdrNotes == null) {
            sdrNotes = ""; // 避免空字符串
        }

        // 确保参数顺序和命名与Mapper一致
        int i = customerMapper.updateCustomerSdrStatus(sdrStatus, sdrNotes, customer.getId());
        return i;
    }
    // 添加客户查询功能
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
}
