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

    @Autowired
    private CustomerFollowsService customerFollowsService;

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
        List<CustomerFollows> customer = customerFollowsService.getPersonalCustomerByTime("2025-09-14","2025-09-16");

        customer.forEach(System.out::println);
    }

    @Test
    public void test3() {
        Customer customer = new Customer();
        customer.setName("test");
        customer.setPhone("13812345678");
        customer.setSource("test");
        customer.setCreatorId(2002);
        int i = customerService.addCustomer(customer);
        System.out.println(i);
    }

    @Test
    public void test4() {
        Customer customer = new Customer();
        customer.setId(1002);
        customer.setName("test");
        customer.setPhone("13887654321");
        customer.setSource("test");
        customer.setCreatorId(2002);
        int i = customerService.updateCustomer(customer);
        System.out.println(i);
    }
}
