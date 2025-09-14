package com.csi.service;

import com.csi.domain.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomer();

    List<Customer> getAssignedCustomer();//获取已分配客户

    List<Customer> getUnAssignedCustomer();//获取未分配客户

    int changeCustomerStatus(Customer customer);

}
