package com.csi.controller.dashboard;

import com.csi.domain.DashboardCompleteData;
import com.csi.domain.DashboardStats;
import com.csi.service.DashboardService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

// DashboardController.java
@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "*") // 根据实际情况配置跨域
public class DashboardController {
    
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping("/stats")
    public R getDashboardStats() {
        try {
            // 调用服务层方法获取统计数据

            DashboardStats stats = dashboardService.getDashboardStats();
            return R.ok(stats);
        } catch (Exception e) {
            return R.error();
        }
    }
    
    /**
     * 获取完整仪表盘数据（包含所有图表数据）
     * @return
     */
    @GetMapping("/complete-data")
    public R getDashboardCompleteData() {
        try {
            DashboardCompleteData completeData = dashboardService.getDashboardCompleteData();
            return R.ok(completeData);
        } catch (Exception e) {
            return R.error();
        }
    }
}
