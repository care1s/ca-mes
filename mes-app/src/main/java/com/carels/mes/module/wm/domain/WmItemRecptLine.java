package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 入库单明细实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmItemRecptLine extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 明细ID */
    private Long lineId;
    
    /** 入库单ID */
    private Long recptId;
    
    /** 行号 */
    private Integer lineNo;
    
    /** 物料ID */
    private Long itemId;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 批次号 */
    private String batchCode;
    
    /** 数量 */
    private Double quantity;
    
    /** 已入库数量 */
    private Double quantityRecpted;
    
    /** 计量单位ID */
    private Long unitId;
    
    /** 计量单位名称 */
    private String unitName;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 库区ID */
    private Long locationId;
    
    /** 库区名称 */
    private String locationName;
    
    /** 库位ID */
    private Long areaId;
    
    /** 库位名称 */
    private String areaName;
    
    /** 状态: PENDING-待入库, COMPLETED-已完成 */
    private String status;
}
