package com.csi.mapper;

import com.csi.domain.EmergencyCommunication;

import java.util.List;

public interface EmergencyCommunicationMapper {
    void insert(EmergencyCommunication communication) ;

    // 根据突发状况ID查询沟通记录
    List<EmergencyCommunication> selectByEmergencyId(Integer emergencyId);
}
