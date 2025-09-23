package com.csi.service;

import com.csi.domain.Opportunities;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OpportunitiesService {

    List<Opportunities> getOpportunities();//查询所有商机

    // 修改分页查询方法，添加排序参数
    PageInfo<Opportunities> getOpportunitiesByPage(int page, int size, String customerName, 
                                                  Integer stage, String startDate, String endDate,
                                                  String sortBy, String sortOrder);

    int addOpportunities(Opportunities opportunities);//新建商机

    int updateOpportunities(Opportunities opportunities);//更改商机状态

}
