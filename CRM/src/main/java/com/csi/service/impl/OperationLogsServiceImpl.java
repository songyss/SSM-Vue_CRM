package com.csi.service.impl;

import com.csi.domain.OperateLogs;
import com.csi.mapper.OperationLogsMapper;
import com.csi.service.OperationLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationLogsServiceImpl implements OperationLogsService {

    @Autowired
    private OperationLogsMapper operationLogsMapper;


    @Override
    public int insert(OperateLogs operateLogs) {
        return operationLogsMapper.insert(operateLogs);
    }
}
