package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class WmStock extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long warehouseId;
    private String warehouseName;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private String batchCode;
    private BigDecimal quantity;
    private BigDecimal availableQty;
    private BigDecimal lockedQty;
    private String unit;
}
