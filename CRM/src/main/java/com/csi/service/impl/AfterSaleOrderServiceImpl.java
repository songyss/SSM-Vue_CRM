package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.domain.Orders;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.service.AfterSaleOrderService;
import com.csi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus) {
        return afterSaleOrderMapper.getAfterSaleOrderByStatus(afterSaleStatus);
    }
    /**
     * 根据售后订单编号查询售后订单
     * */

    @Override
    public AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber) {
        return afterSaleOrderMapper.getAfterSaleOrderByOrderNumber(orderNumber);
    }
/**
 * 修改售后订单状态
 * 根据修改的状态值修改订单状态
 * */
    @Override
    public int updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder) {
        int i = afterSaleOrderMapper.updateAfterSaleOrder(afterSaleOrder.getAfterSaleStatus(), afterSaleOrder.getId());
        if (afterSaleOrder.getAfterSaleStatus()==2){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(4);
            ordersService.updateOrdersStatus(orders);
        }else if (afterSaleOrder.getAfterSaleStatus()==3){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(6);
            ordersService.updateOrdersStatus(orders);

        }else if (afterSaleOrder.getAfterSaleStatus()==4){
            Orders orders = new Orders();
            orders.setOrderNumber(afterSaleOrder.getOrderNumber());
            orders.setOrderStatus(7);
            ordersService.updateOrdersStatus(orders);
        }
        return i;

    }
}
