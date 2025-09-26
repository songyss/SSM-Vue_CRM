package com.csi.service.impl;

import cn.hutool.core.date.DateTime;
import com.csi.domain.MarketingActivities;
import com.csi.domain.PromotionPlans;
import com.csi.mapper.MarketActivitiesMapper;
import com.csi.mapper.PromotionPlansMapper;
import com.csi.service.MarketActivitiesService;
import com.csi.service.MarketingService;
import jakarta.annotation.Resource;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MarketingServiceImpl implements MarketingService {

   @Resource
    private PromotionPlansMapper promotionPlansMapper;

    @Resource
    private MarketActivitiesMapper marketActivitiesMapper;

    @Resource
    MarketActivitiesService marketActivitiesService;


    @Override
    public int savePromotionPlans(PromotionPlans promotionPlans) {

        int result = 0;
        //如果状态是1，则增加一个事务，否则不增加事务
        if(promotionPlans.getStatus()==1){
            if(promotionPlansMapper.savePromotionPlans(promotionPlans) == 1){
                MarketingActivities activity = new MarketingActivities();
                activity.setName(promotionPlans.getTitle());
                activity.setPlanId(promotionPlans.getId());
                activity.setContent(promotionPlans.getContent());
                activity.setEstimatedCost(promotionPlans.getBudget());
                activity.setManagerId(promotionPlans.getApproverId());
                activity.setActivityStatus(1);
                activity.setCreateTime(new Date().toString());
                System.out.println();
                result = marketActivitiesMapper.addMarketActivity(activity);
            }

        }else {
            result =  promotionPlansMapper.savePromotionPlans(promotionPlans);
        }
        return result;
    }



    @Override
    public List<PromotionPlans> getPromotionPlans() {
        List<PromotionPlans> promotionPlans = promotionPlansMapper.getPromotionPlans();
        return promotionPlans;
    }

    /**
     * 修改状态
     * 市场部经理做审批使用。
     * @param promotionPlans
     * @return
     */
    @Override
    public int updateByStatus(PromotionPlans promotionPlans) {
        int result = 0;
        if(promotionPlansMapper.updateByStatus(promotionPlans.getStatus(), promotionPlans.getId(),promotionPlans.getApproverId(),promotionPlans.getFeedback()) ==1) {
            if(promotionPlans.getStatus() == 1){
                MarketingActivities activity = new MarketingActivities();
                activity.setName(promotionPlans.getTitle());
                activity.setPlanId(promotionPlans.getId());
                activity.setContent(promotionPlans.getContent());
                activity.setEstimatedCost(promotionPlans.getBudget());
                activity.setManagerId(promotionPlans.getApproverId());
                activity.setActivityStatus(1);
                activity.setCreateTime(new DateTime().toString());
                result = marketActivitiesService.addMarketActivity(activity);
            } else if (promotionPlans.getStatus() == 2) {
                result = promotionPlansMapper.updateByStatus(promotionPlans.getStatus(), promotionPlans.getId(),promotionPlans.getApproverId(),promotionPlans.getFeedback());
            }
        }
        return result;
    }
}
