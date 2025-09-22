package com.csi.mapper;

import com.csi.domain.Opportunities;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OpportunitiesMapper {

    List<Opportunities> getOpportunities();
    
    // 根据ID获取商机详情
    Opportunities getOpportunityById(Integer id);

    // 修改条件查询方法，移除对不存在的opportunity_stage表的依赖
    List<Opportunities> selectOpportunitiesByCondition(@Param("customerName") String customerName,
                                                      @Param("stage") Integer stage,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate);

    int addOpportunities(Opportunities opportunities);

    int updateOpportunities(Opportunities opportunities);

}
