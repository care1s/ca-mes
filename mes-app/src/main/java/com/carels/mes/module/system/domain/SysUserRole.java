package com.carels.mes.module.system.domain;

import lombok.Data;

/**
 * 用户角色关联实体类
 */
@Data
public class SysUserRole {
    
    /** 用户ID */
    private Long userId;
    
    /** 角色ID */
    private Long roleId;
}
