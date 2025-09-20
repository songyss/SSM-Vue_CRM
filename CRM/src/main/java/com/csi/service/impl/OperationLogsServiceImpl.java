// OperationLogsServiceImpl.java
package com.csi.service.impl;

import com.csi.domain.OperateLogs;
import com.csi.mapper.OperationLogsMapper;
import com.csi.service.OperationLogsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class OperationLogsServiceImpl implements OperationLogsService {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogsServiceImpl.class);

    @Autowired
    private OperationLogsMapper operationLogsMapper;

    @Override
    public int insert(OperateLogs operateLogs) {
        try {
            logger.info("插入操作日志: {}", operateLogs);
            return operationLogsMapper.insert(operateLogs);
        } catch (Exception e) {
            logger.error("插入操作日志时发生异常: ", e);
            throw e;
        }
    }

    @Override
    public PageInfo<OperateLogs> selectLogList(int page, int size) {
        try {
            logger.info("分页查询所有操作日志，页码: {}, 每页数量: {}", page, size);
            PageHelper.startPage(page, size);
            List<OperateLogs> logs = operationLogsMapper.selectLogList();
            PageInfo<OperateLogs> pageInfo = new PageInfo<>(logs);
            logger.info("分页查询结果数量: {}", logs.size());
            return pageInfo;
        } catch (Exception e) {
            logger.error("分页查询所有操作日志时发生异常: ", e);
            throw e;
        }
    }

    @Override
    public PageInfo<OperateLogs> selectByCondition(Map<String, Object> params, int page, int size) {
        try {
            logger.info("根据条件分页查询操作日志，参数: {}, 页码: {}, 每页数量: {}", params, page, size);
            PageHelper.startPage(page, size);
            List<OperateLogs> logs = operationLogsMapper.selectByCondition(params);
            PageInfo<OperateLogs> pageInfo = new PageInfo<>(logs);
            logger.info("条件分页查询结果数量: {}", logs.size());
            return pageInfo;
        } catch (Exception e) {
            logger.error("条件分页查询操作日志时发生异常: ", e);
            throw e;
        }
    }
}
