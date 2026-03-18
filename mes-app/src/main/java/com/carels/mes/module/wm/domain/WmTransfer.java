package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存调拨实体类
 */
@Data
public class WmTransfer {
    
    /** 调拨ID */
    private Long transferId;
    
    /** 调拨单号 */
    private String transferNo;
    
    /** 调出仓库ID */
    private Long fromWarehouseId;
    
    /** 调出仓库名称 */
    private String fromWarehouseName;
    
    /** 调入仓库ID */
    private Long toWarehouseId;
    
    /** 调入仓库名称 */
    private String toWarehouseName;
    
    /** 调拨日期 */
    private LocalDate transferDate;
    
    /** 调拨状态: 0-草稿, 1-待出库, 2-已出库, 3-已入库, 4-已取消 */
    private Integer status;
    
    /** 调拨类型: 0-普通调拨, 1-紧急调拨 */
    private Integer transferType;
    
    /** 总数量 */
    private Double totalQty;
    
    /** 总件数 */
    private Integer totalCount;
    
    /** 申请人ID */
    private Long applicantId;
    
    /** 申请人姓名 */
    private String applicantName;
    
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
