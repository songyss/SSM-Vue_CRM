package com.csi.mapper;

import com.csi.domain.AfterSaleOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleOrderMapper {
    int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder);
    List<AfterSaleOrder> getAfterSaleOrderByStatus(int afterSaleStatus);

    List<AfterSaleOrder> getAfterSaleOrderByOrderNumber(int orderNumber);
    int updateAfterSaleOrder(@Param("afterSaleStatus")int afterSaleStatus,@Param("orderNumber")String orderNumber);
}
