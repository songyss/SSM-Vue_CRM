package com.csi.controller.employee;

import com.csi.domain.Employee;
import com.csi.service.EmployeeService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理员查看全部员工
     */
    /**
     * 管理员查看全部员工（支持模糊查询）
     */
    @GetMapping("/allList")
    public R getAllEmployeeList(
            @RequestParam(value = "superiorId", required = false) Integer superiorId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email
    ){
        // 创建查询条件对象
        Employee queryCondition = new Employee();
        if (username != null && !username.trim().isEmpty()) {
            queryCondition.setUsername(username);
        }
        if (name != null && !name.trim().isEmpty()) {
            queryCondition.setName(name);
        }
        if (phone != null && !phone.trim().isEmpty()) {
            queryCondition.setPhone(phone);
        }
        if (email != null && !email.trim().isEmpty()) {
            queryCondition.setEmail(email);
        }

        List<Employee> allEmployees = employeeService.getAllEmployeesWithConditions(superiorId, queryCondition);
        if (allEmployees != null){
            return R.ok(allEmployees);
        } else {
            return R.error();
        }
    }

    /**
     * 重置员工密码
     */
    @PutMapping("/resetPwd")
    public R resetPassword(@RequestBody Employee employee) {
        try {
            // 检查参数
            if (employee.getId() == null) {
                return R.message("员工ID不能为空");
            }
            if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
                return R.message("密码不能为空");
            }

            employeeService.resetPassword(employee.getId(), employee.getPassword());
            return R.okMessage("密码重置成功");
        } catch (Exception e) {
            return R.message("密码重置失败: " + e.getMessage());
        }
    }


    /**
     * 管理员查看全部在职员工
     */
    @GetMapping("/allOnList")
    public R getAllOnEmployeeList(){
        List<Employee> allOnEmployees = employeeService.getAllOnEmployees();
        if (allOnEmployees != null){
            return R.ok(allOnEmployees);
        } else {
            return R.error();
        }
    }

    /**
     * 管理员查看全部已离职员工
     */
    @GetMapping("/allLeaveList")
    public R getAllLeaveEmployeeList(){
        List<Employee> allLeaveEmployees = employeeService.getAllLeaveEmployees();
        if (allLeaveEmployees != null){
            return R.ok(allLeaveEmployees);
        } else {
            return R.error();
        }
    }

    /**
     * 查询经理自己手下的员工
     */
    @GetMapping("/list")
    public R getEmployeeList(@RequestParam("id") int id){
        List<Employee> employees = employeeService.getEmployees(id);
        if (employees != null){
            return R.ok(employees);
        } else {
            return R.error();
        }
    }

    /**
     * 查询所有员工
     */
    @GetMapping
    public R getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return R.ok(employees);
    }

    /**
     * 根据部门查询员工
     */
    @GetMapping("/department/{departmentId}")
    public R getEmployeesByDepartment(@PathVariable Integer departmentId) {
        try {
            List<Employee> employees = employeeService.findByDepartment(departmentId);
            return R.ok(employees);
        } catch (IllegalArgumentException e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 根据用户名查询员工
     */
    @GetMapping("/username/{username}")
    public R getEmployeeByUsername(@PathVariable String username) {
        try {
            Employee employee = employeeService.findByUsername(username);
            if (employee == null) {
                return R.message("找不到用户名为 " + username + " 的员工");
            }
            return R.ok(employee);
        } catch (IllegalArgumentException e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 新增员工
     */
    @PostMapping("/insert")
    public R createEmployee(@RequestBody Employee employee) {
        try {
            employeeService.save(employee);
            return R.okMessage("员工创建成功");
        } catch (IllegalArgumentException e) {
            return R.message(e.getMessage());
        } catch (RuntimeException e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 更新员工信息根据id
     */
    @PutMapping("/update")
    public R update(@RequestBody Employee employee) {
        try {
            employeeService.update(employee);
            return R.okMessage("员工信息更新成功");
        } catch (IllegalArgumentException e) {
            return R.message(e.getMessage());
        } catch (RuntimeException e) {
            return R.message(e.getMessage());
        }
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public R deleteEmployee(@PathVariable("id") Integer id) {
        try {
            employeeService.deleteById(id);
            return R.okMessage("员工删除成功");
        } catch (IllegalArgumentException e) {
            return R.message(e.getMessage());
        } catch (RuntimeException e) {
            return R.message(e.getMessage());
        }
    }


}
