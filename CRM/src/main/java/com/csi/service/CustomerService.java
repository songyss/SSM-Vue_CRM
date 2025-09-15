package com.csi.service;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomer();

    List<Customer> getAllSaleCustomer();//销售经理查看除未联系之外的所有客户信息

    List<Customer> getAssignedCustomer();//获取有意向已分配客户

    List<Customer> getUnAssignedCustomer();//获取有意向未分配客户

    List<Customer> getNoIntentionCustomer();//获取无意向客户

    List<Customer> getInfoIncorrectCustomer();//获取信息有误客户

    List<Customer> getCustomerBySource(String source);//根据来源筛选客户

    int changeCustomerStatus(@Param("id") int id, @Param("status") int status);

    List<Customer> getPersonalCustomer(int id);//销售获取接受分配

    List<CustomerFollows> getPersonalCustomerByTime(String time);//根据跟进时间筛选客户


    int addCustomer(Customer customer); //客户端录入客户信息

}
