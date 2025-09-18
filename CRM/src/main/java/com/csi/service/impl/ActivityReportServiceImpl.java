package com.csi.service.impl;

import com.csi.domain.ActivityReports;
import com.csi.mapper.ActivityReportMapper;
import com.csi.service.ActivityReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ActivityReportServiceImpl implements ActivityReportService {

    @Autowired
    private ActivityReportMapper activityReportMapper;

    //负责人可以直接添加活动报告
    @Override
    public int addActivityReport(ActivityReports report) {
        return activityReportMapper.addActivityReport(report);
    }

    @Override
    public List<ActivityReports> getAllActivityReport() {
        return activityReportMapper.getAllActivityReport();
    }

    @Override
    public List<ActivityReports> getActivityReportByName(String name) {
        return activityReportMapper.getActivityReportByName(name);
    }

    @Override
    public List<ActivityReports> getActivityReportByStatus(int status) {
        return activityReportMapper.getActivityReportByStatus(status);
    }

    //更改活动报告
    @Override
    public int updateActivityReport(ActivityReports report) {
        return activityReportMapper.updateActivityReport(report);
    }

    @Override
    public int deleteActivityReport(int id) {
        return activityReportMapper.deleteActivityReport(id);
    }

    @Override
    public ActivityReports getActivityReportById(int id) {
        return activityReportMapper.getActivityReportById(id);
    }
}
