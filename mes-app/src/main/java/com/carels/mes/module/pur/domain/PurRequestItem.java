package com.carels.mes.module.pur.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购申请明细实体类 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PurRequestItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 申请ID */
    private Long requestId;

    /** 物料ID引用 */
    private Long itemIdRef;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 规格型号 */
    private String itemSpec;

    /** 单位 */
    private String unit;

    /** 数量 */
    private BigDecimal quantity;

    /** 单价 */
    private BigDecimal price;

    /** 金额 */
    private BigDecimal amount;

    /** 需求日期 */
    private Date requiredDate;

    /** 备注 */
    private String remark;
}
