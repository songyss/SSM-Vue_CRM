package com.csi.mapper;

import com.csi.domain.MarketingActivities;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MarketActivitiesMapper {
    //添加市场活动
    int  addMarketActivity (MarketingActivities activity);

    // 查询所有
    List<MarketingActivities> getAllMarketActivities();

    // 按名称查询
    List<MarketingActivities> getMarketActivityByName(String name);

    // 按状态查询
    List<MarketingActivities> getMarketActivityByStatus(int activityStatus);

    // 更新
    int updateMarketActivity(MarketingActivities activity);

    // 删除
    int deleteMarketActivity(@Param("id") int id);

    MarketingActivities getMarketActivityById(@Param("id") int id);



}
