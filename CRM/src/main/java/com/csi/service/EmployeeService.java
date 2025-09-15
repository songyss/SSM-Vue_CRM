package com.csi.service;

import com.csi.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getAllOnEmployees();

    List<Employee> getAllLeaveEmployees();

    List<Employee> getEmployees(int id);

}
