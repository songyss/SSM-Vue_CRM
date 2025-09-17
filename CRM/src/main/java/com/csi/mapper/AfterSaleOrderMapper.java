package com.csi.mapper;

import com.csi.domain.AfterSaleOrder;
import org.apache.ibatis.annotations.Param;

public interface AfterSaleOrderMapper {
    int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder);
    AfterSaleOrder getAfterSaleOrderByStatus(int afterSaleStatus);

    AfterSaleOrder getAfterSaleOrderByOrderNumber(int orderNumber);
    int updateAfterSaleOrder(@Param("afterSaleStatus")int afterSaleStatus,@Param("id") int id);
}
