package com.carels.mes.module.cal.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 排班计划实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CalPlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 计划ID */
    private Long planId;

    /** 计划编码 */
    private String planCode;

    /** 计划名称 */
    private String planName;

    /** 排班日期 */
    private LocalDate planDate;

    /** 班次ID */
    private Long shiftId;

    /** 班次编码 */
    private String shiftCode;

    /** 班次名称 */
    private String shiftName;

    /** 班组ID */
    private Long teamId;

    /** 班组编码 */
    private String teamCode;

    /** 班组名称 */
    private String teamName;

    /** 状态: 0-启用, 1-停用 */
    private String status;

    /** 查询起始日期 */
    private LocalDate beginDate;

    /** 查询结束日期 */
    private LocalDate endDate;

    /** 搜索关键字（不入库，仅用于查询） */
    private String keyword;
}
