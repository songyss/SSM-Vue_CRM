package com.csi.service;

import com.csi.domain.AfterSaleOrder;

import java.util.List;

public interface AfterSaleOrderService {

    //根据状态查询售后订单
    List<AfterSaleOrder> getAfterSaleOrderByStatus(int afterSaleStatus);
    //根据订单号查询售后订单
    List<AfterSaleOrder> getAfterSaleOrderByOrderNumber(int orderNumber);
    //修改售后订单状态
    int updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder);
}
