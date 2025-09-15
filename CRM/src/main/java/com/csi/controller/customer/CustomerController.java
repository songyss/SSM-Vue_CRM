package com.csi.controller.customer;

import com.csi.domain.Customer;
import com.csi.domain.CustomerFollows;
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
     * 销售经理查询除未联系之外的全部客户
     * @return
     */
    @GetMapping("/saleList")
    public R getAllSaleCustomerList(){
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

    //销售对客户状态进行更改
    @PatchMapping("/status")
    public R changeCustomerStatus(@RequestBody Customer customer) {

        int i = customerService.changeCustomerStatus(customer);

        if (i == 1) {
            return R.ok(i) ;
        } else {
            return R.error() ;
        }
    }

    //查询当前销售的所负责客户
    @GetMapping("/personalCustomer")
    public R getPersonalCustomer(@RequestParam("id") int id) {
        List<Customer> customers = customerService.getPersonalCustomer(id);

        if (customers != null) {
            return R.ok(customers);
        } else {
            return R.error();
        }
    }

    //根据跟进时间筛选客户
    @GetMapping("/personalCustomerByTime")
    public R getPersonalCustomerByTime(@RequestParam("creatTime")String creatTime) {
        List<CustomerFollows> customers = customerService.getPersonalCustomerByTime(creatTime);

        if (customers != null) {
            return R.ok(customers);
        } else {
            return R.error();
        }
    }

}
