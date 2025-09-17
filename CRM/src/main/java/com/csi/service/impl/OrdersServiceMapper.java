package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.domain.Orders;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.mapper.OrdersMapper;
import com.csi.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrdersServiceMapper implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private AfterSaleOrderMapper afterSaleOrderMapper;

    @Override
    public int saveOrders(Orders orders) {
        return 0;
    }

    @Override
    public Orders getOrdersByStatus() {
        return ordersMapper.getOrdersByStatus();
    }

    @Override
    public int updateOrders(int id, int status) {
//        ordersMapper.updateOrders(id, status);
//        if(status==5){
//            afterSaleOrderMapper.saveAfterSaleOrder(new AfterSaleOrder());
//        }
        return 0;

    }


}
