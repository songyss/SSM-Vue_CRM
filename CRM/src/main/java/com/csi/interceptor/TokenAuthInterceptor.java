package com.csi.interceptor;

import com.csi.util.JwtTokenUtils;
import com.csi.util.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Token 权限拦截器（支撑需求“权限精细化”，拦截越权访问）
 */
public class TokenAuthInterceptor implements HandlerInterceptor {

    // 排除登录接口（无需 Token 访问）
    private static final String LOGIN_URL = "/user/login";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 跳过登录接口
        if (request.getRequestURI().equals(LOGIN_URL)) {
            return true;
        }

        // 2. 获取前端传递的 Token（从请求头 Authorization 中获取）
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(R.message("未登录，请先获取 Token")));
            return false;
        }

        // 3. 验证 Token 有效性（防过期、防篡改）
        if (!JwtTokenUtils.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(R.message("Token 无效或已过期，请重新登录")));
            return false;
        }

        // 4. 解析 Token 获取用户权限信息，用于后续接口角色校验
        Map<String, Object> userInfo = JwtTokenUtils.parseToken(token);
        // 将用户信息存入请求域，供 Controller 层获取（如校验“是否为销售部经理”）
        request.setAttribute("userId", userInfo.get("userId"));
        request.setAttribute("role", userInfo.get("role"));
        request.setAttribute("department", userInfo.get("department"));

        return true; // Token 有效且权限信息已存储，放行接口
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}