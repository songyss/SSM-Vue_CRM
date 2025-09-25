package com.csi.mapper;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import com.csi.domain.PublicCustomerPool;
import com.csi.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerMapper {

    // 管理员查看全部的客户信息
    List<Customer> getAllCustomer();

    // 销售经理查看除未联系之外的所有客户信息
    List<Customer> getAllSaleCustomer();

    // 获取有意向已分配客户
    List<Customer> getAssignedCustomer();

    // 获取有意向未分配客户
    List<Customer> getUnAssignedCustomer();

    // 获取无意向客户
    List<Customer> getNoIntentionCustomer();

    // 获取信息有误客户
    List<Customer> getInfoIncorrectCustomer();

    // 根据来源筛选客户
    List<Customer> getCustomerBySource(String source);

    // 销售更改客户状态
    int changeCustomerStatus(@Param("id") int id, @Param("status") int status);

    // 根据 ID 查询客户
    Customer findById(int id);

    // 销售获取接受分配的客户
    List<Customer> getPersonalCustomer(int id);

    // 销售新增客户（5个以内）
    int add5Customer(Customer customer);

    // 销售更新用户信息
    int updateCustomer(Customer customer);

    // 市场部录入客户信息
    int addCustomer(Customer customer);

    // 根据联系状态获取客户
    List<Customer> getCustomerBySdrStatus();

    // 更新客户联系状态
    int updateCustomerSdrStatus(@Param("status") int sdrStatus,
                                @Param("notes") String notes,
                                @Param("id") int id);

    // 模糊查询
    List<Customer> getCustomerByCondition(@Param("name") String name,
                                          @Param("phone") String phone,
                                          @Param("source") String source,
                                          @Param("status") Integer status);

    // 查询客户池
    List<Customer> getPoolCustomer();

    // 获取待分配客户（可按状态）
    List<Customer> getUnAssignedCustomerList(@Param("status") Integer status);

    // 分配客户
    void assignCustomers(@Param("employeeId") Integer employeeId,
                         @Param("customerIds") List<Integer> customerIds);

    // 获取未分配客户
    List<Customer> selectUnAssignedList();

    // 根据员工查询客户
    List<CustomerVO> findByEmployee(@Param("employeeId") Long employeeId);

    // 查询所有客户
    List<CustomerVO> findAll();

    // 查询客户详情（含订单和商机）
    CustomerDetailVO findDetail(@Param("customerId") Long customerId);

    // 查询当日跟进客户
    List<CustomerFollows> getPersonalCustomerByTime(String time);

    // 检查手机号是否存在
    Customer checkCustomerPhone(String phone);

    List<CustomerVO> listByEmployee(@Param("employeeId") int employeeId,
                                    @Param("name") String name,
                                    @Param("phone") String phone,
                                    @Param("offset") int offset,
                                    @Param("limit") int limit);


    int countByEmployee(@Param("employeeId") int employeeId,
                        @Param("name") String name,
                        @Param("phone") String phone);

    int updateIsPool(@Param("id") Long id);
    // 锁定客户（更新负责人）
    int updateAssignee(@Param("customerId") Integer customerId,
                       @Param("employeeId") Integer employeeId);

    int updateIsPoolAndClearAssignee(@Param("id") Long id);

    int insertPoolReason(@Param("customerId") Long customerId, @Param("reason") String reason);

    int insertPoolReason(PublicCustomerPool pool);

    List<Customer> getCustomerList(String name, String phone);

    List<Customer> selectCustomerList(@Param("name") String name,
                                      @Param("phone") String phone);

    // 根据客户ID查询订单
    List<OrderVO> getOrdersByCustomerId(@Param("customerId") Long customerId);

    // 根据客户ID查询商机
    List<OpportunityVO> getOpportunitiesByCustomerId(@Param("customerId") Long customerId);

    // 更新客户表，把客户分配给某个员工
    int updateAssigneeAndPool(@Param("customerId") Long customerId,
                              @Param("employeeId") Long employeeId);

    // 更新公共池表，设置锁定人和锁定截止时间
    int lockCustomer(@Param("customerId") Long customerId,
                     @Param("employeeId") Long employeeId,
                     @Param("lockedUntil") LocalDateTime lockedUntil);

    List<CustomerPoolVO> selectCustomerPoolList();
    int lockCustomer(@Param("customerId") Integer customerId,
                     @Param("employeeId") Integer employeeId);

}
