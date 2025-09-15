package com.csi.service;

import com.csi.domain.PromotionPlans;

import java.util.List;

public interface MarketingService {

    int savePromotionPlans(PromotionPlans promotionPlans);

    List<PromotionPlans> getPromotionPlans();

    int updateByStatus(PromotionPlans promotionPlans);

}
