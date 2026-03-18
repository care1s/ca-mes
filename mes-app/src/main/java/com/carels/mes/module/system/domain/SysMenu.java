package com.carels.mes.module.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统菜单实体类
 */
@Data
public class SysMenu {
    
    /** 菜单ID */
    private Long menuId;
    
    /** 菜单名称 */
    private String menuName;
    
    /** 父菜单ID */
    private Long parentId;
    
    /** 显示顺序 */
    private Integer orderNum;
    
    /** 路由地址 */
    private String path;
    
    /** 组件路径 */
    private String component;
    
    /** 菜单类型: M-目录, C-菜单, F-按钮 */
    private String menuType;
    
    /** 菜单状态: 0-显示, 1-隐藏 */
    private Integer visible;
    
    /** 菜单状态: 0-正常, 1-停用 */
    private Integer status;
    
    /** 权限标识 */
    private String perms;
    
    /** 菜单图标 */
    private String icon;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新者 */
    private String updateBy;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
