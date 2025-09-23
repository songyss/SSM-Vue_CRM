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

    List<Customer> getPersonalCustomer(int assigneeId);//销售获取接受分配

    int add5Customer(Customer customer);//销售新增客户

    int updateCustomer(Customer customer);//销售更新用户信息

    Customer findById(int id);

    List<CustomerFollows> getPersonalCustomerByTime(String time);//根据跟进时间筛选客户
    int changeCustomerStatus(Customer customer);    //销售更改客户状态

    Customer checkCustomerPhone(String phone);  //检验客户手机号唯一性

    int addCustomer (Customer customer);  //市场部录入客户信息

    List<Customer> getCustomerBySdrStatus();//根据联系状态获取客户

    int updateCustomerSdrStatus(@Param("status") int sdrStatus,@Param("notes")String notes,@Param("id") int id);//更新客户联系状态

    //模糊查询
    List<Customer> getCustomerByCondition(@Param("name") String name,
                                          @Param("phone") String phone, @Param("source") String source,
                                          @Param("status") Integer status);
    List<Customer> getPoolCustomer();

    List<Customer> getUnAssignedCustomerList(@Param("status") Integer status);

    void assignCustomers(@Param("employeeId") Integer employeeId,
                         @Param("customerIds") List<Integer> customerIds);

    List<Customer> selectUnAssignedList();

}
