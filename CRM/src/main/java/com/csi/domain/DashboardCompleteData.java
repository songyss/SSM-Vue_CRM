package com.csi.domain;

import java.util.List;

public class DashboardCompleteData {
    private DashboardStats basicStats;
    private List<OpportunityConversion> opportunityConversions;
    private List<CustomerType> customerTypes;
    private List<SalesRank> salesRanks;
    private List<RetentionData> retentionData;
    private List<CustomerLifetimeValue> customerLifetimeValues;
    private List<ProductSalesHeatmap> productSalesHeatmaps;
    private List<SalesData> forecastData;

    public DashboardCompleteData() {}

    public DashboardStats getBasicStats() {
        return basicStats;
    }

    public void setBasicStats(DashboardStats basicStats) {
        this.basicStats = basicStats;
    }

    public List<OpportunityConversion> getOpportunityConversions() {
        return opportunityConversions;
    }

    public void setOpportunityConversions(List<OpportunityConversion> opportunityConversions) {
        this.opportunityConversions = opportunityConversions;
    }

    public List<CustomerType> getCustomerTypes() {
        return customerTypes;
    }

    public void setCustomerTypes(List<CustomerType> customerTypes) {
        this.customerTypes = customerTypes;
    }

    public List<SalesRank> getSalesRanks() {
        return salesRanks;
    }

    public void setSalesRanks(List<SalesRank> salesRanks) {
        this.salesRanks = salesRanks;
    }

    public List<RetentionData> getRetentionData() {
        return retentionData;
    }

    public void setRetentionData(List<RetentionData> retentionData) {
        this.retentionData = retentionData;
    }

    public List<CustomerLifetimeValue> getCustomerLifetimeValues() {
        return customerLifetimeValues;
    }

    public void setCustomerLifetimeValues(List<CustomerLifetimeValue> customerLifetimeValues) {
        this.customerLifetimeValues = customerLifetimeValues;
    }

    public List<ProductSalesHeatmap> getProductSalesHeatmaps() {
        return productSalesHeatmaps;
    }

    public void setProductSalesHeatmaps(List<ProductSalesHeatmap> productSalesHeatmaps) {
        this.productSalesHeatmaps = productSalesHeatmaps;
    }

    public List<SalesData> getForecastData() {
        return forecastData;
    }

    public void setForecastData(List<SalesData> forecastData) {
        this.forecastData = forecastData;
    }
}