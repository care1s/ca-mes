package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓区实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmZone extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 仓区ID */
    private Long zoneId;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库编码 */
    private String warehouseCode;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 仓区编码 */
    private String zoneCode;
    
    /** 仓区名称 */
    private String zoneName;
    
    /** 仓区类型: RECEIVE-收货区, SHIP-发货区, STORAGE-存储区, PICK-拣货区, SPECIAL-特殊区 */
    private String zoneType;
    
    /** 状态: 0-启用, 1-停用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
