package com.csi.mapper;

import com.csi.domain.MarketingActivities;

import java.util.List;

public interface MarketActivitiesMapper {
    int  addMarketActivity (MarketingActivities activity);

    List<MarketingActivities> getAllMarketActivities();

    List<MarketingActivities> getMarketActivityByName(String name);

    List<MarketingActivities> getMarketActivityByStatus(int activityStatus);
}
