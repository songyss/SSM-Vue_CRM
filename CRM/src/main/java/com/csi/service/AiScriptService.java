package com.csi.service;

import com.csi.domain.Customer;
import com.csi.mapper.CustomerMapper;
import com.csi.util.AiClient;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AiScriptService {

    @Autowired
    private CustomerMapper customerMapper;

    @Resource
    private AiClient aiClient;

    public String generate(Integer customerId, String scenario) {
        // 1. 获取客户信息
        Customer customer = customerMapper.findById(customerId);

        if (customer == null) {
            return "未找到该客户信息，无法生成话术";
        }

        // 2. 构造提示词
        String prompt = "请生成一段适合【" + scenario + "】的沟通话术。"
                + "客户姓名：" + customer.getName()
                + "，性别：" + customer.getSex()
                + "，职位：" + customer.getPosition()
                + "，公司：" + customer.getCompany()
                + "。话术要礼貌自然，贴合客户背景。";

        // 3. 调用AI
        return aiClient.chat(prompt);
    }
}
