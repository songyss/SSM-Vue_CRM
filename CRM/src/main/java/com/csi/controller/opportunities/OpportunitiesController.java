package com.csi.controller.opportunities;

import com.csi.domain.Opportunities;
import com.csi.service.OpportunitiesService;
import com.csi.util.R;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        int i = opportunitiesService.addOpportunities(opportunities);
        if (i > 0) {
            return R.ok(i);
        } else {
            return R.error();
        }
    }

    @PatchMapping("/update")
    public R updateOpportunity(@RequestBody Opportunities opportunities) {
        int i = opportunitiesService.updateOpportunities(opportunities);
        if (i > 0) {
            return R.ok(i);
        } else {
            return R.error();
        }
    }

}
