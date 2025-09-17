package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.Orders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestOrdersService {

    @Autowired
    private OrdersService ordersService;

    @Test
    public void test() {

//        System.out.println(111);
        Orders orders = new Orders();

        orders.setOrderNumber("1001");
        orders.setOpportunityId(1);
        orders.setCustomerId(1);
        orders.setTotalAmount(200000);
        orders.setSignedDate("2025-09-16");

        int i = ordersService.addOrders(orders);
        System.out.println(i);
    }

    @Test
    public void test2() {
        Orders orders = new Orders();
        orders.setId(1);
        orders.setOrderStatus(1);
        int i = ordersService.updateOrdersStatus(orders);
        System.out.println(i);
    }
}
