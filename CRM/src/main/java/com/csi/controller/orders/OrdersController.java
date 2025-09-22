package com.csi.controller.orders;

import com.csi.domain.Orders;
import com.csi.service.OrdersService;
import com.csi.util.R;
import com.github.pagehelper.PageInfo;
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
    public R updateOrdersStatus(Orders order) {
        int i = ordersService.updateOrdersStatus(order);
        if (i > 0) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/personalList")
    public R selectPersonalOrders(@RequestParam("id") int id) {
        List<Orders> orders = ordersService.selectPersonalOrders(id);
        if (orders.size() > 0) {
            return R.ok(orders) ;
        } else {
            return R.error() ;
        }
    }
    
    /**
     * 查询所有订单
     */
    @GetMapping("/list")
    public R selectAllOrders() {
        List<Orders> orders = ordersService.selectAllOrders();
        if (orders.size() > 0) {
            return R.ok(orders);
        } else {
            return R.error();
        }
    }
    
    /**
     * 分页查询订单列表
     */
    @GetMapping("/page")
    public R selectOrdersByPage(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            PageInfo<Orders> pageInfo = ordersService.selectOrdersByPage(page, size);
            return R.ok(pageInfo);
        } catch (Exception e) {
            return R.error();
        }
    }
    
    /**
     * 根据条件分页查询订单列表
     */
    @GetMapping("/pageByCondition")
    public R selectOrdersByCondition(@RequestParam(value = "orderNo", required = false) String orderNo,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "startDate", required = false) String startDate,
                                     @RequestParam(value = "endDate", required = false) String endDate,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            PageInfo<Orders> pageInfo = ordersService.selectOrdersByCondition(
                orderNo, status, startDate, endDate, page, size);
            return R.ok(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}
