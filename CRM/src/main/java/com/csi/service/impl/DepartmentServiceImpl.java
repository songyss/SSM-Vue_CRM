package com.csi.service.impl;

import com.csi.domain.Department;
import com.csi.mapper.DepartmentMapper;
import com.csi.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> findAllDepartments(int superDepartId) {
        return departmentMapper.findAllDepartments(superDepartId);
    }

    @Override
    public List<Department> findAllCompanys() {
        return departmentMapper.findAllCompanys();
    }

    @Override
    public int updateDepartment(Department department) {
        return departmentMapper.updateDepartment(department);
    }

    @Override
    public int addDepartment(Department department) {
        return departmentMapper.addDepartment(department);
    }
}
