package com.csi.controller.customer;

import com.csi.domain.Customer;
import com.csi.domain.PublicCustomerPool;
import com.csi.domain.vo.CustomerDetailVO;
import com.csi.domain.vo.CustomerPoolVO;
import com.csi.domain.vo.CustomerVO;
import com.csi.domain.vo.LockCustomerRequest;
import com.csi.service.CustomerService;
import com.csi.util.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询全部客户信息
     */
    @GetMapping("/allList")
    public R getAllCustomerList() {
        List<Customer> allCustomer = customerService.getAllCustomer();
        return allCustomer != null ? R.ok(allCustomer) : R.error();
    }

    /**
     * 销售经理查询除未联系之外的全部客户
     */
    @GetMapping("/saleList")
    public R getAllSaleCustomerList() {
        List<Customer> allSaleCustomer = customerService.getAllSaleCustomer();
        return allSaleCustomer != null ? R.ok(allSaleCustomer) : R.error();
    }

    /**
     * 查询有意向已分配客户
     */
    @GetMapping("/assignedList")
    public R getAssignedCustomerList() {
        List<Customer> assignedCustomer = customerService.getAssignedCustomer();
        return assignedCustomer != null ? R.ok(assignedCustomer) : R.error();
    }

    /**
     * 查询有意向未分配客户（assignee_id IS NULL）
     */
    @GetMapping("/unAssignedList")
    public R getUnAssignedCustomerList() {
        List<Customer> unAssignedCustomer = customerService.getUnAssignedCustomer();
        return unAssignedCustomer != null ? R.ok(unAssignedCustomer) : R.error();
    }

    /**
     * 查询无意向客户
     */
    @GetMapping("/noIntention")
    public R getNoIntentionCustomerList() {
        List<Customer> noIntentionCustomer = customerService.getNoIntentionCustomer();
        return noIntentionCustomer != null ? R.ok(noIntentionCustomer) : R.error();
    }

    /**
     * 查询信息有误客户
     */
    @GetMapping("/infoIncorrect")
    public R getInfoIncorrectCustomerList() {
        List<Customer> infoIncorrectCustomer = customerService.getInfoIncorrectCustomer();
        return infoIncorrectCustomer != null ? R.ok(infoIncorrectCustomer) : R.error();
    }

    /**
     * 根据来源查询客户
     */
    @GetMapping("/bySource/{source}")
    public R getCustomerListBySource(@PathVariable("source") String source) {
        List<Customer> CustomerBySource = customerService.getCustomerBySource(source);
        return CustomerBySource != null ? R.ok(CustomerBySource) : R.error();
    }


    /**
     * 销售对客户状态进行更改
     */
    @PatchMapping("/status")
    public R changeCustomerStatus(@RequestParam("id") int id, @RequestParam("status") int status) {
        int i = customerService.changeCustomerStatus(id, status);
        return i == 1 ? R.ok(i) : R.error();
    }

    /**
     * 查询当前销售的所负责客户
     */
    @GetMapping("/personalCustomer")
    public R getPersonalCustomer(@RequestParam("id") int id) {
        List<Customer> customers = customerService.getPersonalCustomer(id);
        return customers != null ? R.ok(customers) : R.error();
    }

    /**
     * 销售新增用户
     */
    @PostMapping("/add5Customer")
    public R add5Customer(@RequestBody Customer customer) {
        int i = customerService.add5Customer(customer);
        return i == 1 ? R.ok(i) : R.error();
    }

    /**
     * 新增用户（标准接口）
     */
    @PostMapping("/addCustomer")
    public R addCustomer(@RequestBody Customer customer) {
        int i = customerService.add5Customer(customer);
        return i == 1 ? R.ok(i) : R.error();
    }

    /**
     * 销售更新用户信息
     */
    @PatchMapping("/updateCustomer")
    public R updateCustomer(@RequestBody Customer customer) {
        int i = customerService.updateCustomer(customer);
        return i == 1 ? R.ok(i) : R.error();
    }

    @GetMapping("/findBySdrStatus")
    public R getCustomerBySdrStatus() {
        List<Customer> list = customerService.getCustomerBySdrStatus();
        return list != null ? R.ok(list) : R.error();
    }

    @PatchMapping("/updateCustomerSdrStatus")
    public R updateCustomerSdrStatus(@RequestBody Customer customer) {
        int i = customerService.updateCustomerSdrStatus(customer);
        return i == 1 ? R.ok(i) : R.error();
    }

    /**
     * 根据条件查询客户（支持模糊查询）
     */
    @GetMapping("/condition")
    public R getCustomerByCondition(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "phone", required = false) String phone,
                                    @RequestParam(value = "source", required = false) String source,
                                    @RequestParam(value = "status", required = false) Integer status) {
        List<Customer> customers = customerService.getCustomerByCondition(name, phone, source, status);
        return customers != null ? R.ok(customers) : R.error();
    }

    /**
     * 查询客户池客户
     */
    @GetMapping("/poolList")
    public R getPoolCustomerList() {
        List<Customer> poolCustomer = customerService.getPoolCustomer();
        return poolCustomer != null ? R.ok(poolCustomer) : R.error();
    }

    /**
     * 获取待分配客户（可按状态过滤）
     */
    @GetMapping("/unAssigned")
    public List<Customer> getUnAssignedCustomerList(@RequestParam(value = "status", required = false) Integer status) {
        return customerService.getUnAssignedCustomerList(status);
    }

    /**
     * 分配客户
     */
    @PostMapping("/assign")
    public R assignCustomers(@RequestBody Map<String, Object> params) {
        Integer employeeId = (Integer) params.get("employeeId");
        List<Integer> customerIds = (List<Integer>) params.get("customerIds");
        customerService.assignCustomers(employeeId, customerIds);
        return R.ok("分配成功");
    }

    /**
     * 更新客户（标准接口，含订单 & 商机）
     */
    @PostMapping("/update")
    public R update(@RequestBody Customer customer) {
        int i = customerService.updateCustomer(customer);
        return i == 1 ? R.ok(i) : R.error();
    }

    /**
     * 根据销售ID分页查询客户（支持模糊查询）
     */
    @GetMapping("/listByEmployee")
    public R listByEmployee(
            @RequestParam("employeeId") Integer employeeId,
            @RequestParam("pageNum") int pageNum,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone) {

        PageInfo<CustomerVO> pageInfo = customerService.listByEmployee(employeeId, pageNum, pageSize, name, phone);
        return R.ok(pageInfo);
    }


    /**
     * 锁定客户（加入客户池的客户分配给当前销售）
     */
    @PostMapping("/lock/{id}")
    public R lockCustomer(@PathVariable("id") Integer customerId,
                          @RequestBody(required = false) Map<String, Object> body) {
        try {
            Integer employeeId = null;
            if (body != null && body.containsKey("employeeId")) {
                employeeId = (Integer) body.get("employeeId");
            }

            if (employeeId == null) {
                return R.error();
            }

            boolean success = customerService.lockCustomer(customerId, employeeId);
            return success ? R.ok("锁定成功") : R.error();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }

    /**
     * 加入客户池（含原因）
     */
    @PostMapping("/addToPool")
    public R addToPool(@RequestBody PublicCustomerPool pool) {
        int res = customerService.addToPool(pool.getCustomerId(), pool.getReason(), pool.getOperatorId());
        return res > 0 ? R.ok("success") : R.error();
    }

    /**
     * 分页查询客户（支持模糊查询）
     */
    @GetMapping("/list")
    public R getCustomerList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<Customer> list = customerService.getCustomerByCondition(name, phone, null, null);
        PageInfo<Customer> pageInfo = new PageInfo<>(list);

        return R.ok(pageInfo);
    }



    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        CustomerDetailVO detail = customerService.getCustomerDetail(id);
        return R.ok(detail);
    }

    /**
     * 锁定客户
     */
    @PostMapping("/lock")
    public String lockCustomer(@RequestBody LockCustomerRequest request) {
        customerService.lockCustomer(request);
        return "锁定成功";
    }


    @GetMapping("/poolListAdd")
    public R getCustomerPoolList() {
        return R.ok(customerService.getCustomerPoolList());
    }
}

