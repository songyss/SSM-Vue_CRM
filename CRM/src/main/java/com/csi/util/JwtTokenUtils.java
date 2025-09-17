package com.csi.util;

import io.jsonwebtoken.*;
import com.csi.domain.Employee;
import io.jsonwebtoken.io.Decoders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 工具类（支撑需求“权限精细化”“数据安全性”）
 * 生成的 Token 携带用户 ID、角色、部门信息，用于接口权限校验
 */
public class JwtTokenUtils {
    // 1. 核心配置（需在配置文件中定义，此处简化）
    private static final String SECRET_KEY = "Y3JtX3N5c3RlbV9zZWNyZXRfa2V5XzIwMjRfWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFg="; // 签名密钥（需保密）
    private static final long EXPIRATION_TIME = 7200000; // Token 有效期：2小时（7200秒）

    /**
     * 生成 Token（登录成功后调用，匹配需求“用户身份验证”）
     * @param employee 登录用户信息（从 employees 表查询）
     * @return 加密后的 JWT Token
     */
    public static String generateToken(Employee employee) {
        // 2. Token 载荷：携带需求所需的用户权限信息
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", employee.getId()); // 用户唯一标识（关联 operation_logs 表 operator_id）
        claims.put("role", employee.getRole()); // 用户角色（1=员工/2=经理/3=超级管理员，匹配需求三级角色）
        claims.put("department", employee.getDepartment()); // 所属部门（1=市场部/2=电话销售部/3=销售部，匹配需求部门隔离）
        claims.put("username", employee.getUsername()); // 登录名（用于日志展示）

        // 3. 生成 Token 并签名
        return Jwts.builder()
                .setClaims(claims) // 写入载荷信息
                .setIssuedAt(new Date()) // Token 创建时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, Decoders.BASE64.decode(SECRET_KEY)) // HS256 加密签名（防篡改）
                .compact();
    }

    /**
     * 解析 Token（获取用户信息，用于接口权限校验）
     * @param token 前端传递的 Token
     * @return 用户信息（userId/role/department）
     * @throws ExpiredJwtException Token 过期
     * @throws MalformedJwtException Token 格式错误
     * @throws SignatureException Token 签名无效（可能被篡改）
     */
    public static Map<String, Object> parseToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Decoders.BASE64.decode(SECRET_KEY)) // 用相同密钥解析
                .build()
                .parseClaimsJws(token)
                .getBody();
        // 返回需求所需的核心信息（过滤敏感字段，如密码）
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userId", claims.get("userId", Integer.class));
        userInfo.put("role", claims.get("role", Integer.class));
        userInfo.put("department", claims.get("department", Integer.class));
        return userInfo;
    }

    /**
     * 验证 Token 有效性（避免无效/篡改 Token 访问接口）
     * @param token 前端传递的 Token
     * @return true=有效，false=无效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token); // 解析成功则说明 Token 有效
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}