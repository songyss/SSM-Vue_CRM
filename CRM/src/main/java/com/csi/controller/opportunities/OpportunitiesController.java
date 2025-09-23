package com.csi.controller.opportunities;

import com.csi.domain.Opportunities;
import com.csi.service.OpportunitiesService;
import com.csi.util.R;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/opportunities")
public class OpportunitiesController {

    @Autowired
    private OpportunitiesService opportunitiesService;

    @GetMapping("/allList")
    public R getOpportunities() {
        java.util.List<Opportunities> opportunities = opportunitiesService.getOpportunities();
        if (opportunities != null) {
            return R.ok(opportunities);
        } else {
            return R.error();
        }
    }

    // 修改分页查询接口，添加排序支持
    @GetMapping("/page")
    public R getOpportunitiesByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                   @RequestParam(value = "customerName", required = false) String customerName,
                                   @RequestParam(value = "stage", required = false) Integer stage,
                                   @RequestParam(value = "startDate", required = false) String startDate,
                                   @RequestParam(value = "endDate", required = false) String endDate,
                                   @RequestParam(value = "sortBy", required = false) String sortBy,
                                   @RequestParam(value = "sortOrder", required = false) String sortOrder) {
        try {
            PageInfo<Opportunities> pageInfo = opportunitiesService.getOpportunitiesByPage(
                page, size, customerName, stage, startDate, endDate, sortBy, sortOrder);
            return R.ok(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    @PutMapping("/add")
    public R addOpportunity(@RequestBody Opportunities opportunities) {
        try {
            // 设置创建时间和更新时间
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (opportunities.getCreateTime() == null || opportunities.getCreateTime().isEmpty()) {
                opportunities.setCreateTime(currentTime);
            }
            opportunities.setUpdateTime(currentTime);
            
            int result = opportunitiesService.addOpportunities(opportunities);
            if (result > 0) {
                return R.okMessage("商机添加成功");
            } else {
                return R.message("商机添加失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.message("添加商机时发生错误: " + e.getMessage());
        }
    }

    @PatchMapping("/update")
    public R updateOpportunity(@RequestBody Opportunities opportunities) {
        try {
            // 设置更新时间
            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            opportunities.setUpdateTime(currentTime);
            
            int result = opportunitiesService.updateOpportunities(opportunities);
            if (result > 0) {
                return R.okMessage("商机更新成功");
            } else {
                return R.message("商机更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.message("更新商机时发生错误: " + e.getMessage());
        }
    }

}
