package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 生产任务实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

    /** 工单ID */
    private Long workorderId;

    /** 工单编码 */
    private String workorderCode;

    /** 工序ID */
    private Long processId;

    /** 工序编码 */
    private String processCode;

    /** 工序名称 */
    private String processName;

    /** 工作站ID */
    private Long workstationId;

    /** 工作站名称 */
    private String workstationName;

    /** 计划数量 */
    private Double planQuantity;

    /** 完工数量 */
    private Double completedQuantity;

    /** 合格数量 */
    private Double qualifiedQuantity;

    /** 不良数量 */
    private Double defectiveQuantity;

    /** 任务状态: PENDING-待执行, PROCESSING-执行中, COMPLETED-已完成 */
    private String status;

    /** 计划开始时间 */
    private Date planStartTime;

    /** 计划完成时间 */
    private Date planEndTime;

    /** 实际开始时间 */
    private Date actualStartTime;

    /** 实际完成时间 */
    private Date actualEndTime;

    /** 操作员ID */
    private Long operatorId;

    /** 操作员名称 */
    private String operatorName;

    /** 备注 */
    private String remark;
}
