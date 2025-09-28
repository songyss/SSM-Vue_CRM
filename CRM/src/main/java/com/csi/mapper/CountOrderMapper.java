package com.csi.mapper;

import com.csi.domain.SalesChannel;
import com.csi.domain.SalesData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// OrderMapper.java
@Mapper
public interface CountOrderMapper {
    
    /**
     * 获取当月总销售额
     */
    double getCurrentMonthTotalSales();
    
    /**
     * 获取上月总销售额
     */
    double getLastMonthTotalSales();
    
    /**
     * 获取当月总订单数
     */
    int getCurrentMonthTotalOrders();
    
    /**
     * 获取上月总订单数
     */
    int getLastMonthTotalOrders();
    
    /**
     * 获取当月独立客户数
     */
    long getCurrentMonthUniqueCustomers();
    
    /**
     * 获取上月独立客户数
     */
    long getLastMonthUniqueCustomers();
    
    /**
     * 获取近6个月销售数据
     */
    List<SalesData> getSalesDataLastSixMonths();
    
    /**
     * 获取销售渠道分布
     */
    List<SalesChannel> getSalesChannels();
}