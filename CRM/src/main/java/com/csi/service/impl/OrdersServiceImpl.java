package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.domain.Orders;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.mapper.OrdersMapper;
import com.csi.service.OrdersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现类
 * 提供订单相关的业务逻辑处理
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    
    @Autowired
    private AfterSaleOrderMapper afterSaleOrderMapper;

    /**
     * 添加新订单
     * 
     * @param order 订单对象
     * @return 添加成功的记录数
     */
    @Override
    public int addOrders(Orders order) {
        int i = ordersMapper.addOrders(order);
        return i;
    }

    /**
     * 更新订单状态
     * 当订单状态更新为已完成(状态3)时，会自动创建售后订单
     * 
     * @param order 订单对象，包含新的状态和订单ID
     * @return 更新成功的记录数
     */
    @Override
    public int updateOrdersStatus(Orders order) {
        int i = ordersMapper.updateOrdersStatus(order.getOrderStatus(),order.getId());
        // 如果订单状态为已完成(3)，则创建售后订单
        if (order.getOrderStatus()==3) {
            AfterSaleOrder afterSaleOrder = new AfterSaleOrder();
            afterSaleOrder.setOrderNumber(order.getOrderNumber());
            afterSaleOrder.setCustomerId(order.getCustomerId());
            afterSaleOrder.setTotalAmount(order.getTotalAmount());
            afterSaleOrder.setSignedDate(order.getSignedDate());
            afterSaleOrder.setAfterSaleStatus(order.getOrderStatus());
            afterSaleOrder.setFileUrl(order.getFileUrl());
            afterSaleOrder.setNotes(order.getNotes());
            afterSaleOrder.setCreateTime(order.getCreateTime());
            afterSaleOrderMapper.saveAfterSaleOrder(afterSaleOrder);
        }
        return i;
    }

    /**
     * 查询个人订单
     * 根据客户ID查询该客户的所有订单
     * 
     * @param id  客户ID
     * @return 订单列表
     */
    @Override
    public List<Orders> selectPersonalOrders(int id) {
        return ordersMapper.selectPersonalOrders(id);
    }
    
    /**
     * 查询所有订单
     * 
     * @return 所有订单列表
     */
    @Override
    public List<Orders> selectAllOrders() {
        return ordersMapper.selectAllOrders();
    }
    
    /**
     * 分页查询订单列表
     * 使用PageHelper实现分页功能
     * 
     * @param currentPage 当前页码
     * @param pageSize 每页显示条数
     * @return 分页结果，包含订单列表和分页信息
     */
    @Override
    public PageInfo<Orders> selectOrdersByPage(int currentPage, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(currentPage, pageSize);
        // 查询所有订单
        List<Orders> orders = ordersMapper.selectAllOrders();
        // 封装成分页结果返回
        return new PageInfo<>(orders);
    }
    
    /**
     * 根据条件分页查询订单列表
     * 支持按订单编号、状态、下单日期范围等条件查询
     * 
     * @param orderNo 订单编号（可选）
     * @param status 订单状态（可选）
     * @param startDate 开始日期（可选）
     * @param endDate 结束日期（可选）
     * @param currentPage 当前页码
     * @param pageSize 每页显示条数
     * @return 分页结果，包含符合条件的订单列表和分页信息
     */
    @Override
    public PageInfo<Orders> selectOrdersByCondition(String orderNo, String status, 
                                                   String startDate, String endDate, 
                                                   int currentPage, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(currentPage, pageSize);
        // 根据条件查询订单
        List<Orders> orders = ordersMapper.selectOrdersByCondition(orderNo, status, startDate, endDate);
        // 封装成分页结果返回
        return new PageInfo<>(orders);
    }
}
