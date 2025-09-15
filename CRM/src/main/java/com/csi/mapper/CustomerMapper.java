package com.csi.mapper;

import com.csi.domain.Customer;

import java.util.List;

public interface CustomerMapper {

    List<Customer> getAllCustomer();

    List<Customer> getAssignedCustomer();//获取已分配客户

    List<Customer> getUnAssignedCustomer();//获取未分配客户

    int changeCustomerStatus(Customer customer);    //销售更改客户状态

    Customer checkCustomerPhone(String phone);  //检验客户手机号唯一性

    int addCustomer (Customer customer);  //市场部录入客户信息
}
