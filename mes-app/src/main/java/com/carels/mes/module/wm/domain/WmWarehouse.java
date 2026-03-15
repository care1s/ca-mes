package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓库实体类 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WmWarehouse extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /** 仓库ID */
    private Long warehouseId;
    
    /** 仓库编码 */
    private String warehouseCode;
    
    /** 仓库名称 */
    private String warehouseName;
    
    /** 仓库类型: RAW-原材料仓, SEMI-半成品仓, PRODUCT-成品仓, SPARE-备件仓 */
    private String warehouseType;
    
    /** 负责人ID */
    private Long managerId;
    
    /** 负责人名称 */
    private String managerName;
    
    /** 联系电话 */
    private String phone;
    
    /** 地址 */
    private String address;
    
    /** 状态: 0-启用, 1-停用 */
    private String status;
    
    /** 备注 */
    private String remark;
}
