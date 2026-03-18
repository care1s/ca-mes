package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购订单实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long id;

    /** 订单编号 */
    private String orderNo;

    /** 关联申请ID */
    private Long requestId;

    /** 关联申请单号 */
    private String requestNo;

    /** 供应商ID */
    private Long vendorId;

    /** 供应商编码 */
    private String vendorCode;

    /** 供应商名称 */
    private String vendorName;

    /** 订单日期 */
    private Date orderDate;

    /** 交货日期 */
    private Date deliveryDate;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 订单总数量 */
    private BigDecimal totalQuantity;

    /** 币种 */
    private String currency;

    /** 订单状态 */
    private String orderStatus;

    /** 审核状态 */
    private String auditStatus;

    /** 联系人 */
    private String contactName;

    /** 联系电话 */
    private String contactPhone;

    /** 交货地址 */
    private String deliveryAddress;

    /** 备注 */
    private String remark;

    /** 订单明细列表 */
    private List<PurOrderItem> items;
}
