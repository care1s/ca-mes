package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存调拨明细实体类
 */
@Data
public class WmTransferItem {
    
    /** 明细ID */
    private Long itemId;
    
    /** 调拨单ID */
    private Long transferId;
    
    /** 物料ID */
    private Long itemId2;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 批次号 */
    private String batchNo;
    
    /** 申请数量 */
    private Double applyQty;
    
    /** 实际出库数量 */
    private Double outQty;
    
    /** 实际入库数量 */
    private Double inQty;
    
    /** 单位 */
    private String unit;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}
