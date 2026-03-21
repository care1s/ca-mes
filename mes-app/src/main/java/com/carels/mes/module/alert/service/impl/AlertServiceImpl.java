package com.carels.mes.module.alert.service.impl;

import com.carels.mes.module.alert.domain.AlertRecord;
import com.carels.mes.module.alert.domain.AlertRule;
import com.carels.mes.module.alert.service.IAlertService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自动预警Service实现 - carels
 * 多维度实时监控和自动预警系统
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Service
public class AlertServiceImpl implements IAlertService {

    // 模拟数据存储
    private Map<Long, AlertRule> ruleStore = new HashMap<>();
    private Map<Long, AlertRecord> recordStore = new HashMap<>();
    private long ruleIdCounter = 1;
    private long recordIdCounter = 1;

    @Override
    public int createRule(AlertRule rule) {
        rule.setRuleId(ruleIdCounter++);
        rule.setRuleCode("AR" + System.currentTimeMillis());
        rule.setStatus("ENABLE");
        rule.setTriggerCount(0);
        rule.setCreateTime(new Date());
        ruleStore.put(rule.getRuleId(), rule);
        return 1;
    }

    @Override
    public int updateRule(AlertRule rule) {
        if (ruleStore.containsKey(rule.getRuleId())) {
            ruleStore.put(rule.getRuleId(), rule);
            return 1;
        }
        return 0;
    }

    @Override
    public int deleteRule(Long ruleId) {
        ruleStore.remove(ruleId);
        return 1;
    }

    @Override
    public int toggleRuleStatus(Long ruleId, String status) {
        AlertRule rule = ruleStore.get(ruleId);
        if (rule != null) {
            rule.setStatus(status);
            return 1;
        }
        return 0;
    }

    @Override
    public List<AlertRule> getRuleList(AlertRule query) {
        List<AlertRule> rules = new ArrayList<>(ruleStore.values());
        if (query.getAlertType() != null && !query.getAlertType().isEmpty()) {
            rules.removeIf(r -> !r.getAlertType().equals(query.getAlertType()));
        }
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            rules.removeIf(r -> !r.getStatus().equals(query.getStatus()));
        }
        return rules;
    }

    @Override
    public AlertRule getRuleById(Long ruleId) {
        return ruleStore.get(ruleId);
    }

    @Override
    public void executeAlertCheck() {
        // 获取所有启用的规则
        List<AlertRule> activeRules = ruleStore.values().stream()
                .filter(r -> "ENABLE".equals(r.getStatus()))
                .collect(Collectors.toList());

        for (AlertRule rule : activeRules) {
            checkRule(rule.getRuleId());
        }
    }

    @Override
    public List<AlertRecord> checkRule(Long ruleId) {
        List<AlertRecord> alerts = new ArrayList<>();
        AlertRule rule = ruleStore.get(ruleId);
        if (rule == null || !"ENABLE".equals(rule.getStatus())) {
            return alerts;
        }

        // 根据规则类型执行检查
        switch (rule.getAlertType()) {
            case "DELIVERY":
                alerts.addAll(checkDeliveryAlerts(rule));
                break;
            case "QUALITY":
                alerts.addAll(checkQualityAlerts(rule));
                break;
            case "DEVICE":
                alerts.addAll(checkDeviceAlerts(rule));
                break;
            case "STOCK":
                alerts.addAll(checkStockAlerts(rule));
                break;
            case "CAPACITY":
                alerts.addAll(checkCapacityAlerts(rule));
                break;
        }

        return alerts;
    }

    @Override
    public List<AlertRecord> getAlertRecordList(String alertType, String alertLevel, String status) {
        List<AlertRecord> records = new ArrayList<>(recordStore.values());

        if (alertType != null && !alertType.isEmpty()) {
            records.removeIf(r -> !r.getAlertType().equals(alertType));
        }
        if (alertLevel != null && !alertLevel.isEmpty()) {
            records.removeIf(r -> !r.getAlertLevel().equals(alertLevel));
        }
        if (status != null && !status.isEmpty()) {
            records.removeIf(r -> !r.getStatus().equals(status));
        }

        // 按触发时间倒序
        records.sort((r1, r2) -> r2.getTriggerTime().compareTo(r1.getTriggerTime()));
        return records;
    }

    @Override
    public List<AlertRecord> getRealTimeAlerts() {
        // 获取最近1小时的预警
        Date oneHourAgo = new Date(System.currentTimeMillis() - 3600 * 1000);
        return recordStore.values().stream()
                .filter(r -> r.getTriggerTime().after(oneHourAgo))
                .sorted((r1, r2) -> r2.getTriggerTime().compareTo(r1.getTriggerTime()))
                .limit(50)
                .collect(Collectors.toList());
    }

    @Override
    public int markAsRead(Long recordId) {
        AlertRecord record = recordStore.get(recordId);
        if (record != null) {
            record.setStatus("READ");
            return 1;
        }
        return 0;
    }

    @Override
    public int handleAlert(Long recordId, Long handlerId, String handlerName, String remark) {
        AlertRecord record = recordStore.get(recordId);
        if (record != null) {
            record.setStatus("HANDLED");
            record.setHandlerId(handlerId);
            record.setHandlerName(handlerName);
            record.setHandleTime(new Date());
            record.setHandleRemark(remark);
            return 1;
        }
        return 0;
    }

    @Override
    public int ignoreAlert(Long recordId, Long handlerId, String handlerName, String remark) {
        AlertRecord record = recordStore.get(recordId);
        if (record != null) {
            record.setStatus("IGNORED");
            record.setHandlerId(handlerId);
            record.setHandlerName(handlerName);
            record.setHandleTime(new Date());
            record.setHandleRemark(remark);
            return 1;
        }
        return 0;
    }

    @Override
    public Map<String, Object> getAlertStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // 总数统计
        long total = recordStore.size();
        long unread = recordStore.values().stream().filter(r -> "UNREAD".equals(r.getStatus())).count();
        long high = recordStore.values().stream().filter(r -> "HIGH".equals(r.getAlertLevel())).count();
        long medium = recordStore.values().stream().filter(r -> "MEDIUM".equals(r.getAlertLevel())).count();
        long low = recordStore.values().stream().filter(r -> "LOW".equals(r.getAlertLevel())).count();

        stats.put("total", total);
        stats.put("unread", unread);
        stats.put("high", high);
        stats.put("medium", medium);
        stats.put("low", low);

        // 分类统计
        Map<String, Long> typeStats = new HashMap<>();
        for (AlertRecord record : recordStore.values()) {
            String type = record.getAlertType();
            typeStats.put(type, typeStats.getOrDefault(type, 0L) + 1);
        }
        stats.put("typeStats", typeStats);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getAlertTrend(Date startDate, Date endDate) {
        List<Map<String, Object>> trend = new ArrayList<>();

        // 按天统计预警数量
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        while (!cal.getTime().after(endDate)) {
            Date date = cal.getTime();
            Map<String, Object> dayStat = new HashMap<>();
            dayStat.put("date", date);

            long count = recordStore.values().stream()
                    .filter(r -> isSameDay(r.getTriggerTime(), date))
                    .count();
            dayStat.put("count", count);

            trend.add(dayStat);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return trend;
    }

    @Override
    public int getUnreadAlertCount() {
        return (int) recordStore.values().stream()
                .filter(r -> "UNREAD".equals(r.getStatus()))
                .count();
    }

    @Override
    public void sendNotification(AlertRecord record) {
        // 根据通知方式发送预警
        String method = record.getAlertLevel(); // 这里应该从规则获取

        switch (method) {
            case "SMS":
                sendSMS(record);
                break;
            case "EMAIL":
                sendEmail(record);
                break;
            case "APP":
                sendAppPush(record);
                break;
            case "SYSTEM":
            default:
                sendSystemMessage(record);
                break;
        }
    }

    // ==================== 预警检查方法 ====================

    private List<AlertRecord> checkDeliveryAlerts(AlertRule rule) {
        List<AlertRecord> alerts = new ArrayList<>();

        // 检查交期风险
        // 模拟：获取即将到期或已延期的工单
        List<Map<String, Object>> atRiskWorkorders = getAtRiskWorkorders(rule.getThreshold());

        for (Map<String, Object> workorder : atRiskWorkorders) {
            AlertRecord record = createAlertRecord(rule);
            record.setTargetId(((Number) workorder.get("workorderId")).longValue());
            record.setTargetName((String) workorder.get("workorderCode"));
            record.setTitle("交期预警");
            record.setContent(String.format("工单 %s 预计延期 %s 天",
                    workorder.get("workorderCode"),
                    workorder.get("delayDays")));
            record.setCurrentValue(((Number) workorder.get("progress")).doubleValue());
            record.setThreshold(rule.getThreshold());

            saveAlertRecord(record);
            alerts.add(record);
        }

        return alerts;
    }

    private List<AlertRecord> checkQualityAlerts(AlertRule rule) {
        List<AlertRecord> alerts = new ArrayList<>();

        // 检查质量异常
        List<Map<String, Object>> qualityIssues = getQualityIssues(rule.getThreshold());

        for (Map<String, Object> issue : qualityIssues) {
            AlertRecord record = createAlertRecord(rule);
            record.setTargetId(((Number) issue.get("processId")).longValue());
            record.setTargetName((String) issue.get("processName"));
            record.setTitle("质量预警");
            record.setContent(String.format("工序 %s 合格率低于阈值，当前 %.2f%%",
                    issue.get("processName"),
                    issue.get("passRate")));
            record.setCurrentValue(((Number) issue.get("passRate")).doubleValue());
            record.setThreshold(rule.getThreshold());

            saveAlertRecord(record);
            alerts.add(record);
        }

        return alerts;
    }

    private List<AlertRecord> checkDeviceAlerts(AlertRule rule) {
        List<AlertRecord> alerts = new ArrayList<>();

        // 检查设备异常
        List<Map<String, Object>> deviceIssues = getDeviceIssues(rule.getThreshold());

        for (Map<String, Object> issue : deviceIssues) {
            AlertRecord record = createAlertRecord(rule);
            record.setTargetId(((Number) issue.get("deviceId")).longValue());
            record.setTargetName((String) issue.get("deviceName"));
            record.setTitle("设备预警");
            record.setContent(String.format("设备 %s 运行异常，%s",
                    issue.get("deviceName"),
                    issue.get("issueDesc")));
            record.setCurrentValue(((Number) issue.get("health")).doubleValue());
            record.setThreshold(rule.getThreshold());

            saveAlertRecord(record);
            alerts.add(record);
        }

        return alerts;
    }

    private List<AlertRecord> checkStockAlerts(AlertRule rule) {
        List<AlertRecord> alerts = new ArrayList<>();

        // 检查库存预警
        List<Map<String, Object>> stockIssues = getStockIssues(rule.getThreshold());

        for (Map<String, Object> issue : stockIssues) {
            AlertRecord record = createAlertRecord(rule);
            record.setTargetId(((Number) issue.get("itemId")).longValue());
            record.setTargetName((String) issue.get("itemName"));
            record.setTitle("库存预警");
            record.setContent(String.format("物料 %s 库存不足，当前 %.2f，安全库存 %.2f",
                    issue.get("itemName"),
                    issue.get("currentStock"),
                    issue.get("safetyStock")));
            record.setCurrentValue(((Number) issue.get("currentStock")).doubleValue());
            record.setThreshold(rule.getThreshold());

            saveAlertRecord(record);
            alerts.add(record);
        }

        return alerts;
    }

    private List<AlertRecord> checkCapacityAlerts(AlertRule rule) {
        List<AlertRecord> alerts = new ArrayList<>();

        // 检查产能预警
        List<Map<String, Object>> capacityIssues = getCapacityIssues(rule.getThreshold());

        for (Map<String, Object> issue : capacityIssues) {
            AlertRecord record = createAlertRecord(rule);
            record.setTargetId(((Number) issue.get("workstationId")).longValue());
            record.setTargetName((String) issue.get("workstationName"));
            record.setTitle("产能预警");
            record.setContent(String.format("工作站 %s 负载过高，当前 %.2f%%",
                    issue.get("workstationName"),
                    issue.get("loadRate")));
            record.setCurrentValue(((Number) issue.get("loadRate")).doubleValue());
            record.setThreshold(rule.getThreshold());

            saveAlertRecord(record);
            alerts.add(record);
        }

        return alerts;
    }

    // ==================== 辅助方法 ====================

    private AlertRecord createAlertRecord(AlertRule rule) {
        AlertRecord record = new AlertRecord();
        record.setRecordId(recordIdCounter++);
        record.setAlertCode("ALT" + System.currentTimeMillis());
        record.setRuleId(rule.getRuleId());
        record.setRuleName(rule.getRuleName());
        record.setAlertType(rule.getAlertType());
        record.setAlertLevel(rule.getAlertLevel());
        record.setStatus("UNREAD");
        record.setTriggerTime(new Date());
        record.setCreateTime(new Date());
        return record;
    }

    private void saveAlertRecord(AlertRecord record) {
        recordStore.put(record.getRecordId(), record);
    }

    private void sendSMS(AlertRecord record) {
        System.out.println("发送短信预警: " + record.getTitle());
    }

    private void sendEmail(AlertRecord record) {
        System.out.println("发送邮件预警: " + record.getTitle());
    }

    private void sendAppPush(AlertRecord record) {
        System.out.println("发送APP推送: " + record.getTitle());
    }

    private void sendSystemMessage(AlertRecord record) {
        System.out.println("发送系统消息: " + record.getTitle());
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
               cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // 模拟数据方法
    private List<Map<String, Object>> getAtRiskWorkorders(Double threshold) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 返回有延期风险的工单
        return list;
    }

    private List<Map<String, Object>> getQualityIssues(Double threshold) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 返回质量异常的工序
        return list;
    }

    private List<Map<String, Object>> getDeviceIssues(Double threshold) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 返回设备异常
        return list;
    }

    private List<Map<String, Object>> getStockIssues(Double threshold) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 返回库存预警
        return list;
    }

    private List<Map<String, Object>> getCapacityIssues(Double threshold) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 返回产能预警
        return list;
    }
}
