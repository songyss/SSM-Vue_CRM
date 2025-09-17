package com.csi.mapper;

import com.csi.domain.SalesEmergency;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SalesEmergencyMapper {

    void insert(SalesEmergency salesEmergency) ; //新增突发情况

    SalesEmergency selectById(Integer id) ; //根据突发事件id进行查询

    List<SalesEmergency> selectBySalespersonId(Integer salespersonId); //根据销售人员查询

    void update(SalesEmergency emergency); // 更新突发状况

    void updateStatusAndHandler(@Param("id") Integer id,
                                @Param("status") String status,
                                @Param("handlerId") Integer handlerId); //更新状态

    List<SalesEmergency> selectByStatus(String status);    // 根据状态查询（经理使用）

    List<SalesEmergency> selectByHandlerId(Integer handlerId); //根据处理人ID查询需要其处理的所有突发状况

    List<SalesEmergency> selectByCondition(Map<String,Object> params); //根据条件查询突发状况

    int countByStatus(Map<String, Object> params); //根据状态统计图发状况数量

    //int deleteById(Integer id); //软删除（将is_delete置1）

    List<SalesEmergency> selectPendingByManagerId(Integer managerId); //经理的等待审批
}
