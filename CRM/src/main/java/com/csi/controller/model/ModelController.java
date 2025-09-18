package com.csi.controller.model;

import com.csi.domain.Model;
import com.csi.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;

    @ResponseBody
    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Model> models = modelService.findAllModels();
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", models);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败: " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody Model model) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (model.getId() == null) {
                modelService.saveModel(model);
                result.put("code", 200);
                result.put("msg", "模块添加成功");
            } else {
                modelService.updateModel(model);
                result.put("code", 200);
                result.put("msg", "模块更新成功");
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "操作失败: " + e.getMessage());
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        try {
            modelService.deleteModel(id);
            result.put("code", 200);
            result.put("msg", "模块删除成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "删除失败: " + e.getMessage());
        }
        return result;
    }
}
