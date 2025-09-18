package com.csi.controller.movecustomer;

import com.csi.service.MoveCustomerService;
import com.csi.util.IpUtil;
import com.csi.util.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/move")
public class MoveCustomerController {

    @Autowired
    private MoveCustomerService moveCustomerService;

    //选中复选框 拿到客户id列表
    @PostMapping("/modify")//modifyAssigneeId 市场部经理传客户给销售部经理 把负责人id改为销售经理id
    public R modifyAssigneeId(@RequestBody Map<String, Object> paramMap, HttpServletRequest request){

        //到时候前端要 传客户id列表 和 目标经理id 和 操作人id也就是市场部经理id
        try {
            List<Integer> customerIds = (List<Integer>) paramMap.get("customerIds");
            int targetManagerId = Integer.parseInt(paramMap.get("targetManagerId").toString());
            int operatorId = Integer.parseInt(paramMap.get("operatorId").toString());

            String realIp = IpUtil.getRealIp((javax.servlet.http.HttpServletRequest) request);
            moveCustomerService.batchPush(customerIds, targetManagerId, operatorId,realIp);

            return R.okMessage("批量推送成功，共" + customerIds.size() + "条客户");
        } catch (NumberFormatException e) {
            return R.message("推送失败：" + e.getMessage());
        }

    }

}












