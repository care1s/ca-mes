package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存盘点明细实体类
 */
@Data
public class WmStocktakingItem {
    
    /** 明细ID */
    private Long itemId;
    
    /** 盘点单ID */
    private Long stocktakingId;
    
    /** 物料ID */
    private Long itemId2;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 库位ID */
    private Long locationId;
    
    /** 库位编码 */
    private String locationCode;
    
    /** 批次号 */
    private String batchNo;
    
    /** 账面数量 */
    private Double bookQty;
    
    /** 实盘数量 */
    private Double actualQty;
    
    /** 差异数量 */
    private Double diffQty;
    
    /** 单价 */
    private Double unitPrice;
    
    /** 差异金额 */
    private Double diffAmount;
    
    /** 盘点结果: 0-正常, 1-盘盈, 2-盘亏 */
    private Integer result;
    
    /** 备注 */
    private String remark;
    
    /** 创建者 */
    private String createBy;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}
