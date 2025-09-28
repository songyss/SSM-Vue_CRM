package com.csi.controller.orders;

import com.csi.annotation.OperateLog;
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
    @OperateLog(operation = "添加订单",targetType = "订单")
    public R addOrders(@RequestBody Orders order) {
        int i = ordersService.addOrders(order);
        if (i > 0) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }

    @PatchMapping("/update")
    @OperateLog(operation = "更新订单",targetType = "订单")
    public R updateOrdersStatus(@RequestBody Orders order) {
        try {
            // 添加日志以便调试
            System.out.println("接收到订单更新请求: " + order);
            
            // 检查必要字段
            if (order == null) {
                return R.message("订单对象不能为空");
            }
            
            if (order.getId() == null) {
                return R.message("订单ID不能为空");
            }
            
            if (order.getOrderStatus() == null) {
                return R.message("订单状态不能为空");
            }
            
            int i = ordersService.updateOrdersStatus(order);
            if (i > 0) {
                return R.ok(i);
            } else {
                return R.message("订单更新失败");
            }
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误: " + e.getMessage());
            e.printStackTrace();
            return R.message(e.getMessage());
        } catch (Exception e) {
            System.err.println("更新订单时发生错误: " + e.getMessage());
            e.printStackTrace();
            return R.message("更新订单时发生错误: " + e.getMessage());
        }
    }

    @GetMapping("/personalList")
    @OperateLog(operation = "查询个人订单",targetType = "订单")
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
    @OperateLog(operation = "查询所有订单",targetType = "订单")
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
    @OperateLog(operation = "分页查询订单列表",targetType = "订单")
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
    @OperateLog(operation = "根据条件分页查询订单列表",targetType = "订单")
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
