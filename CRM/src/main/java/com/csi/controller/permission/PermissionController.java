package com.csi.controller.permission;


import com.csi.domain.RolePermission;
import com.csi.service.RolePermissionService;
import com.csi.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @GetMapping("/getPermission")
    public R getPermission(@RequestParam("roleId") int roleId){
        //拿着角色id 找到角色模块权限集合
        List<RolePermission> rolePermissions = rolePermissionService.selectPermissions(roleId);
        Map<Integer,List<String>> map = new HashMap<>();
        //遍历集合
        for (RolePermission rolePermission : rolePermissions) {
            //拿到模块id
            Integer modelId = rolePermission.getModelId();
            //拿到权限集合
            String permissionsId = rolePermission.getPermissionsId();
            String[] parts = permissionsId.split(",");
            List<Integer> permissionsIdList = new ArrayList<>();
            for (String part : parts) {
                int num = Integer.parseInt(part);
                permissionsIdList.add(num);
            }
            List<String> urlList = new ArrayList<>();
            for (Integer i : permissionsIdList) {
                String url = rolePermissionService.selectPermissionByList(i);
                urlList.add(url);
            }
            //一个模块对应一个权限集合
            map.put(modelId,urlList);
        }
        //返回map
        return R.ok(map);
    }

}
