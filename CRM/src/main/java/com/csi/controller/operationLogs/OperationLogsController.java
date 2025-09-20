// OperationLogsController.java
package com.csi.controller.operationLogs;

import com.csi.domain.OperateLogs;
import com.csi.service.OperationLogsService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin
@RequestMapping("/api/logs")
public class OperationLogsController {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogsController.class);

    @Autowired
    private OperationLogsService operationLogsService;

    /**
     * 获取所有操作日志（分页）
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllLogs(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        try {
            PageInfo<OperateLogs> pageInfo = operationLogsService.selectLogList(page, size);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", pageInfo.getList());
            result.put("total", pageInfo.getTotal());     // 总记录数
            result.put("page", pageInfo.getPageNum());    // 当前页
            result.put("size", pageInfo.getPageSize());   // 每页大小
            result.put("pages", pageInfo.getPages());     // 总页数

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "服务器内部错误: " + e.getMessage());
            result.put("data", null);
            result.put("total", 0);
            result.put("page", 1);
            result.put("size", 10);
            result.put("pages", 0);
            return ResponseEntity.status(500).body(result);
        }
    }

    /**
     * 根据条件筛选日志（分页）
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchLogs(
            @RequestParam(value = "operatorId", required = false) Integer operatorId,
            @RequestParam(value = "operation", required = false) String operation,
            @RequestParam(value = "targetType", required = false) String targetType,
            @RequestParam(value = "targetId", required = false) Integer targetId,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {

        try {
            logger.info("根据条件搜索操作日志，参数: operatorId={}, operation={}, targetType={}, targetId={}, ipAddress={}, startTime={}, endTime={}, page={}, size={}",
                    operatorId, operation, targetType, targetId, ipAddress, startTime, endTime, page, size);

            Map<String, Object> params = new HashMap<>();
            params.put("operatorId", operatorId);
            params.put("operation", operation);
            params.put("targetType", targetType);
            params.put("targetId", targetId);
            params.put("ipAddress", ipAddress);
            params.put("startTime", startTime);
            params.put("endTime", endTime);

            PageInfo<OperateLogs> pageInfo = operationLogsService.selectByCondition(params, page, size);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 200);
            result.put("message", "搜索成功");
            result.put("data", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", pageInfo.getPageNum());
            result.put("size", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            logger.info("成功搜索操作日志，共 {} 条记录", pageInfo.getList().size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("搜索操作日志时发生异常: ", e);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 500);
            result.put("message", "服务器内部错误: " + e.getMessage());
            result.put("data", null);
            result.put("total", 0);
            result.put("page", 1);
            result.put("size", 10);
            result.put("pages", 0);

            return ResponseEntity.status(500).body(result);
        }
    }
}
