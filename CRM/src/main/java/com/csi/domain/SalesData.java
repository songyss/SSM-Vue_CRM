package com.csi.domain;

// SalesData.java
public class SalesData {
    private String month;
    private double sales;
    private int orders;
    
    // 构造函数
    public SalesData() {}
    
    public SalesData(String month, double sales, int orders) {
        this.month = month;
        this.sales = sales;
        this.orders = orders;
    }
    
    // Getter和Setter方法
    public String getMonth() {
        return month;
    }
    
    public void setMonth(String month) {
        this.month = month;
    }
    
    public double getSales() {
        return sales;
    }
    
    public void setSales(double sales) {
        this.sales = sales;
    }
    
    public int getOrders() {
        return orders;
    }
    
    public void setOrders(int orders) {
        this.orders = orders;
    }
}