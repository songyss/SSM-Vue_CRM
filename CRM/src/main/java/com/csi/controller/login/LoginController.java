package com.csi.controller.login;

import com.csi.annotation.OperateLog;
import com.csi.domain.Employee;
import com.csi.service.EmployeeService;
import com.csi.util.JwtTokenUtils;
import com.csi.util.PasswordUtil;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    @OperateLog(operation = "登录",targetType = "员工")
    public R login(@RequestBody Map<String, String> loginParam){


        String username = loginParam.get("username");
        String password = loginParam.get("password");

        Employee login = employeeService.login(username);
        boolean verify = PasswordUtil.verify(login.getPassword(), password);

        if (!verify) { // 实际需加密校验
            return R.loginFail();
        }

        String token = JwtTokenUtils.generateToken(login);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", Map.of(
                "userId", login.getId(),
                "name", login.getName(),
                "role", login.getRole(),
                "department", login.getDepartment()
        ));

        return R.loginSuccess(data);

    }

}
