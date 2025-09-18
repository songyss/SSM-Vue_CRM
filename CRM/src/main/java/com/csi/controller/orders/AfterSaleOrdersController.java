package com.csi.controller.orders;

import com.csi.domain.AfterSaleOrder;
import com.csi.service.AfterSaleOrderService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/afterSaleOrders")
public class AfterSaleOrdersController {
    @Autowired
    private AfterSaleOrderService afterSaleOrderService;

    @GetMapping("/getAfterSaleOrderByStatus")
    public R getAfterSaleOrderByStatus() {
        AfterSaleOrder afterSaleOrder = afterSaleOrderService.getAfterSaleOrderByStatus(2);
        if (afterSaleOrder != null) {
            return R.ok(afterSaleOrder);
        } else {
            return R.error();
        }
    }

    @GetMapping("/getAfterSaleOrderByOrderNumber")
    public R getAfterSaleOrderByOrderNumber(int orderNumber) {
        AfterSaleOrder afterSaleOrder = afterSaleOrderService.getAfterSaleOrderByOrderNumber(orderNumber);
        if (afterSaleOrder != null) {
            return R.ok(afterSaleOrder);
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
