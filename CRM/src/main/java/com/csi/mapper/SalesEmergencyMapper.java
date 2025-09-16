package com.csi.mapper;

import com.csi.domain.SalesEmergency;

import java.util.List;

public interface SalesEmergencyMapper {

    void insert(SalesEmergency salesEmergency) ; //新增突发情况

    SalesEmergency selectById(Integer id) ; //根据突发事件id进行查询

    List<SalesEmergency> selectBySalespersonId(Integer salespersonId); //根据销售人员查询

    void update(SalesEmergency emergency); // 更新突发状况

    void deleteById(Integer id);  // 删除突发状况


    List<SalesEmergency> selectByStatus(String status);    // 根据状态查询

}
