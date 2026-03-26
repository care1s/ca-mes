package com.carels.mes.module.pro.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 安东异常记录实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProAndonRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 异常单号 */
    private String recordNo;

    /** 异常类型ID */
    private Long typeId;

    /** 异常类型名称 */
    private String typeName;

    /** 异常级别 */
    private String typeLevel;

    /** 工单ID */
    private Long workorderId;

    /** 工单编号 */
    private String workorderNo;

    /** 任务ID */
    private Long taskId;

    /** 工作站ID */
    private Long workstationId;

    /** 工作站名称 */
    private String workstationName;

    /** 异常描述 */
    private String description;

    /** 异常图片 */
    private String images;

    /** 上报人ID */
    private Long reporterId;

    /** 上报人名称 */
    private String reporterName;

    /** 上报时间 */
    private Date reportTime;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人名称 */
    private String handlerName;

    /** 接单时间 */
    private Date acceptTime;

    /** 处理时间 */
    private Date handleTime;

    /** 处理结果 */
    private String handleResult;

    /** 完成时间 */
    private Date completeTime;

    /** 状态: PENDING-待处理, ACCEPTED-已接单, HANDLING-处理中, COMPLETED-已完成, CLOSED-已关闭, ESCALATED-已升级 */
    private String status;

    /** 当前升级级别 */
    private Integer escalateLevel;

    /** 超时时间 */
    private Date timeoutTime;

    /** 关闭原因 */
    private String closeReason;

    /** 备注 */
    private String remark;
}
