package com.csi.service;

import com.csi.domain.OperateLogs;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface OperationLogsService {

    int insert(OperateLogs operateLogs);

    PageInfo<OperateLogs> selectLogList(int page, int size);
    PageInfo<OperateLogs> selectByCondition(Map<String, Object> params, int page, int size);
}
