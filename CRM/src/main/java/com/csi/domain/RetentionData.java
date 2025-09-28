package com.csi.domain;

public class RetentionData {
    private String month;
    private double sevenDayRetention;
    private double thirtyDayRetention;
    private double ninetyDayRetention;

    public RetentionData() {}

    public RetentionData(String month, double sevenDayRetention, double thirtyDayRetention, double ninetyDayRetention) {
        this.month = month;
        this.sevenDayRetention = sevenDayRetention;
        this.thirtyDayRetention = thirtyDayRetention;
        this.ninetyDayRetention = ninetyDayRetention;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getSevenDayRetention() {
        return sevenDayRetention;
    }

    public void setSevenDayRetention(double sevenDayRetention) {
        this.sevenDayRetention = sevenDayRetention;
    }

    public double getThirtyDayRetention() {
        return thirtyDayRetention;
    }

    public void setThirtyDayRetention(double thirtyDayRetention) {
        this.thirtyDayRetention = thirtyDayRetention;
    }

    public double getNinetyDayRetention() {
        return ninetyDayRetention;
    }

    public void setNinetyDayRetention(double ninetyDayRetention) {
        this.ninetyDayRetention = ninetyDayRetention;
    }
}