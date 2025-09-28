package com.csi.domain;

public class CustomerLifetimeValue {
    private String month;
    private double newCustomerValue;
    private double activeCustomerValue;
    private double loyalCustomerValue;

    public CustomerLifetimeValue() {}

    public CustomerLifetimeValue(String month, double newCustomerValue, double activeCustomerValue, double loyalCustomerValue) {
        this.month = month;
        this.newCustomerValue = newCustomerValue;
        this.activeCustomerValue = activeCustomerValue;
        this.loyalCustomerValue = loyalCustomerValue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getNewCustomerValue() {
        return newCustomerValue;
    }

    public void setNewCustomerValue(double newCustomerValue) {
        this.newCustomerValue = newCustomerValue;
    }

    public double getActiveCustomerValue() {
        return activeCustomerValue;
    }

    public void setActiveCustomerValue(double activeCustomerValue) {
        this.activeCustomerValue = activeCustomerValue;
    }

    public double getLoyalCustomerValue() {
        return loyalCustomerValue;
    }

    public void setLoyalCustomerValue(double loyalCustomerValue) {
        this.loyalCustomerValue = loyalCustomerValue;
    }
}