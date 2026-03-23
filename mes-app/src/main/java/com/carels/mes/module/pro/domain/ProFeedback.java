package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 生产报工实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProFeedback extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 报工ID */
    private Long feedbackId;

    /** 报工单号 */
    private String feedbackCode;

    /** 工单ID */
    private Long workorderId;

    /** 工单编码 */
    private String workorderCode;

    /** 任务ID */
    private Long taskId;

    /** 任务编码 */
    private String taskCode;

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

    /** 报工数量 */
    private BigDecimal quantity;

    /** 合格数量 */
    private BigDecimal qualifiedQuantity;

    /** 不良数量 */
    private BigDecimal defectiveQuantity;

    /** 报废数量 */
    private BigDecimal scrapQuantity;

    /** 操作员ID */
    private Long operatorId;

    /** 操作员名称 */
    private String operatorName;

    /** 报工时间 */
    private Date feedbackTime;

    /** 状态: SUBMITTED-已提交, APPROVED-已审核 */
    private String status;

    /** 备注 */
    private String remark;
}
