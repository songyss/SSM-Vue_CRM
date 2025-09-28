package com.csi.domain;

public class ProductSalesHeatmap {
    private String region;
    private String product;
    private double salesAmount;

    public ProductSalesHeatmap() {}

    public ProductSalesHeatmap(String region, String product, double salesAmount) {
        this.region = region;
        this.product = product;
        this.salesAmount = salesAmount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
}