package com.csi.controller.approvalOrders;

import com.csi.domain.ApprovalOrders;
import com.csi.service.ApprovalOrdersService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/approvalOrders")
public class ApprovalOrdersController {

    @Autowired
    private ApprovalOrdersService approvalOrdersService;

    @PutMapping("/add")
    public R insert(@RequestBody ApprovalOrders approvalOrders) {
        int insert = approvalOrdersService.insert(approvalOrders);
        if (insert > 0) {
            return R.ok(insert) ;
        } else {
            return R.error() ;
        }
    }

    @PatchMapping("/status")
    public R updateStatus(@RequestBody ApprovalOrders approvalOrders) {
        int update = approvalOrdersService.updateStatus(approvalOrders);
        if (update > 0) {
            return R.ok(update) ;
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/list")
    public R selectAll() {
        List<ApprovalOrders> approvalOrders = approvalOrdersService.selectAll();
        if (approvalOrders.size() > 0) {
            return R.ok(approvalOrders) ;
        } else {
            return R.error() ;
        }
    }

    @GetMapping("/personal")
    public R selectPersonal(int approverId) {
        List<ApprovalOrders> approvalOrders = approvalOrdersService.selectPersonal(approverId);
        if (approvalOrders.size() > 0) {
            return R.ok(approvalOrders) ;
        } else {
            return R.error() ;
        }
    }

}
