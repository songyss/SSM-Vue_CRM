package com.csi.mapper;

import com.csi.domain.Customer;

import java.util.List;

public interface CustomerMapper {

    List<Customer> getAllCustomer();

    List<Customer> getAssignedCustomer();//获取已分配客户

    List<Customer> getUnAssignedCustomer();//获取未分配客户


}
