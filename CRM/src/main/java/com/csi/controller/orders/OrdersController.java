package com.csi.controller.orders;

import com.csi.domain.Orders;
import com.csi.service.OrdersService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PutMapping("/add")
    public R addOrders(@RequestBody Orders order) {
        int i = ordersService.addOrders(order);
        if (i > 0) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }

    @PatchMapping("/update")
    public R updateOrdersStatus(@RequestParam("id") int id ,@RequestParam("status") int status) {
        int i = ordersService.updateOrdersStatus(id,status);
        if (i > 0) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/personalList")
    public R selectPersonalOrders(int id) {
        List<Orders> orders = ordersService.selectPersonalOrders(id);
        if (orders.size() > 0) {
            return R.ok(orders) ;
        } else {
            return R.error() ;
        }
    }

}
