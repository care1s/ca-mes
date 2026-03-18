package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 采购退货明细实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurReturnItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 退货ID */
    private Long returnId;

    /** 关联入库明细ID */
    private Long receiptItemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String itemSpec;

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
