package com.csi.mapper;

import com.csi.domain.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersMapper {

    int addOrders(Orders order);//新建订单

    int updateOrdersStatus(@Param("status") int status,@Param("id") int id);//更新订单状体

    //销售个人查看自己的订单
    List<Orders> selectPersonalOrders(@Param("id") int id);
    
    // 查询所有订单
    List<Orders> selectAllOrders();
    
    // 根据ID查询订单详情
    Orders selectOrderById(@Param("id") int id);
    
    // 根据条件查询订单
    List<Orders> selectOrdersByCondition(@Param("orderNo") String orderNo, 
                                        @Param("status") String status,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
}
