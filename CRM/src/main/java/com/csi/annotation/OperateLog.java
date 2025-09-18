package com.csi.annotation;

import java.lang.annotation.*;

/**
 * 通用操作日志注解：标注在所有需记录日志的接口方法上（Controller/Service层均可）
 */
@Target(ElementType.METHOD) // 作用于方法（接口方法本质是Controller/Service层方法）
@Retention(RetentionPolicy.RUNTIME) // 运行时生效，便于AOP解析
@Documented
public @interface OperateLog {
    String operation() default ""; // 操作描述（如“客户推送”“员工新增”，必填）
    String targetType() default ""; // 操作对象类型（如“customer”“employee”，必填）
}