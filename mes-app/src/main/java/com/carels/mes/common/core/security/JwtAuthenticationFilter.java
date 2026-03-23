package com.carels.mes.common.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Token 过滤器 - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Value("${mes.jwt.secret:carels-mes-secret-key-2026}")
    private String jwtSecret;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 获取请求路径
        String requestPath = request.getRequestURI();

        // 登录相关接口不需要验证token
        if (requestPath.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT Secret from @Value: {}", jwtSecret);

        // 从header中获取token
        String header = request.getHeader("Authorization");
        
        // 如果没有token，返回401
        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("请求未携带token: {}", requestPath);
            writeUnauthorizedResponse(response, "请先登录");
            return;
        }
        
        String token = header.substring(7);
        
        try {
            // 确保密钥长度足够（HS256需要至少256位）
            String secret = jwtSecret;
            log.debug("Original JWT Secret: {}", secret);
            if (secret.length() < 32) {
                secret = secret + "-carels-mes-padding-to-32chars";
                log.debug("Padded JWT Secret: {}", secret);
            }
            // 解析token
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            
            String username = claims.getSubject();
            
            if (username != null) {
                // 设置认证信息到Spring Security上下文
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                username, 
                                null, 
                                Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT验证成功, 用户: {}", username);
                filterChain.doFilter(request, response);
            } else {
                log.error("JWT token中无用户名");
                writeUnauthorizedResponse(response, "登录已过期，请重新登录");
            }
        } catch (Exception e) {
            log.error("JWT验证失败: {}, jwtSecret used: {}", e.getMessage(), jwtSecret);
            SecurityContextHolder.clearContext();
            writeUnauthorizedResponse(response, "登录已过期，请重新登录");
        }
    }
    
    /**
     * 写入401响应
     */
    private void writeUnauthorizedResponse(HttpServletResponse response, String msg) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("msg", msg);
        result.put("data", null);
        
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
