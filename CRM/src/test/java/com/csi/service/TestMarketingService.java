package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.PromotionPlans;
import com.csi.service.impl.MarketingServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestMarketingService {
    @Autowired
    public MarketingService marketingService;

    @Test
    public void test() {

        PromotionPlans promotionPlans =new PromotionPlans();
        promotionPlans.setId(3);
        promotionPlans.setTitle("测试");
        promotionPlans.setContent("测试");
        promotionPlans.setBudget(1000);
        promotionPlans.setSubmitterId(1);
        promotionPlans.setApproverId(1);
        promotionPlans.setFeedback("测试");
        promotionPlans.setStatus(0);
        promotionPlans.setCreateTime("2020-05-05");
        promotionPlans.setUpdateTime("2020-05-05");
        marketingService.savePromotionPlans(promotionPlans);


    }
    @Test
    public void test1() {
        List<PromotionPlans> promotionPlans = marketingService.getPromotionPlans();
        System.out.println(promotionPlans);
    }

    @Test
    public void test4(){
        PromotionPlans promotionPlans =new PromotionPlans();
        promotionPlans.setId(3);
        promotionPlans.setStatus(1);
        promotionPlans.setTitle("测试");
        promotionPlans.setContent("测试");
        promotionPlans.setBudget(1000);
        promotionPlans.setSubmitterId(1);
        promotionPlans.setApproverId(1);

        int i = marketingService.updateByStatus(promotionPlans);
        System.out.println(i);
    }
}
