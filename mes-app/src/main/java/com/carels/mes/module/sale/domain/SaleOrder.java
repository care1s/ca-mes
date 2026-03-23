package com.carels.mes.module.sale.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售订单实体类
 */
@Data
public class SaleOrder {

    /** 订单ID */
    private Long orderId;

    /** 订单编号 */
    private String orderNo;

    /** 订单日期 */
    private LocalDate orderDate;

    /** 客户ID */
    private Long clientId;

    /** 客户编码 */
    private String clientCode;

    /** 客户名称 */
    private String clientName;

    /** 交货日期 */
    private LocalDate deliveryDate;

    /** 交货地址 */
    private String deliveryAddress;

    /** 联系人 */
    private String contactPerson;

    /** 联系电话 */
    private String contactPhone;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 税额 */
    private BigDecimal taxAmount;

    /** 折扣金额 */
    private BigDecimal discountAmount;

    /** 应付金额 */
    private BigDecimal payableAmount;

    /** 订单状态: 0-草稿, 1-已提交, 2-已审核, 3-生产中, 4-部分出库, 5-已完成, 6-已取消 */
    private Integer status;

    /** 交货状态: 0-未发货, 1-部分发货, 2-已发货 */
    private Integer deliveryStatus;

    /** 关联生产计划ID */
    private Long relatedPlanId;

    /** 备注 */
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private LocalDateTime updateTime;

    /** 订单明细列表 */
    private List<SaleOrderItem> items;
}
