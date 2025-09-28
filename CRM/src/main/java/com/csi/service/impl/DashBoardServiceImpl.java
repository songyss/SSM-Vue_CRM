package com.csi.service.impl;

import com.csi.domain.*;
import com.csi.mapper.DashboardMapper;
import com.csi.service.DashboardService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashboardService {
    
    @Resource
    private DashboardMapper dashboardMapper;
    
    @Override
    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        // 使用DashboardMapper获取实际数据
        Double totalSales = dashboardMapper.getTotalSales();
        Integer totalOrders = dashboardMapper.getTotalOrders();
        Integer newCustomers = dashboardMapper.getNewCustomers();
        Double conversionRate = dashboardMapper.getConversionRate();
        
        // 如果数据库中没有数据，使用默认值
        stats.setTotalSales(totalSales != null ? totalSales : 1280000.0);
        stats.setTotalOrders(totalOrders != null ? totalOrders : 1024);
        stats.setNewCustomers(newCustomers != null ? newCustomers : 256);
        stats.setConversionRate(conversionRate != null ? conversionRate : 32.0);
        
        // 设置增长率
        stats.setTotalSalesGrowth(12.0);
        stats.setTotalOrdersGrowth(8.0);
        stats.setNewCustomersGrowth(5.0);
        stats.setConversionRateGrowth(-2.0);

        // 设置销售数据
        List<SalesData> salesData = dashboardMapper.getSalesTrendData();
        if (salesData == null || salesData.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            salesData = new ArrayList<>();
            salesData.add(new SalesData("1月", 120000.0, 12));
            salesData.add(new SalesData("2月", 200000.0, 20));
            salesData.add(new SalesData("3月", 150000.0, 15));
            salesData.add(new SalesData("4月", 80000.0, 8));
            salesData.add(new SalesData("5月", 70000.0, 7));
            salesData.add(new SalesData("6月", 110000.0, 11));
        } else {
            // 格式化月份显示
            for (SalesData data : salesData) {
                String month = data.getMonth();
                if (month.length() == 7) { // 格式为 YYYY-MM
                    data.setMonth(month.substring(5) + "月"); // 提取月份并添加"月"
                }
            }
        }
        stats.setSalesData(salesData);

        // 设置客户来源分布
        List<CustomerSource> customerSources = dashboardMapper.getCustomerSources();
        if (customerSources == null || customerSources.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            customerSources = new ArrayList<>();
            customerSources.add(new CustomerSource("搜索引擎", 1048));
            customerSources.add(new CustomerSource("直接访问", 735));
            customerSources.add(new CustomerSource("邮件营销", 580));
            customerSources.add(new CustomerSource("联盟广告", 484));
            customerSources.add(new CustomerSource("视频广告", 300));
        }
        stats.setCustomerSources(customerSources);

//        // 设置销售渠道占比
//        List<SalesChannel> salesChannels = dashboardMapper.getSalesChannels();
//        if (salesChannels == null || salesChannels.isEmpty()) {
//            // 如果数据库中没有数据，使用模拟数据
//            salesChannels = new ArrayList<>();
//            salesChannels.add(new SalesChannel("线上商城", 60));
//            salesChannels.add(new SalesChannel("线下门店", 40));
//            salesChannels.add(new SalesChannel("代理商", 20));
//            salesChannels.add(new SalesChannel("大客户", 10));
//        }
//        stats.setSalesChannels(salesChannels);

        return stats;
    }

    @Override
    public DashboardCompleteData getDashboardCompleteData() {
        DashboardCompleteData completeData = new DashboardCompleteData();
        
        // 设置基本统计数据
        completeData.setBasicStats(getDashboardStats());
        
        // 设置商机转化率漏斗数据
        List<OpportunityConversion> opportunityConversions = dashboardMapper.getOpportunityConversions();
        if (opportunityConversions == null || opportunityConversions.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            opportunityConversions = new ArrayList<>();
            opportunityConversions.add(new OpportunityConversion("线索", 100, 100.0));
            opportunityConversions.add(new OpportunityConversion("商机", 60, 60.0));
            opportunityConversions.add(new OpportunityConversion("提案", 35, 35.0));
            opportunityConversions.add(new OpportunityConversion("谈判", 20, 20.0));
            opportunityConversions.add(new OpportunityConversion("成交", 10, 10.0));
        }
        completeData.setOpportunityConversions(opportunityConversions);
        
        // 设置客户类型分布数据
        List<CustomerType> customerTypes = dashboardMapper.getCustomerTypes();
        if (customerTypes == null || customerTypes.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            customerTypes = new ArrayList<>();
            customerTypes.add(new CustomerType("新客户", 350, 27.7));
            customerTypes.add(new CustomerType("老客户", 420, 33.1));
            customerTypes.add(new CustomerType("潜在客户", 280, 22.1));
            customerTypes.add(new CustomerType("重要客户", 150, 11.8));
            customerTypes.add(new CustomerType("流失客户", 80, 6.3));
        }
        completeData.setCustomerTypes(customerTypes);
        
        // 设置销售业绩排行数据
        List<SalesRank> salesRanks = dashboardMapper.getSalesRanks();
        if (salesRanks == null || salesRanks.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            salesRanks = new ArrayList<>();
            salesRanks.add(new SalesRank("张三", 1280000.0));
            salesRanks.add(new SalesRank("李四", 980000.0));
            salesRanks.add(new SalesRank("王五", 860000.0));
            salesRanks.add(new SalesRank("赵六", 780000.0));
            salesRanks.add(new SalesRank("钱七", 660000.0));
            salesRanks.add(new SalesRank("孙八", 580000.0));
        }
        completeData.setSalesRanks(salesRanks);
        
        // 设置客户留存率分析数据
        List<RetentionData> retentionData = dashboardMapper.getRetentionData();
        if (retentionData == null || retentionData.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            retentionData = new ArrayList<>();
            retentionData.add(new RetentionData("1月", 65.0, 45.0, 32.0));
            retentionData.add(new RetentionData("2月", 62.0, 42.0, 30.0));
            retentionData.add(new RetentionData("3月", 68.0, 48.0, 35.0));
            retentionData.add(new RetentionData("4月", 63.0, 44.0, 33.0));
            retentionData.add(new RetentionData("5月", 66.0, 46.0, 34.0));
            retentionData.add(new RetentionData("6月", 64.0, 47.0, 36.0));
        } else {
            // 格式化月份显示
            for (RetentionData data : retentionData) {
                String month = data.getMonth();
                if (month.length() == 7) { // 格式为 YYYY-MM
                    data.setMonth(month.substring(5) + "月"); // 提取月份并添加"月"
                }
            }
        }
        completeData.setRetentionData(retentionData);
        
        // 设置客户生命周期价值分析数据
        List<CustomerLifetimeValue> clvData = dashboardMapper.getCustomerLifetimeValues();
        if (clvData == null || clvData.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            clvData = new ArrayList<>();
            clvData.add(new CustomerLifetimeValue("1月", 2800.0, 4500.0, 7500.0));
            clvData.add(new CustomerLifetimeValue("2月", 3200.0, 4800.0, 7800.0));
            clvData.add(new CustomerLifetimeValue("3月", 2900.0, 5200.0, 8300.0));
            clvData.add(new CustomerLifetimeValue("4月", 3100.0, 5500.0, 8800.0));
            clvData.add(new CustomerLifetimeValue("5月", 3300.0, 5800.0, 9200.0));
            clvData.add(new CustomerLifetimeValue("6月", 3500.0, 6200.0, 9500.0));
        } else {
            // 格式化月份显示
            for (CustomerLifetimeValue data : clvData) {
                String month = data.getMonth();
                if (month.length() == 7) { // 格式为 YYYY-MM
                    data.setMonth(month.substring(5) + "月"); // 提取月份并添加"月"
                }
            }
        }
        completeData.setCustomerLifetimeValues(clvData);
        
        // 设置产品销售热力分布数据（根据用户要求，保留死数据）
        List<ProductSalesHeatmap> productHeatmapData = new ArrayList<>();
        // 华东区域
        productHeatmapData.add(new ProductSalesHeatmap("华东", "产品A", 8500.0));
        productHeatmapData.add(new ProductSalesHeatmap("华东", "产品B", 6200.0));
        productHeatmapData.add(new ProductSalesHeatmap("华东", "产品C", 9800.0));
        productHeatmapData.add(new ProductSalesHeatmap("华东", "产品D", 5200.0));
        productHeatmapData.add(new ProductSalesHeatmap("华东", "产品E", 7800.0));
        // 华北区域
        productHeatmapData.add(new ProductSalesHeatmap("华北", "产品A", 6500.0));
        productHeatmapData.add(new ProductSalesHeatmap("华北", "产品B", 7800.0));
        productHeatmapData.add(new ProductSalesHeatmap("华北", "产品C", 5200.0));
        productHeatmapData.add(new ProductSalesHeatmap("华北", "产品D", 4800.0));
        productHeatmapData.add(new ProductSalesHeatmap("华北", "产品E", 6300.0));
        // 其他区域的数据...
        completeData.setProductSalesHeatmaps(productHeatmapData);
        
        // 设置销售预测数据
        List<SalesData> forecastData = dashboardMapper.getForecastData();
        if (forecastData == null || forecastData.isEmpty()) {
            // 如果数据库中没有数据，使用模拟数据
            forecastData = new ArrayList<>();
            forecastData.add(new SalesData("1月", 120000.0, 12));
            forecastData.add(new SalesData("2月", 200000.0, 20));
            forecastData.add(new SalesData("3月", 150000.0, 15));
            forecastData.add(new SalesData("4月", 80000.0, 8));
            forecastData.add(new SalesData("5月", 70000.0, 7));
            forecastData.add(new SalesData("6月", 110000.0, 11));
            forecastData.add(new SalesData("7月", 130000.0, 13));
            forecastData.add(new SalesData("8月", 150000.0, 15));
            forecastData.add(new SalesData("9月", 180000.0, 18));
        } else {
            // 格式化月份显示
            for (SalesData data : forecastData) {
                String month = data.getMonth();
                if (month.length() == 7) { // 格式为 YYYY-MM
                    data.setMonth(month.substring(5) + "月"); // 提取月份并添加"月"
                }
            }
        }
        completeData.setForecastData(forecastData);
        
        return completeData;
    }

    /**
     * 计算增长率
     * @param current 当前值
     * @param previous 上期值
     * @return 增长率（百分比）
     */
    private double calculateGrowthRate(double current, double previous) {
        if (previous == 0) {
            return previous == 0 && current > 0 ? 100.0 : 0.0;
        }
        return ((current - previous) / previous) * 100;
    }
}
