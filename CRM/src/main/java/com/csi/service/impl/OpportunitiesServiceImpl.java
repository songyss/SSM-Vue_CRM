package com.csi.service.impl;

import com.csi.domain.Opportunities;
import com.csi.mapper.OpportunitiesMapper;
import com.csi.service.OpportunitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OpportunitiesServiceImpl implements OpportunitiesService {

    @Autowired
    private OpportunitiesMapper opportunitiesMapper;

    @Override
    public List<Opportunities> getOpportunities() {
        List<Opportunities> opportunities = opportunitiesMapper.getOpportunities();
        return opportunities;
    }


    @Override
    public int addOpportunities(Opportunities opportunities) {
        int i = opportunitiesMapper.addOpportunities(opportunities);
        return i;
    }

    @Override
    public int updateOpportunities(Opportunities opportunities) {
        int i = opportunitiesMapper.updateOpportunities(opportunities);
        return i;
    }



}
