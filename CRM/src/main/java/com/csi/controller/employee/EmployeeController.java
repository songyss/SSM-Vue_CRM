package com.csi.controller.employee;

import com.csi.domain.Employee;
import com.csi.service.EmployeeService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
     * @return
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
     * 查询经理自己手下的员工
     * @param id
     * @return
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

}
