package com.csi.controller.emergency;

import com.csi.domain.SalesEmergency;
import com.csi.service.SalesEmergencyService;
import com.csi.util.OptionUtils;
import com.csi.util.R;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/salesEmergency")
public class SalesEmergencyController {

    @Autowired
    private SalesEmergencyService salesEmergencyService ;

//    销售人员填写突发状况
    @PostMapping
    public R createEmergency(@RequestBody SalesEmergency emergency,
                             @RequestParam Integer empId) {
        try {
            salesEmergencyService.createEmergency(emergency, empId);
            return R.ok("创建成功");
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    //员工提交审批
    @PostMapping("/{id}/submit")
    public R submitForApproval(@PathVariable Integer id,
                               @RequestParam Integer empId) {
        try {
            salesEmergencyService.submitForApproval(id, empId);
            return R.ok("提交成功");
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//    经理审批通过
    @GetMapping("/{id}/approve")
    public R approve(
            @PathVariable Integer id,
            @RequestParam Integer managerId){
        try {
            salesEmergencyService.approve(id,managerId);
            return  R.ok("审批通过");
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//    审批拒绝
    @PostMapping("/{id}/reject")
    public R reject(
            @PathVariable Integer id ,
            @RequestParam Integer managerId){
        try {
            salesEmergencyService.reject(id,managerId);
            return R.ok("审批拒绝");
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//  查看自己创建的突发状况
    @GetMapping("/creator/{empId}")
    public R listByCreator(
            @PathVariable Integer empId){
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listByCreator(empId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//    经理查看待审批信息
    @GetMapping("/pending")
    public R listPending(){
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listPending();
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//        查看指定经理的待审批列表
    @GetMapping("/pending/{managerId}")
    public R getPendingByManager(
            @PathVariable Integer managerId){
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.getPendingEmergenciesByManager(managerId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//    根据ID查看详情
    @GetMapping("/{id}")
    public R getById(
            @PathVariable Integer id
    ){
        try {
            SalesEmergency emergency = salesEmergencyService.getById(id);
            return R.ok(emergency);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//    经理查看需要处理的突发情况
    @GetMapping("/handler/{handlerId}")
    public R listByHandler(
            @PathVariable Integer handlerId) {
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listByHandler(handlerId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

//        条件查询突发情况
    @GetMapping("/search")
    public R listByCondition(
            @RequestParam(required = false) Integer salespersonId,
            @RequestParam(required = false) Integer handlerId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer type,
            @RequestParam(required = false) Integer urgency,
            @RequestParam(required = false) Integer customerId) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (salespersonId != null) params.put("salespersonId", salespersonId);
            if (handlerId != null) params.put("handlerId", handlerId);
            if (status != null) params.put("status", status);
            if (type != null) params.put("type", type);
            if (urgency != null) params.put("urgency", urgency);
            if (customerId != null) params.put("customerId", customerId);

            List<SalesEmergency> emergencies = salesEmergencyService.listByCondition(params);
            return R.ok(emergencies);
        }catch (Exception e) {
                return R.message(e.getMessage());
            }
        }

//        获取统计信息
    @GetMapping("/stats/{salespersonId}")
    public R getStats(
            @PathVariable Integer salespersonId){
        try {
            Map<String,Integer> stats = salesEmergencyService.getStatsBySalesperson(salespersonId);
            return R.ok(stats);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }
    /**
     * 获取风险等级选项
     */
    @GetMapping("/options/risk-levels")
    public R getRiskLevelOptions() {
        try {
            Map<String, String> options = OptionUtils.getRiskLevelOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 获取紧急程度选项
     */
    @GetMapping("/options/urgency-levels")
    public R getUrgencyOptions() {
        try {
            Map<Integer, String> options = OptionUtils.getUrgencyOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 获取类型选项
     */
    @GetMapping("/options/types")
    public R getTypeOptions() {
        try {
            Map<String, String> options = OptionUtils.getTypeOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 获取所有选项
     */
    @GetMapping("/options/all")
    public R getAllOptions() {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("riskLevels", OptionUtils.getRiskLevelOptions());
            options.put("urgencyLevels", OptionUtils.getUrgencyOptions());
            options.put("types", OptionUtils.getTypeOptions());
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }
}
