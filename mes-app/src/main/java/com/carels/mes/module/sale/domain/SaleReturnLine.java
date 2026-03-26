package com.carels.mes.module.sale.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 销售退货单明细实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleReturnLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long lineId;

    /** 退货单ID */
    private Long returnId;

    /** 出库明细ID */
    private Long deliveryLineId;

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

    /** 退货数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal price;

    /** 金额 */
    private BigDecimal amount;

    /** 批次号 */
    private String batchNo;

    /** 退货原因 */
    private String returnReason;

    /** 备注 */
    private String remark;
}
