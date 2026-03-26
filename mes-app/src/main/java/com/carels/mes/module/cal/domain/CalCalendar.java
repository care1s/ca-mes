package com.carels.mes.module.cal.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 日历实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CalCalendar extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 日历ID */
    private Long calendarId;

    /** 日期 */
    private LocalDate calendarDate;

    /** 年 */
    private Integer year;

    /** 月 */
    private Integer month;

    /** 日 */
    private Integer day;

    /** 星期(1-7) */
    private Integer weekDay;

    /** 周数 */
    private Integer weekOfYear;

    /** 是否工作日: Y-是, N-否 */
    private String isWorkday;

    /** 节假日名称 */
    private String holidayName;

    /** 状态: 0-启用, 1-停用 */
    private String status;

    /** 查询起始日期 */
    private LocalDate beginDate;

    /** 查询结束日期 */
    private LocalDate endDate;
}
