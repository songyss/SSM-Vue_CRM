package com.csi.controller.department;

import com.csi.annotation.OperateLog;
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
    @OperateLog(operation = "部门列表", targetType = "部门")
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
    @OperateLog(operation = "部门更新", targetType = "部门")
    public R updateDepartment(@RequestBody Department department){
        departmentService.updateDepartment(department);
        return R.okMessage("更新成功");
    }

    @PostMapping
    @OperateLog(operation = "部门添加", targetType = "部门")
    public R addDepartment(@RequestBody Department department){
        // 设置默认值
        department.setIsDelete(0); // 新增时默认为正常状态
        departmentService.addDepartment(department);
        return R.okMessage("添加成功");
    }

    // 删除部门（逻辑删除）
    @DeleteMapping("/{departmentId}")
    @OperateLog(operation = "部门删除", targetType = "部门")
    public R deleteDepartment(@PathVariable("departmentId") Integer departmentId) {
        Department department = new Department();
        department.setDepartmentId(departmentId);
        department.setIsDelete(1); // 设置为删除状态
        departmentService.updateDepartment(department); // 复用更新方法
        return R.okMessage("删除成功");
    }

    @GetMapping("condition")
    @OperateLog(operation = "部门查询", targetType = "部门")
    public R getDepartmentByCondition(@RequestParam(value = "departmentName" ,required = false) String departmentName,
                                      @RequestParam(value = "isDelete" ,required = false) Integer isDelete){

        List<Department> byCondition = departmentService.findByCondition(departmentName, isDelete);
        return R.ok(byCondition);

    }

    @GetMapping("tree")
    @OperateLog(operation = "部门树", targetType = "部门")
    public R getDepartmentTree() {
        List<Department> allCompanys = departmentService.findAllCompanys();
        Map<Department, List<Department>> map = new HashMap<>();
        for (Department allCompany : allCompanys) {
            Integer departmentId = allCompany.getDepartmentId();
            List<Department> allDepartments = departmentService.findAllDepartments(departmentId);
            map.put(allCompany, allDepartments);
        }
        return R.ok(map);
    }

}
