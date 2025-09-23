package com.csi.mapper;

import com.csi.domain.Department;
import com.csi.domain.Model;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    List<Department> findAllDepartments(int superDepartId);

    List<Department> findAllCompanys ();

    int updateDepartment(Department department);

    int addDepartment(Department department);

    List<Department> findByCondition(@Param("departmentName") String departmentName, @Param("isDelete") Integer isDelete);

    int updateChildIsDeleteByFather(@Param("departmentId") Integer departmentId,@Param("isDelete") Integer isDelete);

    List<Department> findByDepartmentId(Integer did);

}
