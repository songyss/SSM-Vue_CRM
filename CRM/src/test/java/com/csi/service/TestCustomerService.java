package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
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
        /*Customer customer = new Customer();

        customer.setId(1);
        customer.setStatus(2);*/

        int i = customerService.changeCustomerStatus(1,2);

        System.out.println(i);
    }

    @Test
    public void test1() {
        List<Customer> customers = customerService.getPersonalCustomer(2002);

        customers.forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<CustomerFollows> customer = customerService.getPersonalCustomerByTime("2025-09-15");

        customer.forEach(System.out::println);
    }

    @Test
    public void test3() {
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
