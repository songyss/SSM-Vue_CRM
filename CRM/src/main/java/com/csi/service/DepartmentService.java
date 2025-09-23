package com.csi.service;

import com.csi.domain.Department;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllDepartments(int superDepartId);

    List<Department> findAllCompanys ();

    int updateDepartment(Department department);

    int addDepartment(Department department);

}
