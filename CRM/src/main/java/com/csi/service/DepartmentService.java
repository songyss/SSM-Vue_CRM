package com.csi.service;

import com.csi.domain.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentService {

    List<Department> findAllDepartments(int superDepartId);

    List<Department> findAllCompanys ();

    int updateDepartment(Department department);

    int addDepartment(Department department);

    List<Department> findByCondition(String departmentName,Integer isDelete);

    int updateChildIsDeleteByFather(@Param("departmentId") Integer departmentId,@Param("isDelete") Integer isDelete);

    List<Department> findByDepartmentId(Integer superId);

}
