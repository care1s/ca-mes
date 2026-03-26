package com.carels.mes.module.sale.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售退货单实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleReturn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 退货单ID */
    private Long returnId;

    /** 退货单号 */
    private String returnNo;

    /** 原出库单ID */
    private Long deliveryId;

    /** 原出库单号 */
    private String deliveryNo;

    /** 销售订单ID */
    private Long orderId;

    /** 客户ID */
    private Long clientId;

    /** 客户名称 */
    private String clientName;

    /** 退货日期 */
    private Date returnDate;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 退货总金额 */
    private BigDecimal totalAmount;

    /** 退货总数量 */
    private BigDecimal totalQuantity;

    /** 状态: 0-草稿, 1-已提交, 2-已审核, 3-已入库 */
    private String status;

    /** 退货原因 */
    private String returnReason;

    /** 备注 */
    private String remark;

    /** 退货明细列表 */
    private List<SaleReturnLine> lines;
}
