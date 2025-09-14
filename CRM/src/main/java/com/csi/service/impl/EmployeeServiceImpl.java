package com.csi.service.impl;

import com.csi.domain.Employee;
import com.csi.mapper.EmployeeMapper;
import com.csi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeMapper.getAllEmployees();
    }

    @Override
    public List<Employee> getEmployees(int id) {
        return employeeMapper.getEmployees(id);
    }
}
