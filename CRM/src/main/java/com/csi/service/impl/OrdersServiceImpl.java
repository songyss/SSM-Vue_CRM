package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.domain.Orders;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.mapper.OrdersMapper;
import com.csi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private AfterSaleOrderMapper afterSaleOrderMapper;

    @Override
    public int addOrders(Orders order) {
        int i = ordersMapper.addOrders(order);
        return i;
    }

    @Override
    public int updateOrdersStatus(Orders order) {
        int i = ordersMapper.updateOrdersStatus(order.getOrderStatus(),order.getId());
        if (order.getOrderStatus()==5) {
            AfterSaleOrder afterSaleOrder = new AfterSaleOrder();
            afterSaleOrder.setOrderNumber(order.getOrderNumber());
            afterSaleOrder.setCustomerId(order.getCustomerId());
            afterSaleOrder.setTotalAmount(order.getTotalAmount());
            afterSaleOrder.setSignedDate(order.getSignedDate());
            afterSaleOrder.setAfterSaleStatus(order.getOrderStatus());
            afterSaleOrder.setFileUrl(order.getFileUrl());
            afterSaleOrder.setNotes(order.getNotes());
            afterSaleOrder.setCreateTime(order.getCreateTime());
            afterSaleOrderMapper.saveAfterSaleOrder(afterSaleOrder);
        }
        return i;
    }

    @Override
    public List<Orders> selectPersonalOrders(int id) {
        return ordersMapper.selectPersonalOrders(id);
    }
}
