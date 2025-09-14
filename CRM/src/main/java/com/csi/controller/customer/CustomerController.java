package com.csi.controller.customer;

import com.csi.domain.Customer;
import com.csi.service.CustomerService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询全部客户信息
     * @return
     */
    @GetMapping("/allList")
    public R getAllCustomerList(){
        List<Customer> allCustomer = customerService.getAllCustomer();
        if (allCustomer != null){
            return R.ok(allCustomer);
        } else {
            return R.error();
        }
    }

    /**
     * 查询已分配客户
     * @return
     */
    @GetMapping("/assignedList")
    public R getAssignedCustomerList(){
        List<Customer> assignedCustomer = customerService.getAssignedCustomer();
        if (assignedCustomer != null){
            return R.ok(assignedCustomer);
        } else {
            return R.error();
        }
    }

    /**
     * 查询未分配客户
     * @return
     */
    @GetMapping("/unAssignedList")
    public R getUnAssignedCustomerList(){
        List<Customer> unAssignedCustomer = customerService.getUnAssignedCustomer();
        if (unAssignedCustomer != null){
            return R.ok(unAssignedCustomer);
        } else {
            return R.error();
        }
    }

    @PatchMapping("/status")
    public R changeCustomerStatus(@RequestBody Customer customer) {

        System.out.println(customer);

        int i = customerService.changeCustomerStatus(customer);

        if (i == 1) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }


}
