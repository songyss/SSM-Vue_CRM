package com.csi.domain;

public class OpportunityConversion {
    private String stage;
    private int value;
    private double percentage;

    public OpportunityConversion() {}

    public OpportunityConversion(String stage, int value, double percentage) {
        this.stage = stage;
        this.value = value;
        this.percentage = percentage;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}