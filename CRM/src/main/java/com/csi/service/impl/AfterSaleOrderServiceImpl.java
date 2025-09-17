package com.csi.service.impl;

import com.csi.domain.AfterSaleOrder;
import com.csi.mapper.AfterSaleOrderMapper;
import com.csi.service.AfterSaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AfterSaleOrderServiceImpl implements AfterSaleOrderService {
    @Autowired
    private AfterSaleOrderMapper afterSaleOrderMapper;

    @Override
    public int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder) {
        return 0;
    }

    @Override
    public AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus) {
        return null;
    }

    @Override
    public AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber) {
        return null;
    }
}
