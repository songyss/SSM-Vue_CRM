package com.csi.mapper;

import com.csi.domain.CustomerFollows;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerFollowsMapper {

    List<CustomerFollows> getAllCustomerFollows();//查询所有客户跟进

    List<CustomerFollows> getPersonalCustomerByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);//根据跟进时间筛选客户

    int addCustomerFollows(CustomerFollows customerFollows);//新建商机

    int updateCustomerFollows(CustomerFollows customerFollows);//更改商机

    List<CustomerFollows> getCustomerFollowsByCustomerId(Integer customerId); // 根据客户ID获取跟进记录
}
