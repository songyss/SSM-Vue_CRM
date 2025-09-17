package com.csi.service.impl;

import com.csi.domain.Employee;
import com.csi.mapper.EmployeeMapper;
import com.csi.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeMapper.getAllEmployees();
    }

    @Override
    public List<Employee> getAllOnEmployees() {
        return employeeMapper.getAllOnEmployees();
    }

    @Override
    public List<Employee> getAllLeaveEmployees() {
        return employeeMapper.getAllLeaveEmployees();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployees(int id) {
        return employeeMapper.getEmployees(id);
    }

    @Override
    public int findLeaderIdByEmployeeId(int id) {
        return employeeMapper.findLeaderIdByEmployeeId(id);
    }

    @Override
    public Employee findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("员工ID不能为空");
        }
        Employee employee = employeeMapper.findById(id);
        if (employee == null) {
            throw new RuntimeException("找不到ID为 " + id + " 的员工");
        }
        return employee;
    }

    @Override
    public List<Employee> findByDepartment(Integer department) {
        if (department == null) {
            throw new IllegalArgumentException("部门ID不能为空");
        }
        return employeeMapper.findByDepartment(department);
    }

    @Override
    public List<Employee> findAll() {
        return employeeMapper.findAll();
    }

    @Override
    public Employee findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        return employeeMapper.findByUsername(username.trim());
    }

    @Override
    public void save(Employee employee) {
        // 参数校验
        if (employee == null) {
            throw new IllegalArgumentException("员工信息不能为空");
        }
        if (employee.getUsername() == null || employee.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        // 检查用户名是否唯一
        if (!isUsernameUnique(employee.getUsername())) {
            throw new RuntimeException("用户名已存在: " + employee.getUsername());
        }

        employeeMapper.save(employee);
    }

    @Override
    public void update(Employee employee) {
        // 参数校验
        if (employee == null || employee.getId() == null) {
            throw new IllegalArgumentException("员工信息或ID不能为空");
        }

        // 检查员工是否存在
        Employee existing = employeeMapper.findById(employee.getId());
        if (existing == null) {
            throw new RuntimeException("员工不存在，ID: " + employee.getId());
        }

        // 如果修改了用户名，检查新用户名是否唯一
        if (!Objects.equals(existing.getUsername(), employee.getUsername())) {
            if (!isUsernameUnique(employee.getUsername())) {
                throw new RuntimeException("用户名已存在: " + employee.getUsername());
            }
        }

        employeeMapper.update(employee);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("员工ID不能为空");
        }

        // 检查员工是否存在
        Employee existing = employeeMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("员工不存在，ID: " + id);
        }

        employeeMapper.deleteById(id);
    }

    @Override
    public Employee login(String username, String password) {
        return employeeMapper.login(username, password);
    }

    /**
     * 检查用户名是否唯一
     * @param username 要检查的用户名
     * @return true表示唯一，false表示已存在
     */
    private boolean isUsernameUnique(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        try {
            Employee existing = employeeMapper.findByUsername(username.trim());
            return existing == null; // 如果为null，说明用户名唯一
        } catch (Exception e) {
            // 出现异常时保守起见返回false
            return false;
        }
    }

}
