package com.csi.mapper;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import com.csi.domain.Opportunities;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerMapper {

    List<Customer> getAllCustomer();//管理员查看全部的客户信息

    List<Customer> getAllSaleCustomer();//销售经理查看除未联系之外的所有客户信息

    List<Customer> getAssignedCustomer();//获取有意向已分配客户

    List<Customer> getUnAssignedCustomer();//获取有意向未分配客户

    List<Customer> getNoIntentionCustomer();//获取无意向客户

    List<Customer> getInfoIncorrectCustomer();//获取信息有误客户

    List<Customer> getCustomerBySource(String source);//根据来源筛选客户

    int changeCustomerStatus(@Param("id") int id,@Param("status") int status);//销售更改客户状态

    List<Customer> getPersonalCustomer(int id);//销售获取接受分配

    int add5Customer(Customer customer);//销售新增客户

    int updateCustomer(Customer customer);//销售更新用户信息

    List<CustomerFollows> getPersonalCustomerByTime(String time);//根据跟进时间筛选客户
    int changeCustomerStatus(Customer customer);    //销售更改客户状态

    Customer checkCustomerPhone(String phone);  //检验客户手机号唯一性

    int addCustomer (Customer customer);  //市场部录入客户信息


}
