package com.csi.service;

import com.csi.domain.SalesEmergency;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SalesEmergencyService {
    void setEmergency(SalesEmergency emergency) ; //创建突发情况

    SalesEmergency getEmergencyById(Integer id) ; //获取突发状况详情

    List<SalesEmergency> getEmergenciesBySalesperson(Integer salespersonId); // 获取销售人员的所有突发状况

    // 更新突发状况
    void updateEmergency(SalesEmergency emergency);

    // 删除突发状况
    void deleteEmergency(Integer id);


}
