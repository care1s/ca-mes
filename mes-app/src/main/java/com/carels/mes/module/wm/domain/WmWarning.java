package com.carels.mes.module.wm.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存预警实体类
 */
@Data
public class WmWarning {
    
    /** 预警ID */
    private Long warningId;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 物料ID */
    private Long itemId;
    
    /** 物料编码 */
    private String itemCode;
    
    /** 物料名称 */
    private String itemName;
    
    /** 规格型号 */
    private String specification;
    
    /** 当前库存 */
    private Double currentQty;
    
    /** 安全库存 */
    private Double safetyQty;
    
    /** 最高库存 */
    private Double maxQty;
    
    /** 预警类型: 1-库存不足, 2-库存积压, 3-库存过期 */
    private Integer warningType;
    
    /** 预警级别: 1-一般, 2-严重, 3-紧急 */
    private Integer warningLevel;
    
    /** 预警状态: 0-未处理, 1-处理中, 2-已处理 */
    private Integer status;
    
    /** 预警说明 */
    private String warningDesc;
    
    /** 处理人ID */
    private Long handlerId;
    
    /** 处理人姓名 */
    private String handlerName;
    
    /** 处理时间 */
    private LocalDateTime handleTime;
    
    /** 处理备注 */
    private String handleRemark;
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}
