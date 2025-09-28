package com.csi.service;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
import com.csi.domain.Opportunities;
import com.csi.domain.vo.CustomerDetailVO;
import com.csi.domain.vo.CustomerPoolVO;
import com.csi.domain.vo.CustomerVO;
import com.csi.domain.vo.LockCustomerRequest;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerService {

    // ================== 客户基础查询 ==================
    List<Customer> getAllCustomer();

    List<Customer> getAllSaleCustomer(); // 销售经理查看除未联系之外的所有客户信息

    List<Customer> getAssignedCustomer(); // 获取有意向已分配客户

    List<Customer> getUnAssignedCustomer(); // 获取有意向未分配客户

    List<Customer> getNoIntentionCustomer(); // 获取无意向客户

    List<Customer> getInfoIncorrectCustomer(); // 获取信息有误客户

    List<Customer> getCustomerBySource(String source); // 根据来源筛选客户

    List<Customer> getCustomerBySdrStatus(); // 根据联系状态获取客户信息

    List<Customer> getCustomerByCondition(String name, String phone, String source, Integer status); // 条件查询

    List<Customer> getPoolCustomer(); // 客户池

    PageInfo<Customer> getPersonalCustomerByPage(Customer customer);

    List<Customer> getUnAssignedCustomerList(Integer status); // 获取待分配客户（可按状态过滤）

    List<Customer> getUnAssignedList(); // 备用：未分配客户

    // ================== 客户状态更新 ==================
    int changeCustomerStatus(@Param("id") int id, @Param("status") int status);

    int updateCustomer(Customer customer); // 更新用户信息（标准接口）

    int updateCustomerSdrStatus(Customer customer); // 更新客户联系状态

    // ================== 客户新增 ==================
    int add5Customer(Customer customer); // 销售新增客户（老接口）
    int addCustomer(Customer customer);  // 新增客户（标准接口）

    // ================== 客户分配 ==================
    void assignCustomers(Integer employeeId, List<Integer> customerIds);

    // ================== 分页查询 ==================
    PageInfo<CustomerVO> getCustomersByEmployee(Long employeeId, int pageNum, int pageSize);
    PageInfo<CustomerVO> getAllCustomers(int pageNum, int pageSize);

    // ================== 客户详情 ==================
    Customer findById(int id);
    CustomerDetailVO getCustomerDetail(Long customerId); // 客户详情（带订单/商机）

    PageInfo<CustomerVO> listByEmployee(int employeeId, int pageNum, int pageSize, String name, String phone);

    int addToPool(Long id, String reason);

    boolean lockCustomer(Integer customerId, Integer employeeId);

    int addToPool(Long customerId, String reason, Long operatorId);

    List<Customer> getCustomerList(String name, String phone);

    void lockCustomer(LockCustomerRequest request);

    List<CustomerPoolVO> getCustomerPoolList();


}
