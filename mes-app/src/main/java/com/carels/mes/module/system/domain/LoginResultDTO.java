package com.carels.mes.module.system.domain;

import lombok.Data;

import java.util.Set;

/**
 * 登录返回DTO - carels
 * 
 * @author carels
 * @version V1.0
 * @date 2026-03-15
 */
@Data
public class LoginResultDTO {
    
    /** Token */
    private String token;
    
    /** 用户名 */
    private String userName;
    
    /** 用户昵称 */
    private String nickName;
    
    /** 用户头像 */
    private String avatar;
    
    /** 角色列表 */
    private Set<String> roles;
    
    /** 权限列表 */
    private Set<String> permissions;
}
