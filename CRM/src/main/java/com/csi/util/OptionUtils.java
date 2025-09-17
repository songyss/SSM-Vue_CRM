package com.csi.util;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class OptionUtils {

    public static Map<String ,String> getRiskLevelOptions(){
        Map<String,String> options = new LinkedHashMap<>();
        options.put("HIGH","高");
        options.put("MEDIUM","中");
        options.put("LOW","低");
        return options ;
    }

    public static Map<Integer,String> getUrgencyOptions(){
        Map<Integer,String> options = new LinkedHashMap<>();
        options.put(1, "特急");
        options.put(2, "紧急");
        options.put(3, "普通");
        options.put(4, "低");
        return options;
    }

    public static Map<String, String> getTypeOptions() {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("DISCOUNT", "折扣申请");
        options.put("PAYMENT", "付款问题");
        options.put("DELIVERY", "交付问题");
        options.put("CONTRACT", "合同问题");
        options.put("OTHER", "其他");
        return options;
    }
}
