package com.carels.mes.module.cal.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 班次实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Data
public class CalShift implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 班次ID */
    private Long shiftId;

    /** 班次编码 */
    private String shiftCode;

    /** 班次名称 */
    private String shiftName;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 休息开始时间 */
    private LocalTime restStartTime;

    /** 休息结束时间 */
    private LocalTime restEndTime;

    /** 工作时长(小时) */
    private java.math.BigDecimal workHours;

    /** 状态: 0-启用, 1-停用 */
    private String status;

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

    /** 搜索关键字（不入库，仅用于查询） */
    private String keyword;
}
