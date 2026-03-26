package com.carels.mes.module.sale.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售对账实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleReconciliation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 对账单ID */
    private Long reconId;

    /** 对账单号 */
    private String reconNo;

    /** 客户ID */
    private Long clientId;

    /** 客户名称 */
    private String clientName;

    /** 对账开始日期 */
    private Date startDate;

    /** 对账结束日期 */
    private Date endDate;

    /** 出库总金额 */
    private BigDecimal deliveryAmount;

    /** 退货总金额 */
    private BigDecimal returnAmount;

    /** 应收金额 */
    private BigDecimal receivableAmount;

    /** 状态: 0-草稿, 1-已确认, 2-已开票, 3-已收款 */
    private String status;

    /** 发票号码 */
    private String invoiceNo;

    /** 开票日期 */
    private Date invoiceDate;

    /** 收款金额 */
    private BigDecimal receivedAmount;

    /** 收款日期 */
    private Date receiveDate;

    /** 备注 */
    private String remark;
}
