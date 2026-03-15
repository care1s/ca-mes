package com.carels.mes.common.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.Collections;

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
        
        // 从header中获取token
        String header = request.getHeader("Authorization");
        
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            
            try {
                // 解析token
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
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
                }
            } catch (Exception e) {
                log.error("JWT验证失败: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
