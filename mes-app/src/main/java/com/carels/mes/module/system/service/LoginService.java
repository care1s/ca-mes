package com.carels.mes.module.system.service;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.system.domain.LoginDTO;
import com.carels.mes.module.system.domain.LoginResultDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 登录服务 - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Slf4j
@Service
public class LoginService {
    
    @Value("${mes.jwt.secret:carels-mes-secret-key-2026}")
    private String jwtSecret;
    
    @Value("${mes.jwt.expiration:86400000}")
    private Long jwtExpiration;
    
    /**
     * 登录验证
     * 
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    public AjaxResult login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        
        // 验证用户名密码
        // 这里暂时使用硬编码验证，实际应该查询数据库
        if (!"admin".equals(username) || !"123456".equals(password)) {
            return AjaxResult.error("用户名或密码错误");
        }
        
        // 生成JWT Token
        String token = generateToken(username);
        
        // 构建返回结果
        LoginResultDTO result = new LoginResultDTO();
        result.setToken(token);
        result.setUserName(username);
        result.setNickName("管理员");
        result.setAvatar("");
        result.setDeptName("采购部"); // 设置默认部门
        
        // 设置角色和权限
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        result.setRoles(roles);
        
        Set<String> permissions = new HashSet<>();
        permissions.add("*:*");
        result.setPermissions(permissions);
        
        log.info("用户 {} 登录成功，终端类型: {}", username, loginDTO.getTerminalType());
        
        return AjaxResult.success("登录成功", result);
    }
    
    /**
     * 生成JWT Token
     * 
     * @param username 用户名
     * @return Token
     */
    private String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        // 确保密钥长度足够（HS256需要至少256位）
        String secret = jwtSecret;
        if (secret.length() < 32) {
            secret = secret + "-carels-mes-padding-to-32chars";
        }

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    
    /**
     * 获取当前登录用户
     * 
     * @param token Token
     * @return 用户名
     */
    public String getUserNameFromToken(String token) {
        try {
            // 确保密钥长度足够（HS256需要至少256位）
            String secret = jwtSecret;
            if (secret.length() < 32) {
                secret = secret + "-carels-mes-padding-to-32chars";
            }
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.error("解析Token失败", e);
            return null;
        }
    }
}
