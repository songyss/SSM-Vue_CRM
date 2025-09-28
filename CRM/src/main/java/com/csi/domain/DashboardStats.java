package com.csi.domain;

import java.util.List;

// DashboardStats.java
public class DashboardStats {
    private double totalSales;
    private double totalSalesGrowth; // 同比上月增长率
    private int totalOrders;
    private double totalOrdersGrowth; // 同比上月增长率
    private int newCustomers;
    private double newCustomersGrowth; // 同比上月增长率
    private double conversionRate;
    private double conversionRateGrowth; // 同比上月增长率
    private List<SalesData> salesData;
    private List<CustomerSource> customerSources;
    private List<SalesChannel> salesChannels;

    // 构造函数
    public DashboardStats() {}

    // Getter和Setter方法
    public double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }

    public double getTotalSalesGrowth() {
        return totalSalesGrowth;
    }

    public void setTotalSalesGrowth(double totalSalesGrowth) {
        this.totalSalesGrowth = totalSalesGrowth;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalOrdersGrowth() {
        return totalOrdersGrowth;
    }

    public void setTotalOrdersGrowth(double totalOrdersGrowth) {
        this.totalOrdersGrowth = totalOrdersGrowth;
    }

    public int getNewCustomers() {
        return newCustomers;
    }

    public void setNewCustomers(int newCustomers) {
        this.newCustomers = newCustomers;
    }

    public double getNewCustomersGrowth() {
        return newCustomersGrowth;
    }

    public void setNewCustomersGrowth(double newCustomersGrowth) {
        this.newCustomersGrowth = newCustomersGrowth;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public double getConversionRateGrowth() {
        return conversionRateGrowth;
    }

    public void setConversionRateGrowth(double conversionRateGrowth) {
        this.conversionRateGrowth = conversionRateGrowth;
    }

    public List<SalesData> getSalesData() {
        return salesData;
    }

    public void setSalesData(List<SalesData> salesData) {
        this.salesData = salesData;
    }

    public List<CustomerSource> getCustomerSources() {
        return customerSources;
    }

    public void setCustomerSources(List<CustomerSource> customerSources) {
        this.customerSources = customerSources;
    }

    public List<SalesChannel> getSalesChannels() {
        return salesChannels;
    }

    public void setSalesChannels(List<SalesChannel> salesChannels) {
        this.salesChannels = salesChannels;
    }
}
