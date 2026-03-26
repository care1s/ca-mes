package com.carels.mes.module.sale.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 销售出库单明细实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleDeliveryLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long lineId;

    /** 出库单ID */
    private Long deliveryId;

    /** 销售订单明细ID */
    private Long orderLineId;

    /** 物料ID */
    private Long itemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String specification;

    /** 单位 */
    private String unit;

    /** 出库数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal price;

    /** 金额 */
    private BigDecimal amount;

    /** 批次号 */
    private String batchNo;

    /** 库位ID */
    private Long locationId;

    /** 库位名称 */
    private String locationName;

    /** 备注 */
    private String remark;
}
