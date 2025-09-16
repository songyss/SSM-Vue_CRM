package com.csi.service;

import com.csi.domain.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersService {

    int addOrders(Orders order);//新建订单

    int updateOrdersStatus(@Param("id") int id , @Param("status") int status);//更新订单状体

    List<Orders> selectPersonalOrders(@Param("id") int id);//查询个人订单
}
