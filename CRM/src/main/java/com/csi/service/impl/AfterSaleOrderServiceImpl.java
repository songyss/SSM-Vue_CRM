package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.domain.Orders;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.service.AfterSaleOrderService;
import com.csi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AfterSaleOrderServiceImpl implements AfterSaleOrderService {
    @Autowired
    private AfterSaleOrderMapper afterSaleOrderMapper;
    @Autowired
    private OrdersService ordersService;
    /**
     * 根据售后状态查询售后订单
     * */
    @Override
    public List<AfterSaleOrder> getAfterSaleOrderByStatus() {
        return afterSaleOrderMapper.getAfterSaleOrderByStatus();
    }
    /**
     * 根据售后订单编号查询售后订单
     * */

    @Override
    public List<AfterSaleOrder> getAfterSaleOrderByOrderNumber(String orderNumber) {
        return afterSaleOrderMapper.getAfterSaleOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<AfterSaleOrder> getAfterSaleOrderByDateRange(String startDate, String endDate) {
        return afterSaleOrderMapper.getAfterSaleOrderByDateRange(startDate, endDate);
    }

    /**
     * 修改售后订单状态
     * 根据修改的状态值修改订单状态
     * */
    @Override
    public int updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder) {
        int i = afterSaleOrderMapper.updateAfterSaleOrder(afterSaleOrder.getAfterSaleStatus(), afterSaleOrder.getOrderNumber());
        if (afterSaleOrder.getAfterSaleStatus()==2){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(8);
            // 先根据订单号查询订单ID，再更新订单状态
            Orders existingOrder = ordersService.getOrdersByOrderNumber(afterSaleOrder.getOrderNumber());
            if (existingOrder != null) {
                orders.setId(existingOrder.getId());
                ordersService.updateOrdersStatus(orders);
            }
        }else if (afterSaleOrder.getAfterSaleStatus()==3){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(4);
            // 先根据订单号查询订单ID，再更新订单状态
            Orders existingOrder = ordersService.getOrdersByOrderNumber(afterSaleOrder.getOrderNumber());
            if (existingOrder != null) {
                orders.setId(existingOrder.getId());
                ordersService.updateOrdersStatus(orders);
            }
        }else if (afterSaleOrder.getAfterSaleStatus()==4){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(7);
            // 先根据订单号查询订单ID，再更新订单状态
            Orders existingOrder = ordersService.getOrdersByOrderNumber(afterSaleOrder.getOrderNumber());
            if (existingOrder != null) {
                orders.setId(existingOrder.getId());
                ordersService.updateOrdersStatus(orders);
            }
        }else if (afterSaleOrder.getAfterSaleStatus()==5){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(6);
            // 先根据订单号查询订单ID，再更新订单状态
            Orders existingOrder = ordersService.getOrdersByOrderNumber(afterSaleOrder.getOrderNumber());
            if (existingOrder != null) {
                orders.setId(existingOrder.getId());
                ordersService.updateOrdersStatus(orders);
            }
        }
        return i;

    }

    @Override
    public List<AfterSaleOrder> getAfterSaleOrderByCondition(Integer afterSaleStatus, String orderNumber,
                                                             String startDateStr, String endDateStr) {
        return afterSaleOrderMapper.getAfterSaleOrderByCondition(afterSaleStatus, orderNumber, startDateStr, endDateStr);
    }
}
