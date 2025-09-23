package com.csi.controller.market;

import com.csi.util.QRCodeUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.OutputStream;

@Controller
@RequestMapping("/market/qrcode")
public class QrCodeController {

    // 扫码跳转的表单页面
    private static final String SCAN_FORM_URL = "https://99b1da859eff.ngrok-free.app";

    /**
     * 生成普通活动二维码（不带logo）
     */
    @GetMapping("/common")
    public void createCommonQRCode(
            @RequestParam("activityId") Integer activityId,
            @RequestParam("creatorId") Integer creatorId,
            HttpServletResponse response
    ) {
        try {
            // 1. 构建二维码内容（活动ID+表单链接）
            String content = SCAN_FORM_URL + "?activityId=" + activityId + "&creatorId=" + creatorId;

            // 2. 设置响应头（告诉浏览器返回图片，禁止缓存）
            setupResponseHeaders(response);

            // 3. 生成二维码并写入响应流
            OutputStream stream = response.getOutputStream();
            QRCodeUtil.encode(content, stream);

        } catch (Exception e) {
            handleException(response, "二维码生成失败：" + e.getMessage());
        }
    }

    /**
     * 生成带logo的活动二维码（logo放在 src/main/resources/logo.jpg）
     */
    @GetMapping("/withLogo")
    public void createLogoQRCode(
            @RequestParam("activityId") Integer activityId,
            HttpServletResponse response
    ) {
        try {
            String content = SCAN_FORM_URL + "?activityId=" + activityId;
            setupResponseHeaders(response);

            OutputStream stream = response.getOutputStream();
            // 调用工具类生成带logo的二维码（logo路径是 src/main/resources/logo.jpg）
            QRCodeUtil.encode(content, "logo.jpg", stream, true);

        } catch (Exception e) {
            handleException(response, "带logo二维码生成失败：" + e.getMessage());
        }
    }

    // 提取公共方法设置响应头
    private void setupResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setDateHeader("Expires", 0);
    }

    // 提取公共异常处理方法
    private void handleException(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
