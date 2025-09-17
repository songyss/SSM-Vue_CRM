package com.csi.mapper;

import com.csi.domain.AfterSaleOrder;

public interface AfterSaleOrderMapper {
    int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder);
    AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus);

    AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber);
}
