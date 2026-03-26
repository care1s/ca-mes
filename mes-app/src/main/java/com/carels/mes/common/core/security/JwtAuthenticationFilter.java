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

        log.info("JWT Filter - Request: {}, Secret from config: {}", requestPath, jwtSecret);

        // 从header中获取token
        String header = request.getHeader("Authorization");
        log.info("JWT Filter - Authorization header: {}", header);

        // 如果没有token，返回401
        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("请求未携带token: {}", requestPath);
            writeUnauthorizedResponse(response, "请先登录");
            return;
        }

        String token = header.substring(7);
        log.info("JWT Filter - Token: {}", token.substring(0, Math.min(30, token.length())) + "...");

        try {
            // 确保密钥长度足够（HS256需要至少256位）
            String secret = jwtSecret;
            if (secret.length() < 32) {
                secret = secret + "-carels-mes-padding-to-32chars";
            }
            log.info("JWT Filter - Using secret (padded): {}", secret);

            // 解析token
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            log.info("JWT Filter - Token valid, username: {}", username);

            if (username != null) {
                // 设置认证信息到Spring Security上下文
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            } else {
                log.error("JWT token中无用户名");
                writeUnauthorizedResponse(response, "登录已过期，请重新登录");
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.error("JWT token已过期: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            writeUnauthorizedResponse(response, "登录已过期，请重新登录");
        } catch (io.jsonwebtoken.JwtException e) {
            log.error("JWT验证失败: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            writeUnauthorizedResponse(response, "登录已过期，请重新登录");
        } catch (Exception e) {
            // 其他异常（如业务异常）不应被JWT过滤器捕获，应继续传播
            log.error("JWT Filter - 发生非JWT相关异常，继续传播: {}", e.getMessage());
            throw e;
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
