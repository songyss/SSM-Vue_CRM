package com.csi.service.impl;

import com.csi.domain.Customer;
import com.csi.mapper.CustomerMapper;
import com.csi.service.CustomerService;
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
    public List<Customer> getAssignedCustomer() {
        return customerMapper.getAssignedCustomer();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getUnAssignedCustomer() {
        return customerMapper.getUnAssignedCustomer();
    }

    @Override
    public int changeCustomerStatus(Customer customer) {
        int i = customerMapper.changeCustomerStatus(customer);
        return i;
    }

    @Override
    public List<Customer> getPersonalCustomer(int id) {
        List<Customer> customers = customerMapper.getPersonalCustomer(id);

        return customers;
    }
}
