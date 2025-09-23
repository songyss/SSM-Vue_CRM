package com.csi.service;

import com.csi.domain.Orders;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersService {

    int addOrders(Orders order);//新建订单

    int updateOrdersStatus(Orders order);//更新订单状体

    List<Orders> selectPersonalOrders(@Param("id") int id);//查询个人订单
    
    // 查询所有订单
    List<Orders> selectAllOrders();//查询所有订单
    
    // 分页查询订单列表
    PageInfo<Orders> selectOrdersByPage(int currentPage, int pageSize);
    
    // 根据条件分页查询订单
    PageInfo<Orders> selectOrdersByCondition(String orderNo, String status, 
                                            String startDate, String endDate, 
                                            int currentPage, int pageSize);
}
