package com.csi.controller.ai;

import com.csi.service.AiScriptService;
import com.csi.util.R;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ai/script")
public class AiScriptController {

    @Resource
    private AiScriptService aiScriptService;

    @PostMapping("/generate")
    public R generateScript(@RequestBody Map<String, Object> params) {
        Integer customerId = (Integer) params.get("customerId");
        String scenario = (String) params.getOrDefault("scenario", "客户沟通");
        String result = aiScriptService.generate(customerId, scenario);

        if (result.startsWith("AI生成失败") || result.startsWith("未找到")) {
            return R.error().message(result);
        }
        return R.ok(result);
    }
}
