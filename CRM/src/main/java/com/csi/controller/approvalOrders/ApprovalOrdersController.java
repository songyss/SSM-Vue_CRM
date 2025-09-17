package com.csi.controller.approvalOrders;

import com.csi.domain.ApprovalOrders;
import com.csi.service.ApprovalOrdersService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批订单控制器
 * 提供审批订单的增删改查功能，包括员工提交申请、经理审批等操作
 */
@RestController
@CrossOrigin
@RequestMapping("/approvalOrders")
public class ApprovalOrdersController {

    @Autowired
    private ApprovalOrdersService approvalOrdersService;

    /**
     * 添加新的审批订单
     * @param approvalOrders 审批订单信息
     * @return 添加结果，成功返回订单ID，失败返回错误信息
     */
    @PutMapping("/add")
    public R insert(@RequestBody ApprovalOrders approvalOrders) {
        // 设置默认状态为1（待审批）
        approvalOrders.setStatus(1);
        // 设置创建时间
        if (approvalOrders.getCreateTime() == null || approvalOrders.getCreateTime().isEmpty()) {
            approvalOrders.setCreateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        }
        approvalOrders.setUpdateTime(approvalOrders.getCreateTime());
        
        int insert = approvalOrdersService.insert(approvalOrders);
        if (insert > 0) {
            return R.ok(insert) ;
        } else {
            return R.error() ;
        }
    }

    /**
     * 审批订单（通过或驳回）
     * @param approvalOrders 审批订单信息，包括ID、状态和反馈
     * @return 审批结果，成功返回更新记录数，失败返回错误信息
     */
    @PatchMapping("/approve")
    public R approve(@RequestBody ApprovalOrders approvalOrders) {
        // 设置审批人ID为2002（经理）
        approvalOrders.setApproverId(2002);
        // 设置更新时间
        approvalOrders.setUpdateTime(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        
        int update = approvalOrdersService.updateStatus(approvalOrders);
        if (update > 0) {
            return R.ok(update) ;
        } else {
            return R.error() ;
        }
    }

    /**
     * 获取所有审批订单列表
     * @return 所有审批订单列表
     */
    @GetMapping("/list")
    public R selectAll() {
        List<ApprovalOrders> approvalOrders = approvalOrdersService.selectAll();
        return R.ok(approvalOrders) ;
    }

    /**
     * 根据审批人ID查询审批订单列表
     * @param approverId 审批人ID
     * @return 审批订单列表
     */
    @GetMapping("/personal")
    public R selectPersonal(@RequestParam("approverId") int approverId) {
        List<ApprovalOrders> approvalOrders = approvalOrdersService.selectByApproverId(approverId);
        return R.ok(approvalOrders) ;
    }

    /**
     * 根据申请人ID查询审批订单列表
     * @param applicantId 申请人ID
     * @return 审批订单列表
     */
    @GetMapping("/byApplicant")
    public R selectByApplicant(@RequestParam("applicantId") int applicantId) {
        List<ApprovalOrders> approvalOrders = approvalOrdersService.selectByApplicantId(applicantId);
        return R.ok(approvalOrders) ;
    }
    
    /**
     * 获取待审批的订单数量
     * @return 待审批订单数量
     */
    @GetMapping("/pendingCount")
    public R getPendingApprovalCount() {
        // 获取所有待审批的订单 (状态为1表示待审批)
        List<ApprovalOrders> allOrders = approvalOrdersService.selectAll();
        
        // 统计待处理订单数量
        long pendingCount = allOrders.stream()
                .filter(order -> order.getStatus() != null && order.getStatus() == 1) // 1表示待审批状态
                .count();
        
        return R.ok(pendingCount);
    }

}
