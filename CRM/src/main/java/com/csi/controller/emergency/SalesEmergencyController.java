package com.csi.controller.emergency;

import com.csi.domain.Employee;
import com.csi.domain.SalesEmergency;
import com.csi.service.EmployeeService;
import com.csi.service.SalesEmergencyService;
import com.csi.util.OptionUtils;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // 放开前端端口的跨域
@RequestMapping("/api/salesEmergency")
public class SalesEmergencyController {

    @Autowired
    private SalesEmergencyService salesEmergencyService;

    @Autowired
    private EmployeeService employeeService;

    // 销售人员填写突发状况
    @PostMapping
    public R createEmergency(@RequestBody SalesEmergency emergency,
                             @RequestParam("empId") Integer empId) {

        // deadline 如果是空字符串，改为 null
        if (emergency.getDeadline() != null && "".equals(emergency.getDeadline().toString().trim())) {
            emergency.setDeadline(null);
        }

        // 1. 根据当前员工ID查直属上司
        Employee leader = employeeService.findLeaderByEmployeeId(empId);
        if (leader == null) {
            return R.error().message("未找到直属上司，无法提交");
        }

        // 2. 生成唯一编号
        String emergencyNo = "SE" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        emergency.setEmergencyNo(emergencyNo);

        // 3. 设置提交人、提报人、状态、当前处理人
        emergency.setSalespersonId(empId);
        emergency.setProposedBy(empId);
        emergency.setStatus("DRAFT");
        emergency.setCurrentHandlerId(leader.getId());

        // 4. 保存突发事件
        salesEmergencyService.createEmergency(emergency, empId);

        // 5. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("emergencyId", emergency.getId());
        result.put("leaderName", leader.getName());
        result.put("emergencyNo", emergencyNo);
        result.put("message", "新增成功");

        return R.ok(result);
    }

    // 审批拒绝
    @PostMapping("/{id}/reject")
    public R reject(@PathVariable Integer id,
                    @RequestParam Integer managerId) {
        try {
            salesEmergencyService.reject(id, managerId);
            return R.ok("审批拒绝");
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 查看自己创建的突发状况
    @GetMapping("/creator/{empId}")
    public R listByCreator(@PathVariable Integer empId) {
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listByCreator(empId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 经理查看待审批信息
    @GetMapping("/pending")
    public R listPending() {
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listPending();
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 查看指定经理的待审批列表
    @GetMapping("/pending/{managerId}")
    public R getPendingByManager(@PathVariable Integer managerId) {
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.getPendingEmergenciesByManager(managerId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 根据ID查看详情
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        try {
            SalesEmergency emergency = salesEmergencyService.getById(id);
            return R.ok(emergency);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }


    // 经理查看需要处理的突发情况
    @GetMapping("/handler/{handlerId}")
    public R listByHandler(@PathVariable Integer handlerId) {
        try {
            List<SalesEmergency> emergencies = salesEmergencyService.listByHandler(handlerId);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 条件查询突发情况
    @GetMapping("/search")
    public R listByCondition(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                             @RequestParam(value = "pageSize", required = false) Integer pageSize,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "status", required = false) String status,
                             @RequestParam(value = "employeeId") Integer employeeId) {

        try {
            Map<String, Object> params = new HashMap<>();
            if (pageNum != null) params.put("pageNum", pageNum);
            if (pageSize != null) params.put("pageSize", pageSize);
            if (title != null && !title.isEmpty()) params.put("title", title);
            if (type != null && !type.isEmpty()) params.put("type", type);
            if (status != null && !status.isEmpty()) params.put("status", status);
            if (employeeId != null) params.put("employeeId", employeeId);

            List<SalesEmergency> emergencies = salesEmergencyService.listByCondition(params);
            return R.ok(emergencies);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }


    // 获取统计信息
    @GetMapping("/stats/{salespersonId}")
    public R getStats(@PathVariable Integer salespersonId) {
        try {
            Map<String, Integer> stats = salesEmergencyService.getStatsBySalesperson(salespersonId);
            return R.ok(stats);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 获取风险等级选项
    @GetMapping("/options/risk-levels")
    public R getRiskLevelOptions() {
        try {
            Map<String, String> options = OptionUtils.getRiskLevelOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 获取紧急程度选项
    @GetMapping("/options/urgency-levels")
    public R getUrgencyOptions() {
        try {
            Map<Integer, String> options = OptionUtils.getUrgencyOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 获取类型选项
    @GetMapping("/options/types")
    public R getTypeOptions() {
        try {
            Map<String, String> options = OptionUtils.getTypeOptions();
            return R.ok(options);
        } catch (Exception e) {
            return R.message(e.getMessage());
        }
    }

    // 获取所有选项
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

    /**
     * 员工提交审批
     */
    @PutMapping("/{id}/submit")
    public R submitForApproval(@PathVariable("id") Integer id,
                               @RequestParam("empId") Integer empId) {
        try {
            salesEmergencyService.submitForApproval(id, empId);
            return R.okMessage("提交成功");
        } catch (Exception e) {
            return R.message("提交失败: " + e.getMessage());
        }
    }

    /**
     * 经理审批通过
     */
    @PutMapping("/{id}/approve")
    public R approve(@PathVariable("id") Integer id,
                     @RequestParam("managerId") Integer managerId) {
        try {
            salesEmergencyService.approve(id, managerId);
            return R.okMessage("批准成功");
        } catch (Exception e) {
            return R.message("批准失败: " + e.getMessage());
        }
    }

    /**
     * 经理审批拒绝
     */
    @PutMapping("/{id}/reject")
    public R reject(@PathVariable("id") Integer id,
                    @RequestParam("managerId") Integer managerId,
                    @RequestParam("reason") String reason) {
        try {

            SalesEmergency emergency = salesEmergencyService.getById(id);
            if (emergency != null) {
                emergency.setDescription(reason);
                salesEmergencyService.updateById(emergency);
            }
            salesEmergencyService.reject(id, managerId);
            return R.okMessage("拒绝成功");
        } catch (Exception e) {
            return R.message("拒绝失败: " + e.getMessage());
        }
    }

    /**
     * 标记为已解决
     */
    @PutMapping("/{id}/solve")
    public R markAsSolved(@PathVariable("id") Integer id,
                          @RequestParam("empId") Integer empId,
                          @RequestBody Map<String, String> requestBody) {
        try {
            String solutionDescription = requestBody.get("solutionDescription");
            salesEmergencyService.markAsSolved(id, empId, solutionDescription);
            return R.okMessage("标记成功");
        } catch (Exception e) {
            return R.message("标记失败: " + e.getMessage());
        }
    }


}
