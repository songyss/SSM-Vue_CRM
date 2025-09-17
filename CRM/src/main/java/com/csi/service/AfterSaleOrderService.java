package com.csi.service;

import com.csi.domain.AfterSaleOrder;

public interface AfterSaleOrderService {
    //生成售后订单
    int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder);
    //根据状态查询售后订单
    AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus);
    //根据订单号查询售后订单
    AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber);
}
