package com.csi.service;

import com.csi.domain.DashboardStats;
import com.csi.domain.DashboardCompleteData;

public interface DashboardService {

    DashboardStats getDashboardStats();
    
    DashboardCompleteData getDashboardCompleteData();
}
