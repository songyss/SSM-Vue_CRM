package com.csi.controller.CustomerFollows;

import com.csi.domain.CustomerFollows;
import com.csi.service.CustomerFollowsService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customerFollows")
public class CustomerFollowsController {

    @Autowired
    private CustomerFollowsService customerFollowsService;

    @GetMapping("/allList")
    public R getAllCustomerFollows() {
        List<CustomerFollows> allCustomerFollows = customerFollowsService.getAllCustomerFollows();
        if (allCustomerFollows != null) {
            return R.ok(allCustomerFollows);
        } else {
            return R.error();
        }
    }

    //根据跟进时间筛选客户
    @GetMapping("/personalCustomerByTime")
    public R getPersonalCustomerByTime(@RequestParam("startTime")String startTime, @RequestParam("endTime")String endTime) {
        List<CustomerFollows> customers = customerFollowsService.getPersonalCustomerByTime(startTime,endTime);

        if (customers != null) {
            return R.ok(customers);
        } else {
            return R.error();
        }
    }

    @PutMapping("/add")
    public R addCustomerFollows(@RequestBody CustomerFollows customerFollows) {
        int i = customerFollowsService.addCustomerFollows(customerFollows);
        if (i > 0) {
            return R.ok(i);
        } else {
            return R.error();
        }
    }

    @PatchMapping("/update")
    public R updateCustomerFollows(@RequestBody CustomerFollows customerFollows) {
        int i = customerFollowsService.updateCustomerFollows(customerFollows);
        if (i > 0) {
            return R.ok(i);
        } else {
            return R.error();
        }
    }
}
