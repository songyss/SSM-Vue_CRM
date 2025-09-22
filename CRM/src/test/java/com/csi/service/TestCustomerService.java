package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestCustomerService {

    @Autowired
    private CustomerService customerService;

    @Test
    public void test() {
        List<Customer> customers = customerService.getPersonalCustomer(1);

        System.out.println(customers);
    }
}
