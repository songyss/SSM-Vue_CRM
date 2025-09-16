package com.csi.service.impl;

import com.csi.domain.Orders;
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

    @Override
    public int addOrders(Orders order) {
        int i = ordersMapper.addOrders(order);
        return i;
    }

    @Override
    public int updateOrdersStatus(int id, int status) {
        int i = ordersMapper.updateOrdersStatus(id, status);
        return i;
    }

    @Override
    public List<Orders> selectPersonalOrders(int id) {
        return ordersMapper.selectPersonalOrders(id);
    }
}
