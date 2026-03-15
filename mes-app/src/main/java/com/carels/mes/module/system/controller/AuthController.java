package com.carels.mes.module.system.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.system.domain.CaptchaUtil;
import com.carels.mes.module.system.domain.LoginDTO;
import com.carels.mes.module.system.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录控制器 - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private LoginService loginService;
    
    // 验证码缓存，key: captchaKey, value: code
    private static final Map<String, String> captchaCache = new ConcurrentHashMap<>();
    
    /**
     * 用户登录
     * 
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody @Validated LoginDTO loginDTO) {
        // 验证码校验
        String cacheCode = captchaCache.get(loginDTO.getCaptchaKey());
        if (cacheCode == null) {
            return AjaxResult.error("验证码已过期，请重新获取");
        }
        if (!cacheCode.equalsIgnoreCase(loginDTO.getCaptcha())) {
            return AjaxResult.error("验证码错误");
        }
        // 验证通过后删除验证码
        captchaCache.remove(loginDTO.getCaptchaKey());
        
        log.info("用户登录请求: {}, 终端类型: {}", loginDTO.getUsername(), loginDTO.getTerminalType());
        return loginService.login(loginDTO);
    }
    
    /**
     * 用户登出
     * 
     * @return 结果
     */
    @PostMapping("/logout")
    public AjaxResult logout() {
        // 实际项目中应该将token加入黑名单
        return AjaxResult.success("登出成功");
    }
    
    /**
     * 获取验证码
     * 
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public AjaxResult getCaptcha() {
        // 生成4位随机验证码
        StringBuilder code = new StringBuilder();
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 4; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }
        String captchaCode = code.toString();
        
        // 生成唯一key
        String captchaKey = java.util.UUID.randomUUID().toString();
        
        // 保存到缓存（5分钟过期）
        captchaCache.put(captchaKey, captchaCode);
        
        // 5分钟后自动删除
        new Thread(() -> {
            try {
                Thread.sleep(5 * 60 * 1000);
                captchaCache.remove(captchaKey);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("captchaKey", captchaKey);
        result.put("captchaCode", captchaCode);  // 返回验证码值，前端用来生成图片
        
        return AjaxResult.success("获取成功", result);
    }
}
