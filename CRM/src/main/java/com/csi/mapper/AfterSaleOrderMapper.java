package com.csi.mapper;

import com.csi.domain.AfterSaleOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AfterSaleOrderMapper {
    int saveAfterSaleOrder(AfterSaleOrder afterSaleOrder);
    List<AfterSaleOrder> getAfterSaleOrderByStatus();

    List<AfterSaleOrder> getAfterSaleOrderByOrderNumber(String orderNumber);
    List<AfterSaleOrder> getAfterSaleOrderByDateRange(@Param("startDate")String startDate,@Param("endDate")String endDate);
    int updateAfterSaleOrder(@Param("afterSaleStatus")int afterSaleStatus,@Param("orderNumber")String orderNumber);
    List<AfterSaleOrder> getAfterSaleOrderByCondition(
            @Param("afterSaleStatus") Integer afterSaleStatus,
            @Param("orderNumber") String orderNumber,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

}
