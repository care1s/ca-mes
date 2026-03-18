package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购退货实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurReturn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 退货ID */
    private Long id;

    /** 退货单号 */
    private String returnNo;

    /** 关联入库ID */
    private Long receiptId;

    /** 关联入库单号 */
    private String receiptNo;

    /** 供应商ID */
    private Long vendorId;

    /** 供应商编码 */
    private String vendorCode;

    /** 供应商名称 */
    private String vendorName;

    /** 退货日期 */
    private Date returnDate;

    /** 退货金额 */
    private BigDecimal totalAmount;

    /** 退货总数量 */
    private BigDecimal totalQuantity;

    /** 退货原因 */
    private String returnReason;

    /** 状态 */
    private String status;

    /** 审核状态 */
    private String auditStatus;

    /** 出库状态 */
    private String stockOutStatus;

    /** 备注 */
    private String remark;

    /** 退货明细列表 */
    private List<PurReturnItem> items;
}
