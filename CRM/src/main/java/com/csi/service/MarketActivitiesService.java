package com.csi.service;


import com.csi.domain.MarketingActivities;

import java.util.List;

public interface MarketActivitiesService  {

    int addMarketActivity(MarketingActivities activity);

    List<MarketingActivities> getAllMarketActivity();

    List<MarketingActivities> getMarketActivityByName(String name);

    List<MarketingActivities> getMarketActivityByStatus(int activityStatus);
}
