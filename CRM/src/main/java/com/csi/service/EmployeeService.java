package com.csi.service;

import com.csi.domain.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees(Integer superiorId);
    /**
     * 重置员工密码
     * @param id 员工ID
     * @param password 新密码
     */
    void resetPassword(Integer id, String password);

    Employee findLeaderByEmployeeId(Integer id);

    List<Employee> getAllOnEmployees();

    List<Employee> getAllLeaveEmployees();

    List<Employee> getEmployees(int id);

    //根据员工id找到经理id
    int findLeaderIdByEmployeeId(int id);

    Employee findById(Integer id) ; //根据员工id查询员工信息

    List<Employee> findByDepartment(Integer department) ; //根据部门查询员工信息

    List<Employee> findAll() ; //查询所有员工信息

    Employee findByUsername(String username) ; //根据员工名字查询该员工信息

    void save(Employee employee);
    void update(Employee employee);
    void deleteById(Integer id);

    Employee login(@Param("username") String username, @Param("password") String password);



    //带条件查询
    List<Employee> getAllEmployeesWithConditions(Integer superiorId, Employee conditions);
}
