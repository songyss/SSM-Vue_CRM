package com.csi.mapper;

import com.csi.domain.Customer;

import java.util.List;

public interface CustomerMapper {

    List<Customer> getAllCustomer();

    List<Customer> getAssignedCustomer();//获取已分配客户

    List<Customer> getUnAssignedCustomer();//获取未分配客户

    int changeCustomerStatus(Customer customer);//销售更改客户状态

    List<Customer> getPersonalCustomer(int id);//销售获取接受分配
}
