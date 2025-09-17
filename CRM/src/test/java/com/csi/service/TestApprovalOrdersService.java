package com.csi.service;

import com.csi.config.DataSourceConfig;
import com.csi.config.MybatisJavaConfig;
import com.csi.config.ServiceJavaConfig;
import com.csi.domain.ApprovalOrders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig({DataSourceConfig.class, MybatisJavaConfig.class, ServiceJavaConfig.class})
public class TestApprovalOrdersService {

    @Autowired
    private ApprovalOrdersService approvalOrdersService;

    @Test
    public void test() {
        ApprovalOrders approvalOrders = new ApprovalOrders();

        approvalOrders.setType(1);
        approvalOrders.setCustomerId(1);
        approvalOrders.setOpportunityId(2);
        approvalOrders.setOrderId(2);
        approvalOrders.setApplicantId(2002);
        approvalOrders.setContent("测试");
        approvalOrders.setRequestedAmount(1000);
        approvalOrders.setStatus(1);

        int insert = approvalOrdersService.insert(approvalOrders);
        System.out.println(insert);
    }

}
