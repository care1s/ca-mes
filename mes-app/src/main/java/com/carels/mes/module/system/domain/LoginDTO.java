package com.carels.mes.module.system.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录请求DTO - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Data
public class LoginDTO {
    
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /** 终端类型: PC/WEB/PDA/TERMINAL */
    private String terminalType;
    
    /** 验证码 */
    private String captcha;
    
    /** 验证码Key */
    private String captchaKey;
}
