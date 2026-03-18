package com.carels.mes.module.system.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统角色实体类
 */
@Data
public class SysRole {
    
    /** 角色ID */
    private Long roleId;
    
    /** 角色名称 */
    private String roleName;
    
    /** 角色编码 */
    private String roleCode;
    
    /** 显示顺序 */
    private Integer orderNum;
    
    /** 数据范围: 1-全部数据, 2-本部门数据, 3-本部门及以下数据, 4-仅本人数据 */
    private Integer dataScope;
    
    /** 状态: 0-正常, 1-停用 */
    private Integer status;
    
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
