package com.carels.mes.module.alert.service;

import com.carels.mes.module.alert.domain.AlertRecord;
import com.carels.mes.module.alert.domain.AlertRule;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自动预警Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
public interface IAlertService {

    /**
     * 创建预警规则
     */
    int createRule(AlertRule rule);

    /**
     * 更新预警规则
     */
    int updateRule(AlertRule rule);

    /**
     * 删除预警规则
     */
    int deleteRule(Long ruleId);

    /**
     * 启用/禁用预警规则
     */
    int toggleRuleStatus(Long ruleId, String status);

    /**
     * 获取预警规则列表
     */
    List<AlertRule> getRuleList(AlertRule query);

    /**
     * 获取预警规则详情
     */
    AlertRule getRuleById(Long ruleId);

    /**
     * 执行预警检查
     * 扫描所有监控对象，触发符合条件的预警
     */
    void executeAlertCheck();

    /**
     * 检查单个规则
     */
    List<AlertRecord> checkRule(Long ruleId);

    /**
     * 获取预警记录列表
     */
    List<AlertRecord> getAlertRecordList(String alertType, String alertLevel, String status);

    /**
     * 获取实时预警数据
     */
    List<AlertRecord> getRealTimeAlerts();

    /**
     * 标记预警为已读
     */
    int markAsRead(Long recordId);

    /**
     * 处理预警
     */
    int handleAlert(Long recordId, Long handlerId, String handlerName, String remark);

    /**
     * 忽略预警
     */
    int ignoreAlert(Long recordId, Long handlerId, String handlerName, String remark);

    /**
     * 获取预警统计
     */
    Map<String, Object> getAlertStatistics();

    /**
     * 获取预警趋势
     */
    List<Map<String, Object>> getAlertTrend(Date startDate, Date endDate);

    /**
     * 获取未处理预警数量
     */
    int getUnreadAlertCount();

    /**
     * 发送预警通知
     */
    void sendNotification(AlertRecord record);
}
