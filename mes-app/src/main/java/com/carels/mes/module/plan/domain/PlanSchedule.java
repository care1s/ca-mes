package com.carels.mes.module.plan.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 排产计划实体类 - carels
 * 智能排产结果存储
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanSchedule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 排产ID */
    private Long scheduleId;

    /** 排产编码 */
    private String scheduleCode;

    /** 工单ID */
    private Long workorderId;

    /** 工单编码 */
    private String workorderCode;

    /** 物料ID */
    private Long itemId;

    /** 物料名称 */
    private String itemName;

    /** 计划数量 */
    private Double planQuantity;

    /** 工序ID */
    private Long processId;

    /** 工序名称 */
    private String processName;

    /** 工作站ID */
    private Long workstationId;

    /** 工作站名称 */
    private String workstationName;

    /** 生产线ID */
    private Long lineId;

    /** 生产线名称 */
    private String lineName;

    /** 计划开始时间 */
    private Date planStartTime;

    /** 计划结束时间 */
    private Date planEndTime;

    /** 需求交期 */
    private Date deliveryDate;

    /** 优先级（1-10，数字越小优先级越高） */
    private Integer priority;

    /** 排产状态: PENDING-待排产, SCHEDULED-已排产, PROCESSING-生产中, COMPLETED-已完成 */
    private String status;

    /** 产能负载率（%） */
    private Double loadRate;

    /** 标准工时（分钟/件） */
    private Double standardTime;

    /** 预计耗时（分钟） */
    private Double estimatedDuration;

    /** 实际开始时间 */
    private Date actualStartTime;

    /** 实际结束时间 */
    private Date actualEndTime;

    /** 是否延期 */
    private String isDelayed;

    /** 延期原因 */
    private String delayReason;

    /** 排产算法类型: GENETIC-遗传算法, GREEDY-贪心算法, MANUAL-手动 */
    private String algorithmType;

    /** 备注 */
    private String remark;
}
