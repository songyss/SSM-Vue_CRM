package com.csi.service;

import com.csi.domain.Orders;

public interface OrdersService {
    int saveOrders(Orders orders);
    Orders getOrdersByStatus();
    int updateOrders(int id, int status);
}
