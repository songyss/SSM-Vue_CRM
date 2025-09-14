package com.csi.service;

import com.csi.domain.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    List<Employee> getEmployees(int id);

}
