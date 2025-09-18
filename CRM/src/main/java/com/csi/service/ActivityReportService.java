package com.csi.service;

import com.csi.domain.ActivityReports;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityReportService {

    // 活动报告
    int addActivityReport(ActivityReports report);

    // 查询所有活动报告
    List<ActivityReports> getAllActivityReport();

    // 按名称查询活动报告
    List<ActivityReports> getActivityReportByName(@Param("name") String name);

    // 按状态查询活动报告
    List<ActivityReports> getActivityReportByStatus(@Param("status")  int status);

    // 更改活动报告
    int updateActivityReport( ActivityReports report);

    int deleteActivityReport(int id);

    // 按id查询活动报告
    ActivityReports getActivityReportById(int id);
}
