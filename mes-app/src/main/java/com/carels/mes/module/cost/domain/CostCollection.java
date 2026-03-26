package com.carels.mes.module.cost.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 成本归集实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CostCollection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 归集ID */
    private Long collectionId;

    /** 归集单号 */
    private String collectionNo;

    /** 成本项目ID */
    private Long itemId;

    /** 成本项目名称 */
    private String itemName;

    /** 成本类型 */
    private String costType;

    /** 归集日期 */
    private Date collectionDate;

    /** 工单ID */
    private Long workorderId;

    /** 工单号 */
    private String workorderNo;

    /** 金额 */
    private BigDecimal amount;

    /** 工时(小时) */
    private BigDecimal workHours;

    /** 产量 */
    private BigDecimal quantity;

    /** 数据来源: 1-手工录入, 2-系统自动 */
    private String dataSource;

    /** 数据来源单据ID */
    private Long sourceId;

    /** 数据来源单据号 */
    private String sourceNo;

    /** 状态: 0-草稿, 1-已确认 */
    private String status;

    /** 备注 */
    private String remark;
}
