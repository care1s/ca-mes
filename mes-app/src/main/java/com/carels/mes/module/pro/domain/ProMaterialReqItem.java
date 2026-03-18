package com.carels.mes.module.pro.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 物料需求明细实体类
 */
@Data
public class ProMaterialReqItem {
    
    /** 明细ID */
    private Long itemId;
    
    /** 需求单ID */
    private Long reqId;
    
    /** 物料ID */
    private Long itemId2;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 需求数量 */
    private Double reqQty;
    
    /** 已发料数量 */
    private Double issuedQty;
    
    /** 单位 */
    private String unit;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 库存可用量 */
    private Double availableQty;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}
