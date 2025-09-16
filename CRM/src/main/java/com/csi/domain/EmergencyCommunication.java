package com.csi.domain;

import java.sql.Date;

public class EmergencyCommunication {

    private Integer id; // 主键ID
    private Integer emergencyId;   // 突发状况ID
    private String commType; // 沟通类型：CUSTOMER-客户、INTERNAL-内部
    private String commChannel; // 沟通渠道：PHONE-电话、EMAIL-邮件、WECHAT-微信、MEETING-会议
    private String content; // 沟通内容
    private Integer createdBy; // 记录人ID
    private Date createdTime; // 记录创建时间
}
