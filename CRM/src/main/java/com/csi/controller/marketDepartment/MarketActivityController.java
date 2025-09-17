package com.csi.controller.marketDepartment;


import com.csi.domain.MarketingActivities;
import com.csi.service.MarketActivitiesService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/marketActivity")
public class MarketActivityController {
    @Autowired
    private MarketActivitiesService marketActivitiesService;

    //添加
    @PostMapping("/add")
    public R addMarketActivity(MarketingActivities activity) {
        int i =  marketActivitiesService.addMarketActivity(activity);
        return i > 0 ? R.ok(i) : R.error();
    }

    //查询所有 活动
    @GetMapping("/getAll")
    public R  getAllActivity(){
        List<MarketingActivities> allMarketActivity = marketActivitiesService.getAllMarketActivity();
        return R.ok(allMarketActivity);
    }

    //按名称模糊查询
    @GetMapping("/getByName")
    public R getActivityByName(@RequestParam("name")String name){
        List<MarketingActivities> allMarketActivity = marketActivitiesService.getMarketActivityByName(name);
        return R.ok(allMarketActivity);
    }

    /**
    *  按状态查询(活动状态（'筹备中','进行中','已结束','已取消'）)
     */
    @GetMapping("/getByStatus")
    public R getActivityByStatus(@RequestParam("status") int status){
        List<MarketingActivities> allMarketActivity = marketActivitiesService.getMarketActivityByStatus(status);
        return R.ok(allMarketActivity);
    }

    /**
     * 修改活动
     * @param activity
     * @return
     */
    @PutMapping("/update")
    public R updateMarketActivity(@RequestBody MarketingActivities activity){
        int i = marketActivitiesService.updateMarketActivity(activity);
        return i > 0 ? R.ok(i) : R.error();
    }

    @DeleteMapping("/delete")
    public R deleteMarketActivity(@RequestParam("id") int id){
        int i = marketActivitiesService.deleteMarketActivity(id);
        return i > 0 ? R.ok(i) : R.error();
    }

    //按id查询
    @GetMapping("/getById")
    public R getActivityById(@RequestParam("id") int id){
        MarketingActivities activity = marketActivitiesService.getMarketActivityById(id);
        return R.ok(activity);
    }

}
