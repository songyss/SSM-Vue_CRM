package com.csi.domain;

public class CustomerType {
    private String type;
    private int count;
    private double percentage;

    public CustomerType() {}

    public CustomerType(String type, int count, double percentage) {
        this.type = type;
        this.count = count;
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}