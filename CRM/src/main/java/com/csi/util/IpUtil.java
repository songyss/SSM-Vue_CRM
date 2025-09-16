package com.csi.util;

import javax.servlet.http.HttpServletRequest;

/**
 * IP地址获取工具类（支撑需求ADM-002操作日志的ip_address记录）
 */
public class IpUtil {

    /**
     * 获取客户端真实IP（适配代理/负载均衡场景）
     * @param request Http请求对象
     * @return 真实IP地址（若无法获取则返回"unknown"）
     */
    public static String getRealIp(HttpServletRequest request) {
        // 1. 优先从X-Forwarded-For头获取（代理服务器转发时携带真实IP，格式：真实IP,代理IP1,代理IP2）
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 截取第一个IP（X-Forwarded-For可能包含多个IP，第一个为真实客户端IP）
            if (ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            return ip;
        }

        // 2. 若X-Forwarded-For不存在，从X-Real-IP头获取（部分代理服务器使用该字段）
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 3. 若以上都不存在，从Proxy-Client-IP头获取（Apache代理时使用）
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 4. 若以上都不存在，从WL-Proxy-Client-IP头获取（WebLogic代理时使用）
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // 5. 最后从request获取RemoteAddr（无代理时直接获取客户端IP）
        ip = request.getRemoteAddr();
        // 处理本地测试场景（返回127.0.0.1或0:0:0:0:0:0:0:1时，统一转为"127.0.0.1"）
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}