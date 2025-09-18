package com.csi.controller.marketDepartment;


import com.csi.domain.ActivityReports;
import com.csi.service.ActivityReportService;
import com.csi.util.R;
import jakarta.ws.rs.PUT;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/activityReport")
public class ActivityReportController {

    @Autowired
    private ActivityReportService activityReportService;

    // 活动报告
    @PostMapping("/add")
    public R addActivityReport(@RequestBody ActivityReports report){
        int i = activityReportService.addActivityReport(report);
        return i == 1 ? R.ok("上传报告成功") : R.error();
    }


    // 查询所有活动报告
    @GetMapping("/getAll")
    public R getAllActivityReport(){
        List<ActivityReports> list = activityReportService.getAllActivityReport();
        return list != null ? R.ok(list) : R.error();
    }

    // 按名称查询活动报告
    @GetMapping("/getByName")
    public R getActivityReportByName(@RequestParam("name") String name){
        List<ActivityReports> list = activityReportService.getActivityReportByName(name);
        return list != null ? R.ok(list) : R.error();
    }

    // 按状态查询活动报告
    @GetMapping("/getByStatus")
    public R getActivityReportByStatus(@RequestParam("status") int status){
        List<ActivityReports> list = activityReportService.getActivityReportByStatus(status);
        return list != null ? R.ok(list) : R.error();
    }

    // 更改活动报告
    @PutMapping("/update")
    public R updateActivityReport(@RequestBody ActivityReports report){
        int i = activityReportService.updateActivityReport(report);
        return i == 1 ? R.ok("更改报告成功") : R.error();
    }

    @DeleteMapping("/delete")
    public R deleteActivityReport(@RequestParam("id") int id){
        int i = activityReportService.deleteActivityReport(id);
        return i == 1 ? R.ok("删除报告成功") : R.error();
    }

    // 按id查询活动报告
    @GetMapping("/getById")
    public R getActivityReportById(@RequestParam("id") int id){
        ActivityReports report = activityReportService.getActivityReportById(id);
        return report != null ? R.ok(report) : R.error();
    }
}
