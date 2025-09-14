package com.csi.mapper;

import com.csi.domain.Employee;

import java.util.List;


public interface EmployeeMapper {

    List<Employee> getAllEmployees();

    List<Employee> getEmployees(int id);

}
