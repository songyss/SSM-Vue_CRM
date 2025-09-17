package com.csi.util;

import com.csi.domain.SalesEmergency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValidationUtils {

//    允许的风险等级
    private static final Set<String> ALLOWED_RISK_LEVELS =
        new HashSet<>(Arrays.asList("HIGH","MEDIUM","LOW"));

//    允许的紧急程度
    private static final Set<Integer> ALLOW_URGENCY_LEVELS =
        new HashSet<>(Arrays.asList(1,2,3,4));

//    允许的类型值
    private static final Set<String> ALLOWED_TYPES =
        new HashSet<>(Arrays.asList("DISCOUNT", "PAYMENT", "DELIVERY", "CONTRACT"));

//    验证风险等级是否合法
    public static boolean isValidRiskLevel(String riskLevel){
        return riskLevel != null && ALLOWED_RISK_LEVELS.contains(riskLevel);
    }

//  验证紧急程度程度合法
    public static boolean isValidUrgency(Integer urgency){
        return urgency != null && ALLOW_URGENCY_LEVELS.contains(urgency);
    }

//  验证类型是否合法
    public static boolean isValidType(String type){
        return type != null && ALLOWED_TYPES.contains(type);
    }

//    验证所有固有字段
    public static void validateFixed(SalesEmergency salesEmergency){
        if (!isValidRiskLevel(salesEmergency.getRiskLevel())) {
            throw new RuntimeException("无效的风险等级值" + salesEmergency.getRiskLevel());
        }
        if (!isValidUrgency(salesEmergency.getUrgency())) {
            throw new RuntimeException("无效的紧急程度值" + salesEmergency.getUrgency());
        }
        if (!isValidType(salesEmergency.getType())) {
            throw new RuntimeException("无效的类型值" + salesEmergency.getType());
        }
    }
}
