package com.csi.mapper;

import com.csi.domain.Orders;

public interface OrdersMapper {
    int saveOrders(Orders orders);
    Orders getOrdersByStatus();
    int updateOrders(int id,int orderStatus);
}
