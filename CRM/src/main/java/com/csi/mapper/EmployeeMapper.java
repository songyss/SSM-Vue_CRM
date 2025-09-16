package com.csi.mapper;

import com.csi.domain.Employee;

import java.util.List;


public interface EmployeeMapper {

    List<Employee> getAllEmployees();

    List<Employee> getAllOnEmployees();

    List<Employee> getAllLeaveEmployees();

    List<Employee> getEmployees(int id);

    Employee findById(Integer id) ; //根据员工id查询员工信息

    List<Employee> findByDepartment(Integer department) ; //根据部门查询员工信息

    List<Employee> findAll() ; //查询所有员工信息

    Employee findByUsername(String username) ; //根据员工名字查询该员工信息

    void save(Employee employee);
    void update(Employee employee);
    void deleteById(Integer id);

}
