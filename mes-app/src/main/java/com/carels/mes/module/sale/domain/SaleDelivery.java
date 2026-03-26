package com.carels.mes.module.sale.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售出库单实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SaleDelivery extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 出库单ID */
    private Long deliveryId;

    /** 出库单号 */
    private String deliveryNo;

    /** 销售订单ID */
    private Long orderId;

    /** 销售订单号 */
    private String orderNo;

    /** 客户ID */
    private Long clientId;

    /** 客户名称 */
    private String clientName;

    /** 出库日期 */
    private Date deliveryDate;

    /** 仓库ID */
    private Long warehouseId;

    /** 仓库名称 */
    private String warehouseName;

    /** 出库总金额 */
    private BigDecimal totalAmount;

    /** 出库总数量 */
    private BigDecimal totalQuantity;

    /** 状态: 0-草稿, 1-已提交, 2-已审核, 3-已发货, 4-已完成 */
    private String status;

    /** 物流单号 */
    private String trackingNo;

    /** 物流公司 */
    private String logisticsCompany;

    /** 发货日期 */
    private Date shipDate;

    /** 收货人 */
    private String receiverName;

    /** 收货电话 */
    private String receiverPhone;

    /** 收货地址 */
    private String receiverAddress;

    /** 备注 */
    private String remark;

    /** 出库明细列表 */
    private List<SaleDeliveryLine> lines;
}
