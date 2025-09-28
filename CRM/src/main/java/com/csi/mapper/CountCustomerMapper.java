package com.csi.mapper;

import com.csi.domain.CustomerSource;

import java.util.List;

public interface CountCustomerMapper {

    /**
     * 获取当月新增客户数
     */
    int getCurrentMonthNewCustomers();

    /**
     * 获取上月新增客户数
     */
    int getLastMonthNewCustomers();

    /**
     * 获取当月访客数
     */
    long getCurrentMonthVisitors();

    /**
     * 获取上月访客数
     */
    long getLastMonthVisitors();

    /**
     * 获取客户来源分布
     */
    List<CustomerSource> getCustomerSources();
}