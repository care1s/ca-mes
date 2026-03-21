package com.carels.mes.module.wm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class WmRecpt extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String recptNo;
    private String recptType;
    private String sourceType;
    private Long sourceId;
    private String sourceNo;
    private Long warehouseId;
    private String warehouseName;
    private String status;
    private Date recptDate;
    private String remark;

    // ==================== 采购入库特有字段 ====================
    /** 供应商ID */
    private Long vendorId;

    /** 供应商编码 */
    private String vendorCode;

    /** 供应商名称 */
    private String vendorName;

    /** 采购订单ID */
    private Long orderId;

    /** 采购订单号 */
    private String orderNo;

    /** 入库总金额 */
    private java.math.BigDecimal totalAmount;

    /** 入库总数量 */
    private java.math.BigDecimal totalQuantity;

    /** 合格数量 */
    private java.math.BigDecimal qualifiedQty;

    /** 不合格数量 */
    private java.math.BigDecimal unqualifiedQty;

    /** 质检员ID */
    private Long inspectorId;

    /** 质检员名称 */
    private String inspectorName;

    /** 质检日期 */
    private Date inspectionDate;

    /** 质检备注 */
    private String inspectionRemark;

    /** 审核状态 */
    private String auditStatus;

    // =========================================================

    private List<WmRecptItem> items;
}
