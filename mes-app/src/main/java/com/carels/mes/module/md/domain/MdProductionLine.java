package com.carels.mes.module.md.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 生产线实体类 - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MdProductionLine extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 生产线ID */
    private Long lineId;
    
    /** 生产线编码 */
    private String lineCode;
    
    /** 生产线名称 */
    private String lineName;
    
    /** 所属车间ID */
    private Long workshopId;
    
    /** 所属车间名称 */
    private String workshopName;
    
    /** 生产线类型: ASSEMBLY-装配线, PROCESSING-加工线, PACKING-包装线, INSPECTION-检测线 */
    private String lineType;
    
    /** 产能(件/小时) */
    private BigDecimal capacity;
    
    /** 状态: 0-启用, 1-停用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
