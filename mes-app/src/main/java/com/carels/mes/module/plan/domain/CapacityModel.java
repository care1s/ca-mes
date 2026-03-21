package com.carels.mes.module.plan.domain;

import lombok.Data;

import java.util.Date;

/**
 * 产能模型 - carels
 * 用于智能排产计算
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
public class CapacityModel {

    /** 工作站ID */
    private Long workstationId;

    /** 工作站名称 */
    private String workstationName;

    /** 生产线ID */
    private Long lineId;

    /** 生产线名称 */
    private String lineName;

    /** 工序ID */
    private Long processId;

    /** 工序名称 */
    private String processName;

    /** 日产能（件/天） */
    private Double dailyCapacity;

    /** 标准工时（分钟/件） */
    private Double standardTime;

    /** 设备利用率（%） */
    private Double utilizationRate;

    /** 工作时间开始 */
    private String workStartTime;

    /** 工作时间结束 */
    private String workEndTime;

    /** 可用日期 */
    private Date availableDate;

    /** 当前负载（已排产量） */
    private Double currentLoad;

    /** 剩余产能 */
    private Double remainingCapacity;

    /** 负载率（%） */
    private Double loadRate;
}
