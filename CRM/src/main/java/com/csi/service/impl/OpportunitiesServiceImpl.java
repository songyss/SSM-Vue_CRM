package com.csi.service.impl;

import com.csi.domain.Opportunities;
import com.csi.domain.Orders;
import com.csi.mapper.OpportunitiesMapper;
import com.csi.mapper.OrdersMapper;
import com.csi.service.OpportunitiesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OpportunitiesServiceImpl implements OpportunitiesService {

    @Autowired
    private OpportunitiesMapper opportunitiesMapper;
    
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Opportunities> getOpportunities() {
        return opportunitiesMapper.getOpportunities();
    }

    @Override
    public PageInfo<Opportunities> getOpportunitiesByPage(int page, int size, String customerName,
                                                         Integer stage, String startDate, String endDate,
                                                         String sortBy, String sortOrder) {
        // 开启分页
        PageHelper.startPage(page, size);
        
        // 处理排序
        if (sortBy != null && !sortBy.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            // 将前端传来的字段名转换为数据库字段名
            String dbSortBy = convertSortByField(sortBy);
            PageHelper.orderBy(dbSortBy + " " + sortOrder);
        }
        
        // 查询数据，不依赖不存在的opportunity_stage表
        List<Opportunities> opportunities = opportunitiesMapper.selectOpportunitiesByCondition(
            customerName, stage, startDate, endDate);
        // 封装分页结果
        return new PageInfo<>(opportunities);
    }

    @Override
    @Transactional
    public int addOpportunities(Opportunities opportunities) {
        return opportunitiesMapper.addOpportunities(opportunities);
    }

    @Override
    @Transactional
    public int updateOpportunities(Opportunities opportunities) {
        // 获取更新前的商机信息
        Opportunities oldOpportunity = opportunitiesMapper.getOpportunityById(opportunities.getId());
        
        // 更新商机
        int result = opportunitiesMapper.updateOpportunities(opportunities);
        
        // 检查是否从其他阶段更新为"赢单"(阶段5)
        if (oldOpportunity != null && oldOpportunity.getStage() != 5 && opportunities.getStage() == 5) {
            // 创建订单
            createOrderFromOpportunity(opportunities);
        }
        
        return result;
    }
    
    /**
     * 根据赢单的商机创建订单
     * @param opportunity 赢单的商机
     */
    private void createOrderFromOpportunity(Opportunities opportunity) {
        try {
            Orders order = new Orders();
            order.setOrderNumber(generateOrderNumber()); // 生成订单编号
            order.setOpportunityId(opportunity.getId()); // 关联商机ID
            order.setCustomerId(opportunity.getCustomerId()); // 客户ID
            order.setTotalAmount(opportunity.getAmount()); // 订单总金额
            order.setSignedDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date())); // 签约日期
            order.setOrderStatus(1); // 默认订单状态为"待支付"
            order.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); // 创建时间
            
            // 调用订单服务创建订单
            ordersMapper.addOrders(order);
        } catch (Exception e) {
            // 记录日志，但不中断商机更新流程
            e.printStackTrace();
        }
    }
    
    /**
     * 生成订单编号
     * @return 订单编号
     */
    private String generateOrderNumber() {
        // 生成订单编号：ORD + 当前时间戳
        return "ORD" + System.currentTimeMillis();
    }
    
    /**
     * 将前端字段名转换为数据库字段名
     * @param sortBy 前端字段名
     * @return 数据库字段名
     */
    private String convertSortByField(String sortBy) {
        switch (sortBy) {
            case "name":
                return "o.name";
            case "customerName":
                return "c.name";
            case "amount":
                return "o.amount";
            case "stage":
                return "o.stage";
            case "probability":
                return "o.probability";
            case "expectedCloseDate":
                return "o.expected_close_date";
            case "assigneeName":
                return "e.name";
            default:
                return "o.create_time"; // 默认按创建时间排序
        }
    }
}
