package com.carels.mes.module.alert.domain;

import lombok.Data;

import java.util.Date;

/**
 * 预警记录实体类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
public class AlertRecord {

    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 预警编码 */
    private String alertCode;

    /** 规则ID */
    private Long ruleId;

    /** 规则名称 */
    private String ruleName;

    /** 预警类型 */
    private String alertType;

    /** 预警级别: HIGH-高, MEDIUM-中, LOW-低 */
    private String alertLevel;

    /** 监控对象类型 */
    private String targetType;

    /** 监控对象ID */
    private Long targetId;

    /** 监控对象名称 */
    private String targetName;

    /** 预警标题 */
    private String title;

    /** 预警内容 */
    private String content;

    /** 当前值 */
    private Double currentValue;

    /** 阈值 */
    private Double threshold;

    /** 状态: UNREAD-未读, READ-已读, HANDLED-已处理, IGNORED-已忽略 */
    private String status;

    /** 处理人ID */
    private Long handlerId;

    /** 处理人名称 */
    private String handlerName;

    /** 处理时间 */
    private Date handleTime;

    /** 处理备注 */
    private String handleRemark;

    /** 触发时间 */
    private Date triggerTime;

    /** 创建时间 */
    private Date createTime;
}
