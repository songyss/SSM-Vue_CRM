package com.csi.mapper;

import com.csi.domain.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper {

    int addOrders(Orders order);//新建订单

    int updateOrdersStatus(@Param("id") int id , @Param("status") int status);//更新订单状体

    //销售个人查看自己的订单
    List<Orders> selectPersonalOrders(@Param("id") int id);
}
