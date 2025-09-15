package com.csi.service;

import com.csi.domain.CustomerFollows;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerFollowsService {

    List<CustomerFollows> getAllCustomerFollows();//查询所有客户跟进

    List<CustomerFollows> getPersonalCustomerByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);//根据跟进时间筛选客户

    int addCustomerFollows(@Param("customerFollows") CustomerFollows customerFollows);//新建商机

    int updateCustomerFollows(@Param("customerFollows") CustomerFollows customerFollows);//更改商机

}
