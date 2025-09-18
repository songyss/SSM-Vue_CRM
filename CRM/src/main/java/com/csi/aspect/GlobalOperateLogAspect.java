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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 全接口操作日志AOP切面：拦截所有@OperateLog标注的接口，自动生成日志
 */
@Component
@Aspect
public class GlobalOperateLogAspect {

    @Autowired
    private OperationLogsMapper operationLogsMapper;

    // 切点：拦截所有标注@OperateLog的方法（覆盖Controller/Service层所有操作接口）
    @Pointcut("@annotation(com.csi.annotation.OperateLog)")
    public void globalOperateLogPointcut() {}

    // 后置通知：接口执行成功后生成日志（避免接口异常时误记录无效日志）
    @AfterReturning("globalOperateLogPointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        // 1. 获取请求上下文（IP、Token等来自HTTP请求）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 2. 解析接口方法的注解信息（操作描述、对象类型）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperateLogs operateLog = (OperateLogs) method.getAnnotation(OperateLog.class);
        String operationDesc = operateLog.getOperation();
        String targetType = operateLog.getTargetType();

        // 3. 从Token解析操作人信息（确保日志记录“谁操作”，匹配权限逻辑）
        String token = request.getHeader("Authorization");
        Integer operatorId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Map<String, Object> userInfo = JwtTokenUtils.parseToken(token);
            operatorId = (Integer) userInfo.get("userId");
        }

        // 4. 提取接口参数（记录“操作了什么”，如客户ID、员工ID、新旧值）
        Object[] args = joinPoint.getArgs();
        String targetId = ""; // 操作对象ID（多个用逗号分隔）
        String oldValue = ""; // 操作前旧值（如旧负责人ID）
        String newValue = ""; // 操作后新值（如新负责人ID）
        // 示例：按实际接口参数结构提取（可根据参数类型适配，此处兼容常见场景）
        for (Object arg : args) {
            if (arg instanceof Integer) {
                targetId = arg.toString(); // 单个ID（如员工ID）
            } else if (arg instanceof List) {
                targetId = String.join(",", ((List<?>) arg).stream().map(String::valueOf).toArray(String[]::new)); // 多个ID（如客户ID列表）
            } else if (arg instanceof Map) {
                Map<String, Object> paramMap = (Map<String, Object>) arg;
                oldValue = paramMap.get("oldValue") != null ? paramMap.get("oldValue").toString() : "";
                newValue = paramMap.get("newValue") != null ? paramMap.get("newValue").toString() : "";
            }
        }

        // 5. 封装日志数据并插入数据库（统一日志格式，确保所有接口日志字段一致）
        OperateLogs log = new OperateLogs();
        log.setOperatorId(operatorId); // 操作人ID（关联employees表）
        log.setOperation(operationDesc); // 操作描述
        log.setTargetType(targetType); // 操作对象类型
        log.setTargetId(Integer.valueOf(targetId)); // 操作对象ID
        log.setIpAddress(request.getRemoteAddr()); // 操作IP地址
        log.setOldValue(oldValue); // 旧值
        log.setNewValue(newValue); // 新值

        operationLogsMapper.insert(log);
    }
}