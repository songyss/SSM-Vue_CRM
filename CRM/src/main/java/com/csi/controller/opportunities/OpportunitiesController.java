package com.csi.controller.opportunities;

import com.csi.domain.Opportunities;
import com.csi.service.OpportunitiesService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/opportunities")
public class OpportunitiesController {

    @Autowired
    private OpportunitiesService opportunitiesService;

    @GetMapping("/allList")
    public R getOpportunities() {
        List<Opportunities> opportunities = opportunitiesService.getOpportunities();
        if (opportunities != null) {
            return R.ok(opportunities);
        } else {
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
