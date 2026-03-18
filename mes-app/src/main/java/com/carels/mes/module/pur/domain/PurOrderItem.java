package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购订单明细实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurOrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 订单ID */
    private Long orderId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String itemSpec;

    /** 单位 */
    private String unit;

    /** 采购数量 */
    private BigDecimal quantity;

    /** 已收货数量 */
    private BigDecimal receivedQty;

    /** 单价 */
    private BigDecimal price;

    /** 金额 */
    private BigDecimal amount;

    /** 交货日期 */
    private Date deliveryDate;

    /** 备注 */
    private String remark;
}
