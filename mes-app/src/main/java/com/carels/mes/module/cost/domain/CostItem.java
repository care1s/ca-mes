package com.carels.mes.module.cost.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 成本项目实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CostItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 成本项目ID */
    private Long itemId;

    /** 成本项目编码 */
    private String itemCode;

    /** 成本项目名称 */
    private String itemName;

    /** 成本类型: 1-直接材料, 2-直接人工, 3-制造费用 */
    private String costType;

    /** 分配方式: 1-按工时, 2-按产量, 3-按材料成本 */
    private String allocationMethod;

    /** 标准成本 */
    private BigDecimal standardCost;

    /** 状态: 0-停用, 1-启用 */
    private String status;

    /** 排序号 */
    private Integer sortNo;

    /** 备注 */
    private String remark;
}
