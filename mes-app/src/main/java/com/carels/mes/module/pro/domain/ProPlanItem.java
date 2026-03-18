package com.carels.mes.module.pro.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 生产计划明细实体类
 */
@Data
public class ProPlanItem {
    
    /** 明细ID */
    private Long itemId;
    
    /** 计划ID */
    private Long planId;
    
    /** 物料ID */
    private Long itemId2;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 计划数量 */
    private Double planQty;
    
    /** 实际数量 */
    private Double actualQty;
    
    /** 单位 */
    private String unit;
    
    /** 生产线ID */
    private Long lineId;
    
    /** 生产线名称 */
    private String lineName;
    
    /** 优先级: 0-普通, 1-紧急, 2-特急 */
    private Integer priority;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}
