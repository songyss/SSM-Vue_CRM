package com.csi.service.impl;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import com.csi.domain.Opportunities;
import com.csi.mapper.CustomerMapper;
import com.csi.service.CustomerService;
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
    public int addCustomer(Customer customer){
        Customer result=customerMapper.checkCustomerPhone(customer.getPhone());
        if(result!=null){
            return 0;
        }else {

            int i = customerMapper.addCustomer(customer);
            return i;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getPersonalCustomer(int id) {
        List<Customer> customers = customerMapper.getPersonalCustomer(id);

        return customers;
    }

    @Override
    public int addCustomer(Customer customer) {
        int i = customerMapper.addCustomer(customer);
        return i;
    }

    @Override
    public int updateCustomer(Customer customer) {
        int i = customerMapper.updateCustomer(customer);
        return i;
    }

}
