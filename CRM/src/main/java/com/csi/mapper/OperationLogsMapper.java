package com.csi.mapper;

import com.csi.domain.OperateLogs;

import java.util.List;
import java.util.Map;

public interface OperationLogsMapper {

    int insert(OperateLogs operateLogs);

    // 使用 PageHelper，只需要一个查询方法，不需要单独的分页和统计方法
    List<OperateLogs> selectLogList();
    List<OperateLogs> selectByCondition(Map<String, Object> params);

}
