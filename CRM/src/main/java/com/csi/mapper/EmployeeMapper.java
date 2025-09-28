package com.csi.mapper;

import com.csi.domain.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EmployeeMapper {

    List<Employee> getAllEmployees(Integer superiorId);

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

    /**
     * 更新员工密码
     * @param id 员工ID
     * @param password 新密码
     */
    void updatePassword(@Param("id") Integer id, @Param("password") String password);


    Employee login(String username);

    List<Employee> getAllEmployeesWithConditions(@Param("superiorId") Integer superiorId, @Param("conditions") Employee conditions);

    Employee findLeaderByEmployeeId(Integer id);

}
