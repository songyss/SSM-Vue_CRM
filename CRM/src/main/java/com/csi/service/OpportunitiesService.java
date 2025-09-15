package com.csi.service;

import com.csi.domain.Opportunities;

import java.util.List;

public interface OpportunitiesService {

    List<Opportunities> getOpportunities();//查询所有商机

    int addOpportunities(Opportunities opportunities);//新建商机

    int updateOpportunities(Opportunities opportunities);//更改商机状态

}
