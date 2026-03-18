package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购入库实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurReceipt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 入库ID */
    private Long id;

    /** 入库单号 */
    private String receiptNo;

    /** 关联订单ID */
    private Long orderId;

    /** 关联订单号 */
    private String orderNo;

    /** 供应商ID */
    private Long vendorId;

    /** 供应商编码 */
    private String vendorCode;

    /** 供应商名称 */
    private String vendorName;

    /** 入库日期 */
    private Date receiptDate;

    /** 入库金额 */
    private BigDecimal totalAmount;

    /** 入库总数量 */
    private BigDecimal totalQuantity;

    /** 合格数量 */
    private BigDecimal qualifiedQty;

    /** 不合格数量 */
    private BigDecimal unqualifiedQty;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 状态 */
    private String status;

    /** 审核状态 */
    private String auditStatus;

    /** 入库状态 */
    private String stockInStatus;

    /** 质检员ID */
    private Long inspectorId;

    /** 质检员名称 */
    private String inspectorName;

    /** 质检日期 */
    private Date inspectionDate;

    /** 质检备注 */
    private String inspectionRemark;

    /** 备注 */
    private String remark;

    /** 入库明细列表 */
    private List<PurReceiptItem> items;
}
