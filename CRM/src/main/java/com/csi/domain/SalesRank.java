package com.csi.domain;

public class SalesRank {
    private String salesperson;
    private double salesAmount;

    public SalesRank() {}

    public SalesRank(String salesperson, double salesAmount) {
        this.salesperson = salesperson;
        this.salesAmount = salesAmount;
    }

    public String getSalesperson() {
        return salesperson;
    }

    public void setSalesperson(String salesperson) {
        this.salesperson = salesperson;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
}