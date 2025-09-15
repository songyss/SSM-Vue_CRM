package com.csi.controller.marketDepartment;

import com.csi.domain.Customer;
import com.csi.domain.PromotionPlans;
import com.csi.service.CustomerService;
import com.csi.service.MarketingService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 市场部
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/marketing")
public class MarketingController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private MarketingService marketingService;

    /**
     * 市场部录入客户信息
     *
     * @return
     */
    @PutMapping("/savecustomer")
    public R saveCustomer(@RequestBody Customer customer) {
        int i = customerService.addCustomer(customer);
        if (i == 1) {
            return R.ok(i);
        } else {
            return R.message("电话号码已存在");
        }
    }

    /**
     * 活动计划提交
     *
     * @return
     *
     */
    @PutMapping("/savePromotionPlans")
    public R savePromotionPlans(@RequestBody PromotionPlans promotionPlans) {


        int i = marketingService.savePromotionPlans(promotionPlans);
        if (i == 1) {
            return R.ok(i);
        } else {
            return R.error();
        }
    }

    /**
     * 获取活动计划
     *
     * @return
     */
    @GetMapping("/getPromotionPlans")
    public R getPromotionPlans() {
        List<PromotionPlans> promotionPlans = marketingService.getPromotionPlans();
        if (promotionPlans != null) {
            return R.ok(promotionPlans);
        } else {
            return R.error();
        }
    }




}
