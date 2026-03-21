package com.carels.mes.module.alert.domain;

import com.carels.mes.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 预警规则实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlertRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 规则ID */
    private Long ruleId;

    /** 规则编码 */
    private String ruleCode;

    /** 规则名称 */
    private String ruleName;

    /** 预警类型: DELIVERY-交期预警, QUALITY-质量预警, DEVICE-设备预警, STOCK-库存预警, CAPACITY-产能预警 */
    private String alertType;

    /** 监控对象类型: WORKORDER-工单, TASK-任务, DEVICE-设备, ITEM-物料, WORKSTATION-工作站 */
    private String targetType;

    /** 监控对象ID（可选，为空则监控全部） */
    private Long targetId;

    /** 预警条件: GT-大于, LT-小于, EQ-等于, GTE-大于等于, LTE-小于等于 */
    private String condition;

    /** 阈值 */
    private Double threshold;

    /** 阈值单位 */
    private String thresholdUnit;

    /** 预警级别: HIGH-高, MEDIUM-中, LOW-低 */
    private String alertLevel;

    /** 通知方式: SMS-短信, EMAIL-邮件, APP-APP推送, SYSTEM-系统消息 */
    private String notifyMethod;

    /** 通知人ID列表，逗号分隔 */
    private String notifyUsers;

    /** 状态: ENABLE-启用, DISABLE-禁用 */
    private String status;

    /** 触发次数 */
    private Integer triggerCount;

    /** 最后触发时间 */
    private Date lastTriggerTime;

    /** 规则描述 */
    private String description;
}
