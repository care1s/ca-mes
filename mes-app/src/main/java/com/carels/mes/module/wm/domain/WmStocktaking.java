package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存盘点实体类
 */
@Data
public class WmStocktaking {
    
    /** 盘点ID */
    private Long stocktakingId;
    
    /** 盘点单号 */
    private String stocktakingNo;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 盘点类型: 0-全盘, 1-抽盘, 2-动碰盘 */
    private Integer stocktakingType;
    
    /** 盘点日期 */
    private LocalDate stocktakingDate;
    
    /** 盘点状态: 0-草稿, 1-盘点中, 2-已完成, 3-已取消 */
    private Integer status;
    
    /** 盘点人ID */
    private Long stocktakerId;
    
    /** 盘点人姓名 */
    private String stocktakerName;
    
    /** 计划盘点数量 */
    private Integer planCount;
    
    /** 实际盘点数量 */
    private Integer actualCount;
    
    /** 盘盈数量 */
    private Integer profitCount;
    
    /** 盘亏数量 */
    private Integer lossCount;
    
    /** 差异金额 */
    private Double diffAmount;
    
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
