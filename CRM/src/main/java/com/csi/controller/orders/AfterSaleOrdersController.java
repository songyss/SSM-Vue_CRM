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
    public R getAfterSaleOrderByStatus(@RequestParam(value = "afterSaleStatus",defaultValue = "1") Integer afterSaleStatus){
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByStatus(afterSaleStatus);
        // 正确的判断逻辑：检查列表是否不为空且包含元素
        if (afterSaleOrders.size()>0) {
            return R.ok(afterSaleOrders);
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/getAfterSaleOrderByOrderNumber")
    public R getAfterSaleOrderByOrderNumber(int orderNumber) {
        List<AfterSaleOrder> afterSaleOrders = afterSaleOrderService.getAfterSaleOrderByOrderNumber(orderNumber);
        if (afterSaleOrders.size()>0) {
            return R.ok(afterSaleOrders);
        } else {
            return R.error();
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
