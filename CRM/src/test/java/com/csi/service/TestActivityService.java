package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;

import com.csi.domain.MarketingActivities;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestActivityService {

    @Autowired
    private MarketActivitiesService marketActivitiesService;

    @Test
    public void test() {
        List<MarketingActivities> allMarketActivity = marketActivitiesService.getAllMarketActivity();

        for (MarketingActivities marketingActivities : allMarketActivity) {
            System.out.println(marketingActivities);
        }

//        activity.setName("测试活动");
//        activity.setContent("测试内容");
//        activity.setLocation("测试地点");
//        activity.setForm("测试形式");
//        activity.setStartTime("2020-01-01");
//        activity.setEndTime("2020-01-01");
//        activity.setEstimatedCost(1000);
//        activity.setActualCost(1000);
//        activity.setManagerId(1);
    }
    @Test
    public void testByName(){
        List<MarketingActivities> activityByName = marketActivitiesService.getMarketActivityByName("大促");
        for (MarketingActivities marketingActivities : activityByName) {
            System.out.println(marketingActivities);
        }
    }

    @Test
    public void testByStatus(){
        List<MarketingActivities> activityByStatus = marketActivitiesService.getMarketActivityByStatus(1);
        for (MarketingActivities marketingActivities : activityByStatus) {
            System.out.println(marketingActivities);
        }
    }
}
