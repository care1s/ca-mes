package com.carels.mes.module.system.domain;

import lombok.Data;

/**
 * 角色菜单关联实体类
 */
@Data
public class SysRoleMenu {
    
    /** 角色ID */
    private Long roleId;
    
    /** 菜单ID */
    private Long menuId;
}
