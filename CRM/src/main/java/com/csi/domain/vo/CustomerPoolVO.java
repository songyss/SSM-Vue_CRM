package com.csi.domain.vo;


import lombok.Data;
@Data
public class CustomerPoolVO {
        private Integer id;
        private String name;
        private String phone;
        private String sex;
        private String companyName;
        private String position;
        private String salesRemark;
        private String reason;
        private String poolCreateTime;   // 改成 String
        private String lastFollowTime;   // 改成 String
        private String createTime;       // 改成 String
        private String updateTime;       // 改成 String


}
