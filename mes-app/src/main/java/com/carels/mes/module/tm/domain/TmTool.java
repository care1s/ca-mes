package com.carels.mes.module.tm.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工装机具实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TmTool extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 工装机具ID */
    private Long toolId;

    /** 工装机具编码 */
    private String toolCode;

    /** 工装机具名称 */
    private String toolName;

    /** 工装机具类型: 1-工装, 2-模具, 3-刀具, 4-量具, 5-辅具 */
    private String toolType;

    /** 规格型号 */
    private String specification;

    /** 制造商 */
    private String manufacturer;

    /** 购买日期 */
    private Date purchaseDate;

    /** 价格 */
    private BigDecimal price;

    /** 使用寿命(次数或小时) */
    private Integer lifeLimit;

    /** 已使用次数/时长 */
    private Integer usedLife;

    /** 剩余寿命 */
    private Integer remainLife;

    /** 存放位置 */
    private String location;

    /** 状态: 0-停用, 1-可用, 2-维修中, 3-报废 */
    private String status;

    /** 上次保养日期 */
    private Date lastMaintainDate;

    /** 保养周期(天) */
    private Integer maintainCycle;

    /** 备注 */
    private String remark;
}
