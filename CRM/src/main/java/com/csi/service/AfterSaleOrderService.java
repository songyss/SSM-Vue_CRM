package com.csi.service;

import com.csi.domain.AfterSaleOrder;

import java.util.List;

public interface AfterSaleOrderService {

    //根据状态查询售后订单
    List<AfterSaleOrder> getAfterSaleOrderByStatus();
    //根据订单号查询售后订单
    List<AfterSaleOrder> getAfterSaleOrderByOrderNumber(String orderNumber);
    //    根据时间范围查询售后订单
    List<AfterSaleOrder> getAfterSaleOrderByDateRange(String startDate, String endDate);
    //修改售后订单状态
    int updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder);

    /**
     * 组合查询售后订单（字符串日期参数版本）
     * @param afterSaleStatus 售后状态
     * @param orderNumber 订单号
     * @param startDateStr 开始时间字符串（格式：yyyy-MM-dd）
     * @param endDateStr 结束时间字符串（格式：yyyy-MM-dd）
     * @return 售后订单列表
     */
    // AfterSaleOrderService.java
    List<AfterSaleOrder> getAfterSaleOrderByCondition(Integer afterSaleStatus, String orderNumber,
                                                      String startDateStr, String endDateStr);

}
