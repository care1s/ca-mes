package com.carels.mes.module.cost.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 成本核算明细实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CostCalculationLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long lineId;

    /** 核算单ID */
    private Long calcId;

    /** 成本项目ID */
    private Long itemId;

    /** 成本项目名称 */
    private String itemName;

    /** 成本类型 */
    private String costType;

    /** 金额 */
    private BigDecimal amount;

    /** 分配率 */
    private BigDecimal allocationRate;

    /** 分配金额 */
    private BigDecimal allocatedAmount;

    /** 备注 */
    private String remark;
}
