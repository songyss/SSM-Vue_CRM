package com.csi.service;

import com.csi.domain.AfterSaleOrder;

public interface AfterSaleOrderService {

    //根据状态查询售后订单
    AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus);
    //根据订单号查询售后订单
    AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber);
    //修改售后订单状态
    int updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder);
}
