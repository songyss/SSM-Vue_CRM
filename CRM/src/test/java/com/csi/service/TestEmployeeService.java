package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestEmployeeService {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getAllEmployeesTest(){
        for (Employee allEmployee : employeeService.getAllEmployees()) {
            System.out.println(allEmployee);
        }

    }



}
