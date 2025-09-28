package com.csi.aspect;

import com.csi.annotation.OperateLog;
import com.csi.domain.OperateLogs;
import com.csi.mapper.OperationLogsMapper;
import com.csi.util.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 优化后的全接口操作日志AOP切面：
 * 1. 优化无参数场景的日志展示
 * 2. 增强参数解析的健壮性
 * 3. 提升日志内容的可读性
 */
@Component
@Aspect
public class GlobalOperateLogAspect {

    @Autowired
    private OperationLogsMapper operationLogsMapper;

    // 切点：拦截所有标注@OperateLog的方法
    @Pointcut("@annotation(com.csi.annotation.OperateLog)")
    public void globalOperateLogPointcut() {}

    // 后置通知：接口执行成功后生成日志
    @AfterReturning("globalOperateLogPointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        System.out.println("进入日志记录切面==================================================");
        try {
            // 1. 获取请求上下文（IP、Token等）
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                System.out.println("非HTTP请求环境，跳过日志记录");
                return;
            }
            HttpServletRequest request = attributes.getRequest();

            // 2. 解析注解信息
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            OperateLog operateLog = method.getAnnotation(OperateLog.class);

            // 校验注解必填属性
            if (!StringUtils.hasText(operateLog.operation()) || !StringUtils.hasText(operateLog.targetType())) {
                System.err.println("日志注解缺少必填属性，方法：" + method.getName());
                return;
            }

            // 3. 解析操作人信息
            Integer operatorId = parseOperatorId(request);

            // 4. 解析参数信息（优化无参数场景的处理）
            ParameterInfo paramInfo = parseParameterInfo(joinPoint, method);

            // 5. 封装日志对象
            OperateLogs log = new OperateLogs();
            log.setOperatorId(operatorId);
            log.setOperation(operateLog.operation());
            log.setTargetType(operateLog.targetType());
            log.setTargetId(paramInfo.targetId);
            log.setIpAddress(getRealIp(request)); // 优化IP获取逻辑
            log.setOldValue(paramInfo.oldValue);
            log.setNewValue(paramInfo.newValue);

            // 6. 保存日志
            operationLogsMapper.insert(log);
            System.out.println("操作日志记录成功: " + operateLog.operation() + " | 对象ID: " +
                    (paramInfo.targetId != null ? paramInfo.targetId : "无"));

        } catch (Exception e) {
            // 日志记录失败不影响主业务
            System.err.println("记录操作日志失败: " + e.getMessage());
        }
    }

    /**
     * 解析操作人ID（提取Token中的用户信息）
     */
    private Integer parseOperatorId(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                Map<String, Object> userInfo = JwtTokenUtils.parseToken(token.substring(7));
                return (Integer) userInfo.get("userId");
            }
        } catch (Exception e) {
            System.err.println("解析操作人信息失败: " + e.getMessage());
        }
        return null; // 匿名操作或Token解析失败时为null
    }

    /**
     * 解析参数信息（封装为内部类，使代码更清晰）
     */
    private ParameterInfo parseParameterInfo(JoinPoint joinPoint, Method method) {
        ParameterInfo info = new ParameterInfo();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();

        // 处理无参数场景
        if (args == null || args.length == 0) {
            info.newValue = "无参数";
            return info;
        }

        // 有参数时解析
        StringBuilder paramDetails = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String paramName = (i < parameters.length) ? parameters[i].getName() : "arg" + i;

            // 记录参数详情（限制长度，避免日志过大）
            String argStr = arg != null ? arg.toString() : "null";
            if (argStr.length() > 200) {
                argStr = argStr.substring(0, 200) + "..."; // 超长截断
            }
            paramDetails.append(paramName).append("=").append(argStr).append(", ");

            // 提取目标ID（单个ID）
            if (info.targetId == null && arg instanceof Integer) {
                info.targetId = (Integer) arg;
            }
            // 提取目标ID（多个ID列表）
            else if (info.targetId == null && arg instanceof List) {
                List<?> list = (List<?>) arg;
                if (!list.isEmpty() && list.get(0) instanceof Integer) {
                    String ids = list.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(","));
                    // 只记录第一个ID，或标记为多ID
                    info.targetId = list.size() == 1 ? (Integer) list.get(0) : -1;
                }
            }
            // 提取新旧值（Map参数）
            else if (arg instanceof Map) {
                Map<?, ?> paramMap = (Map<?, ?>) arg;
                if (paramMap.containsKey("oldValue")) {
                    info.oldValue = String.valueOf(paramMap.get("oldValue"));
                }
                if (paramMap.containsKey("newValue")) {
                    info.newValue = String.valueOf(paramMap.get("newValue"));
                }
            }
        }

        // 处理参数详情（移除末尾逗号）
        String paramStr = paramDetails.length() > 0
                ? paramDetails.substring(0, paramDetails.length() - 2)
                : "无参数";

        // 组合新值信息
        if (StringUtils.hasText(info.newValue)) {
            info.newValue += " | 参数: " + paramStr;
        } else {
            info.newValue = "参数: " + paramStr;
        }

        return info;
    }

    /**
     * 获取真实IP地址（处理代理场景）
     */
    private String getRealIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多代理场景取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 内部类：封装参数解析结果
     */
    private static class ParameterInfo {
        Integer targetId; // 操作对象ID
        String oldValue = ""; // 旧值
        String newValue = ""; // 新值
    }
}