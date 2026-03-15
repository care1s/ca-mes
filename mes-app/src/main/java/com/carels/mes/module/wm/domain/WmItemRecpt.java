package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 入库单实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmItemRecpt extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 入库单ID */
    private Long recptId;
    
    /** 入库单号 */
    private String recptCode;
    
    /** 入库类型: PURCHASE-采购入库, PRODUCT-产品入库, OUTSOURCE-委外入库, MISC-杂项入库 */
    private String recptType;
    
    /** 供应商ID */
    private Long vendorId;
    
    /** 供应商名称 */
    private String vendorName;
    
    /** 来源单号 */
    private String sourceCode;
    
    /** 来源类型 */
    private String sourceType;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 入库日期 */
    private Date recptDate;
    
    /** 入库状态: PENDING-待入库, PARTIAL-部分入库, COMPLETED-已完成, CANCELLED-已取消 */
    private String status;
    
    /** 检验状态: PENDING-待检, INSPECTING-检验中, PASSED-合格, FAILED-不合格 */
    private String inspectStatus;
    
    /** 入库人ID */
    private Long operatorId;
    
    /** 入库人名称 */
    private String operatorName;
    
    /** 总金额 */
    private Double totalAmount;
    
    /** 币种 */
    private String currency;
    
    /** 备注 */
    private String remark;
    
    /** 入库明细列表 */
    private List<WmItemRecptLine> lines;
}
