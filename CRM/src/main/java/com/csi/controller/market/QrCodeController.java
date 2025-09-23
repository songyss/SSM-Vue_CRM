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
        OutputStream stream = null;
        try {
            // 1. 构建二维码内容（活动ID+表单链接）
            String content = SCAN_FORM_URL + "?activityId=" + activityId + "&creatorId=" + creatorId;

            // 2. 设置响应头（告诉浏览器返回图片，禁止缓存）
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);

            // 3. 生成二维码并写入响应流
            stream = response.getOutputStream(); // 这里用的是 jakarta 的 OutputStream
            QRCodeUtil.encode(content, stream);

        } catch (Exception e) {
            // 异常时返回文字提示
            response.setContentType("text/plain;charset=UTF-8");
            try {
                response.getWriter().write("二维码生成失败：" + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            // 关闭流（避免资源泄漏）
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
        OutputStream stream = null;
        try {
            String content = SCAN_FORM_URL + "?activityId=" + activityId;
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);

            stream = response.getOutputStream();
            // 调用工具类生成带logo的二维码（logo路径是 src/main/resources/logo.jpg）
            QRCodeUtil.encode(content, "logo.jpg", stream, true);

        } catch (Exception e) {
            response.setContentType("text/plain;charset=UTF-8");
            try {
                response.getWriter().write("带logo二维码生成失败：" + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            if (stream != null) {
                try {
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}