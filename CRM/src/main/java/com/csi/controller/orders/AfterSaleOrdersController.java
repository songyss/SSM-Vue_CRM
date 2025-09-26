package com.csi.controller.orders;

import com.csi.domain.AfterSaleOrder;
import com.csi.service.AfterSaleOrderService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/afterSaleOrders")
public class AfterSaleOrdersController {
    @Autowired
    private AfterSaleOrderService afterSaleOrderService;

    @GetMapping("/getAfterSaleOrderByStatus")
    public R getAfterSaleOrderByStatus(){
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByStatus();
        // 正确的判断逻辑：检查列表是否不为空且包含元素
        if (afterSaleOrders.size()>=0) {
            return R.ok(afterSaleOrders);
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/getAfterSaleOrderByOrderNumber")
    public R getAfterSaleOrderByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByOrderNumber(orderNumber);
        if (afterSaleOrders.size()>0) {
            return R.ok(afterSaleOrders);
        } else {
            return R.error();
        }
    }

    @GetMapping("/getAfterSaleOrderByDateRange")
    public R getAfterSaleOrderByDateRange(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByDateRange(startDate, endDate);

        if (afterSaleOrders.size() > 0) {
            return R.ok(afterSaleOrders);
        } else {
            return R.error();
        }
    }

    @GetMapping("/getAfterSaleOrderByCondition")
    public R getAfterSaleOrderByCondition(
            @RequestParam(value = "afterSaleStatus", required = false) Integer afterSaleStatus,
            @RequestParam(value = "orderNumber", required = false) String orderNumber,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {

        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByCondition(
                afterSaleStatus, orderNumber, startDate, endDate);

        if (afterSaleOrders != null && !afterSaleOrders.isEmpty()) {
            return R.ok(afterSaleOrders);
        } else {
            return R.okMessage("没有符合条件的售后订单");
        }
    }


    @GetMapping("/updateAfterSaleOrderStatus")
    public R updateAfterSaleOrderStatus(AfterSaleOrder afterSaleOrder) {
        int result=afterSaleOrderService.updateAfterSaleOrderStatus(afterSaleOrder);
        if (result>0){
            return R.okMessage("修改成功");
        }else {
            return R.error();
        }
    }


}
