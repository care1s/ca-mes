package com.carels.mes.module.cost.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 成本核算单实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CostCalculation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 核算单ID */
    private Long calcId;

    /** 核算单号 */
    private String calcNo;

    /** 核算日期 */
    private Date calcDate;

    /** 核算月份 */
    private String calcMonth;

    /** 工单ID */
    private Long workorderId;

    /** 工单号 */
    private String workorderNo;

    /** 物料ID */
    private Long itemId;

    /** 物料编码 */
    private String itemCode;

    /** 物料名称 */
    private String itemName;

    /** 完工数量 */
    private BigDecimal completedQty;

    /** 直接材料成本 */
    private BigDecimal materialCost;

    /** 直接人工成本 */
    private BigDecimal laborCost;

    /** 制造费用 */
    private BigDecimal overheadCost;

    /** 总成本 */
    private BigDecimal totalCost;

    /** 单位成本 */
    private BigDecimal unitCost;

    /** 状态: 0-草稿, 1-已核算, 2-已结转 */
    private String status;

    /** 备注 */
    private String remark;

    /** 成本明细列表 */
    private List<CostCalculationLine> lines;
}
