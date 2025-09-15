package com.csi.mapper;

import com.csi.domain.PromotionPlans;

import java.util.List;

public interface PromotionPlansMapper {
/**
 * 创建活动审批
 **/
    int savePromotionPlans(PromotionPlans promotionPlans);

    List<PromotionPlans> getPromotionPlans();
}
