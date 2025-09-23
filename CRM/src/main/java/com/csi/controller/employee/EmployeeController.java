package com.csi.controller.employee;

import com.csi.domain.Employee;
import com.csi.service.EmployeeService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/allList")
    public R getAllEmployeeList(){
        List<Employee> allEmployees = employeeService.getAllEmployees();
        if (allEmployees != null){
            return R.ok(allEmployees);
        } else {
            return R.error();
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
     * 根据ID查询员工
     * 用正则约束：id 必须是数字
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id) {
        try {
            Employee employee = employeeService.findById(id);
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * 查询所有员工
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    /**
     * 根据部门查询员工
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getEmployeesByDepartment(@PathVariable Integer departmentId) {
        try {
            List<Employee> employees = employeeService.findByDepartment(departmentId);
            return ResponseEntity.ok(employees);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 根据用户名查询员工
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getEmployeeByUsername(@PathVariable String username) {
        try {
            Employee employee = employeeService.findByUsername(username);
            if (employee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到用户名为 " + username + " 的员工");
            }
            return ResponseEntity.ok(employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 新增员工
     */
    @PostMapping
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        try {
            employeeService.save(employee);
            return ResponseEntity.status(HttpStatus.CREATED).body("员工创建成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * 更新员工信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        try {
            employee.setId(id);
            employeeService.update(employee);
            return ResponseEntity.ok("员工信息更新成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * 删除员工
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer id) {
        try {
            employeeService.deleteById(id);
            return ResponseEntity.ok("员工删除成功");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
