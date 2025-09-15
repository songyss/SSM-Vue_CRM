package com.csi.service.impl;

import com.csi.domain.PromotionPlans;
import com.csi.mapper.PromotionPlansMapper;
import com.csi.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MarketingServiceImpl implements MarketingService {

    @Autowired
    private PromotionPlansMapper promotionPlansMapper;


    @Override
    public int savePromotionPlans(PromotionPlans promotionPlans) {
        int result = promotionPlansMapper.savePromotionPlans(promotionPlans);
        return result;
    }

    @Override
    public List<PromotionPlans> getPromotionPlans() {
        List<PromotionPlans> promotionPlans = promotionPlansMapper.getPromotionPlans();
        return promotionPlans;
    }
}
