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
        List<Customer> allSaleCustomer = customerService.getAllSaleCustomer();
        if (allSaleCustomer != null){
            return R.ok(allSaleCustomer);
        } else {
            return R.error();
        }
    }

    /**
     * 查询有意向已分配客户
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
     * 查询有意向未分配客户
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

    /**
     * 查询无意向客户
     * @return
     */
    @GetMapping("/noIntention")
    public R getNoIntentionCustomerList(){
        List<Customer> noIntentionCustomer = customerService.getNoIntentionCustomer();
        if (noIntentionCustomer != null){
            return R.ok(noIntentionCustomer);
        } else {
            return R.error();
        }
    }

    /**
     * 查询信息有误客户
     * @return
     */
    @GetMapping("/infoIncorrect")
    public R getInfoIncorrectCustomerList(){
        List<Customer> infoIncorrectCustomer = customerService.getInfoIncorrectCustomer();
        if (infoIncorrectCustomer != null){
            return R.ok(infoIncorrectCustomer);
        } else {
            return R.error();
        }
    }

    /**
     * 根据来源查询客户
     * @param source
     * @return
     */
    @GetMapping("/{source}")
    public R getCustomerListBySource(@PathVariable("source") String source){
        List<Customer> CustomerBySource = customerService.getCustomerBySource(source);
        if (CustomerBySource != null){
            return R.ok(CustomerBySource);
        } else {
            return R.error();
        }
    }

    //销售对客户状态进行更改
    @PatchMapping("/status")
    public R changeCustomerStatus(@RequestParam("id") int id,@RequestParam("status") int status) {

        int i = customerService.changeCustomerStatus(id, status);

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

    //销售新增用户
    @PutMapping("/add5Customer")
    public R add5Customer(@RequestBody Customer customer) {
        int i = customerService.add5Customer(customer);
        if (i == 1) {
            return R.ok(i) ;
        } else {
            return R.error();
        }
    }

    //销售更新用户信息
    @PatchMapping("/updateCustomer")
    public R updateCustomer(@RequestBody Customer customer) {
        int i = customerService.updateCustomer(customer);
        if (i == 1) {
            return R.ok(i) ;
        } else {
            return R.error();
        }
    }

    @GetMapping("/findBySdrStatus")
    public R getCustomerBySdrStatus(){
        List<Customer> list = customerService.getCustomerBySdrStatus();
        if (list != null){
            return R.ok(list);
        }else {
            return R.error();
        }
    }
    @PatchMapping("/updateCustomerSdrStatus")
    public R updateCustomerSdrStatus(@RequestBody Customer customer){
        int i = customerService.updateCustomerSdrStatus(customer);
        if (i == 1){
            return R.ok(i);
        }else {
            return R.error();
        }
    }


}
