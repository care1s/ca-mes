package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购入库明细实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurReceiptItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 入库ID */
    private Long receiptId;

    /** 关联订单明细ID */
    private Long orderItemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String itemSpec;

    /** 单位 */
    private String unit;

    /** 入库数量 */
    private BigDecimal quantity;

    /** 合格数量 */
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    private BigDecimal unqualifiedQty;

    /** 批次号 */
    private String batchNo;

    /** 生产日期 */
    private Date productionDate;

    /** 有效期至 */
    private Date expiryDate;

    /** 备注 */
    private String remark;
}
