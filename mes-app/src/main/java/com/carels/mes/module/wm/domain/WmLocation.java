package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓位实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmLocation extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 仓位ID */
    private Long locationId;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库编码 */
    private String warehouseCode;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 仓区ID */
    private Long zoneId;
    
    /** 仓区编码 */
    private String zoneCode;
    
    /** 仓区名称 */
    private String zoneName;
    
    /** 仓位编码 */
    private String locationCode;
    
    /** 仓位名称 */
    private String locationName;
    
    /** 巷道号 */
    private String aisleNo;
    
    /** 货架号 */
    private String shelfNo;
    
    /** 层号 */
    private String layerNo;
    
    /** 位号 */
    private String positionNo;
    
    /** 容量(托盘数) */
    private Integer capacity;
    
    /** 已用容量 */
    private Integer usedCapacity;
    
    /** 状态: 0-启用, 1-停用, 2-占用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
