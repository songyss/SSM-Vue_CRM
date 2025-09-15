package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestCustomerService {

    @Autowired
    private CustomerService customerService;

    @Test
    public void test() {
        Customer customer = new Customer();

        customer.setId(1);
        customer.setStatus(2);

        int i = customerService.changeCustomerStatus(customer);

        System.out.println(i);
    }

    @Test
    public void test1() {
        Customer customer = new Customer();
        customer.setId(11);
        customer.setName("刘里");
        customer.setPhone("12345678909");
        customer.setBorndate("2001-09-09");
        customer.setCompany("公司");
        customer.setSource("线下活动");
        customer.setStatus(1);
        customer.setCreatorId(1);



        customer.setAssigneeId(888);
        customerService.addCustomer(customer);
    }
}
