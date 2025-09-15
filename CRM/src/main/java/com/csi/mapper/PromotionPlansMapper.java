package com.csi.mapper;

import com.csi.domain.PromotionPlans;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PromotionPlansMapper {
/**
 * 创建活动审批
 **/
    int savePromotionPlans(PromotionPlans promotionPlans);

    List<PromotionPlans> getPromotionPlans();

    int updateByStatus(@Param("status") int status,@Param("id") int id);
}
