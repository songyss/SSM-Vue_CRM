package com.csi.controller.permission;


import com.csi.domain.PermissionTreeNode;
import com.csi.domain.RolePermission;
import com.csi.service.PermissionTreeService;
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

    @Autowired
    private PermissionTreeService permissionTreeService;


    @GetMapping("/getPermission")
    public R getPermission(@RequestParam("roleId") int roleId) {
        //拿着角色id 找到角色模块权限集合
        List<RolePermission> rolePermissions = rolePermissionService.selectPermissions(roleId);
        Map<Integer, List<String>> map = new HashMap<>();
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
            map.put(modelId, urlList);
        }
        //返回map
        return R.ok(map);
    }

    @PostMapping("/savePermission")
    public R savePermission(@RequestParam("roleId") int roleId,
                            @RequestParam("modelsId") String modelsId,
                            @RequestParam("permissionsId") String permissionsId) {
        String[] modelIds = modelsId.split(",");
        List<Integer> modelsIdList = new ArrayList<>();
        for (String modelId : modelIds) {
            int num1 = Integer.parseInt(modelId);
            modelsIdList.add(num1);
        }
        String[] permissionIds = permissionsId.split(",");
        List<Integer> permissionIdList = new ArrayList<>();
        for (String permissionId : permissionIds) {
            int num2 = Integer.parseInt(permissionId);
            permissionIdList.add(num2);
        }

        // 用于存储每个模块ID对应的权限ID字符串
        Map<Integer, String> modelPermissionsMap = new HashMap<>();

        for (Integer modelId : modelsIdList) {
            List<String> permissionsForModel = new ArrayList<>();

            for (Integer permissionId : permissionIdList) {
                int permModelId = rolePermissionService.getModelIdByPermissionId(permissionId);
                if (modelId == permModelId) {
                    permissionsForModel.add(String.valueOf(permissionId));
                }
            }

            // 将权限ID列表拼接成以逗号分隔的字符串
            if (!permissionsForModel.isEmpty()) {
                String permissionsStr = String.join(",", permissionsForModel);
                modelPermissionsMap.put(modelId, permissionsStr);
            }
        }

        for (Map.Entry<Integer, String> entry : modelPermissionsMap.entrySet()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setModelId(entry.getKey());
            rolePermission.setPermissionsId(entry.getValue());
            rolePermission.setIsDelete(0);
            rolePermissionService.save(rolePermission);
        }
        return R.okMessage("权限保存成功");

    }

    @GetMapping("/tree")
    public R getPermissionTree() {
        List<PermissionTreeNode> tree = permissionTreeService.buildPermissionTree();
        return R.ok(tree);
    }

}
