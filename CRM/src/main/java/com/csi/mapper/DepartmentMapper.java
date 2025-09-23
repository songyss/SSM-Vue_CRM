package com.csi.mapper;

import com.csi.domain.Department;
import com.csi.domain.Model;

import java.util.List;

public interface DepartmentMapper {

    List<Department> findAllDepartments(int superDepartId);

    List<Department> findAllCompanys ();

    int updateDepartment(Department department);

    int addDepartment(Department department);

}
