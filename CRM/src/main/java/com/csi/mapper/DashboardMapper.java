package com.csi.mapper;

import com.csi.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DashboardMapper {
    
    /**
     * 获取总销售额
     */
    Double getTotalSales();
    
    /**
     * 获取总订单数
     */
    Integer getTotalOrders();
    
    /**
     * 获取新增客户数
     */
    Integer getNewCustomers();
    
    /**
     * 获取转化率（订单数/客户数）
     */
    Double getConversionRate();
    
    /**
     * 获取销售趋势数据（最近6个月）
     */
    List<SalesData> getSalesTrendData();
    
    /**
     * 获取客户来源分布
     */
    List<CustomerSource> getCustomerSources();
    


    
    /**
     * 获取商机转化率漏斗数据
     */
    List<OpportunityConversion> getOpportunityConversions();
    
    /**
     * 获取客户类型分布
     */
    List<CustomerType> getCustomerTypes();
    
    /**
     * 获取销售业绩排行
     */
    List<SalesRank> getSalesRanks();
    
    /**
     * 获取客户留存率分析数据
     */
    List<RetentionData> getRetentionData();
    
    /**
     * 获取客户生命周期价值分析数据
     */
    List<CustomerLifetimeValue> getCustomerLifetimeValues();
    
    /**
     * 获取销售预测数据（最近9个月，包括未来3个月的预测）
     */
    List<SalesData> getForecastData();
}