package com.csi.service;

import com.csi.domain.SalesEmergency;

import java.util.List;
import java.util.Map;

public interface SalesEmergencyService {

    void createEmergency(SalesEmergency emergency, Integer empId);/* 员工新建 */

    void submitForApproval(Integer id, Integer empId); /* 员工提交审批，提交之后草稿变待审批 */

    void approve(Integer id, Integer managerId); /* 经理审批通过 */

    void reject(Integer id, Integer managerId);/* 经理审批拒绝 */

    List<SalesEmergency> listByCreator(Integer empId);/* 员工查看自己创建的突发情况 */

    List<SalesEmergency> listPending();  /* 经理查看待审批列表 */

    SalesEmergency getById(Integer id); /* 根据 ID 查询详情 */

    // 根据处理人ID查询需要处理的突发状况
    List<SalesEmergency> listByHandler(Integer handlerId);

    // 根据条件查询突发状况
    List<SalesEmergency> listByCondition(Map<String, Object> params);

    // 统计各种状态的突发状况数量
    Map<String, Integer> getStatsBySalesperson(Integer salespersonId);

    List<SalesEmergency> getPendingEmergenciesByManager(Integer managerId);
}
