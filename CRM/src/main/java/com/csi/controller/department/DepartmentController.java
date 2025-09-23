package com.csi.controller.department;

import com.csi.domain.Department;
import com.csi.service.DepartmentService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("list")
    public R getDepartmentList(){
        List<Department> allCompanys = departmentService.findAllCompanys();
        Map<Department, List<Department>> map = new HashMap<>();
        for (Department allCompany : allCompanys) {
            Integer departmentId = allCompany.getDepartmentId();
            List<Department> allDepartments = departmentService.findAllDepartments(departmentId);
            map.put(allCompany, allDepartments);
        }
        return R.ok(map);
    }

    @PutMapping
    public R updateDepartment(@RequestBody Department department){
        departmentService.updateDepartment(department);
        return R.okMessage("更新成功");
    }

    @PostMapping
    public R addDepartment(@RequestBody Department department){
        departmentService.addDepartment(department);
        return R.okMessage("添加成功");
    }

}
