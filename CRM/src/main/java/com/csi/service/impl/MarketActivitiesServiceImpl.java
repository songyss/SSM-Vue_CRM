package com.csi.service.impl;

import com.csi.domain.MarketingActivities;
import com.csi.mapper.MarketActivitiesMapper;
import com.csi.service.MarketActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MarketActivitiesServiceImpl implements MarketActivitiesService {


    @Autowired
    private MarketActivitiesMapper marketActivitiesMapper;
    /**
     * 添加市场活动
     * @param activity
     * @return
     */
    @Override
    public int addMarketActivity(MarketingActivities activity) {
        return marketActivitiesMapper.addMarketActivity(activity);
    }

    /**
     * 获取所有市场活动
     * @return
     */
    @Override
    public List<MarketingActivities> getAllMarketActivity() {
        return marketActivitiesMapper.getAllMarketActivities();
    }

    /**
     * m模糊查询名称获取市场活动
     * @param name
     * @return
     */
    @Override
    public List<MarketingActivities> getMarketActivityByName(String name) {
        return marketActivitiesMapper.getMarketActivityByName(name);
    }

    /**
     * 根据活动状态 activityStatus获取市场活动
     * @param activityStatus
     * @return
     *
     */
    @Override
    public List<MarketingActivities> getMarketActivityByStatus(int activityStatus) {
        return marketActivitiesMapper.getMarketActivityByStatus(activityStatus);
    }


}
