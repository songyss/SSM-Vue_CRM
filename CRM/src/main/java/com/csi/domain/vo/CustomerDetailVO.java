package com.csi.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class CustomerDetailVO {
    private Long id;
    private String name;
    private String phone;
    private Integer sex;
    private String companyName;
    private String position;
    private String salesRemark;

    private List<OrderVO> orders;
    private List<OpportunityVO> opportunities;
}
