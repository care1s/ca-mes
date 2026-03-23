package com.carels.mes.module.sale.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单明细实体类
 */
@Data
public class SaleOrderItem {

    /** 明细ID */
    private Long itemId;

    /** 订单ID */
    private Long orderId;

    /** 物料ID */
    private Long itemId2;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String specification;

    /** 单位 */
    private String unit;

    /** 订单数量 */
    private BigDecimal quantity;

    /** 已发货数量 */
    private BigDecimal deliveredQty;

    /** 剩余数量 */
    private BigDecimal remainQty;

    /** 单价 */
    private BigDecimal unitPrice;

    /** 总价 */
    private BigDecimal totalPrice;

    /** 状态: 0-正常, 1-已完成, 2-已取消 */
    private Integer status;

    /** 备注 */
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private LocalDateTime createTime;
}
