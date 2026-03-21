package com.carels.mes.module.alert.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.alert.domain.AlertRecord;
import com.carels.mes.module.alert.domain.AlertRule;
import com.carels.mes.module.alert.service.IAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 自动预警Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@RestController
@RequestMapping("/mes/alert")
public class AlertController {

    @Autowired
    private IAlertService alertService;

    // ==================== 规则管理 ====================

    /**
     * 创建预警规则
     */
    @PostMapping("/rule")
    public AjaxResult createRule(@RequestBody AlertRule rule) {
        return AjaxResult.success(alertService.createRule(rule));
    }

    /**
     * 更新预警规则
     */
    @PutMapping("/rule")
    public AjaxResult updateRule(@RequestBody AlertRule rule) {
        return AjaxResult.success(alertService.updateRule(rule));
    }

    /**
     * 删除预警规则
     */
    @DeleteMapping("/rule/{ruleId}")
    public AjaxResult deleteRule(@PathVariable Long ruleId) {
        return AjaxResult.success(alertService.deleteRule(ruleId));
    }

    /**
     * 启用/禁用规则
     */
    @PutMapping("/rule/{ruleId}/toggle")
    public AjaxResult toggleRuleStatus(@PathVariable Long ruleId,
                                       @RequestParam String status) {
        return AjaxResult.success(alertService.toggleRuleStatus(ruleId, status));
    }

    /**
     * 获取规则列表
     */
    @GetMapping("/rule/list")
    public AjaxResult getRuleList(AlertRule query) {
        return AjaxResult.success(alertService.getRuleList(query));
    }

    /**
     * 获取规则详情
     */
    @GetMapping("/rule/{ruleId}")
    public AjaxResult getRuleById(@PathVariable Long ruleId) {
        return AjaxResult.success(alertService.getRuleById(ruleId));
    }

    // ==================== 预警执行 ====================

    /**
     * 执行预警检查
     */
    @PostMapping("/check")
    public AjaxResult executeAlertCheck() {
        alertService.executeAlertCheck();
        return AjaxResult.success("预警检查执行完成");
    }

    /**
     * 检查单个规则
     */
    @PostMapping("/check/{ruleId}")
    public AjaxResult checkRule(@PathVariable Long ruleId) {
        List<AlertRecord> alerts = alertService.checkRule(ruleId);
        return AjaxResult.success(alerts);
    }

    // ==================== 预警记录 ====================

    /**
     * 获取预警记录列表
     */
    @GetMapping("/record/list")
    public AjaxResult getAlertRecordList(
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) String alertLevel,
            @RequestParam(required = false) String status) {
        return AjaxResult.success(alertService.getAlertRecordList(alertType, alertLevel, status));
    }

    /**
     * 获取实时预警
     */
    @GetMapping("/realtime")
    public AjaxResult getRealTimeAlerts() {
        return AjaxResult.success(alertService.getRealTimeAlerts());
    }

    /**
     * 标记为已读
     */
    @PutMapping("/record/{recordId}/read")
    public AjaxResult markAsRead(@PathVariable Long recordId) {
        return AjaxResult.success(alertService.markAsRead(recordId));
    }

    /**
     * 处理预警
     */
    @PostMapping("/record/{recordId}/handle")
    public AjaxResult handleAlert(@PathVariable Long recordId,
                                  @RequestBody Map<String, Object> params) {
        Long handlerId = ((Number) params.get("handlerId")).longValue();
        String handlerName = (String) params.get("handlerName");
        String remark = (String) params.get("remark");
        return AjaxResult.success(alertService.handleAlert(recordId, handlerId, handlerName, remark));
    }

    /**
     * 忽略预警
     */
    @PostMapping("/record/{recordId}/ignore")
    public AjaxResult ignoreAlert(@PathVariable Long recordId,
                                  @RequestBody Map<String, Object> params) {
        Long handlerId = ((Number) params.get("handlerId")).longValue();
        String handlerName = (String) params.get("handlerName");
        String remark = (String) params.get("remark");
        return AjaxResult.success(alertService.ignoreAlert(recordId, handlerId, handlerName, remark));
    }

    /**
     * 获取预警统计
     */
    @GetMapping("/statistics")
    public AjaxResult getAlertStatistics() {
        return AjaxResult.success(alertService.getAlertStatistics());
    }

    /**
     * 获取预警趋势
     */
    @GetMapping("/trend")
    public AjaxResult getAlertTrend(@RequestParam Date startDate,
                                    @RequestParam Date endDate) {
        return AjaxResult.success(alertService.getAlertTrend(startDate, endDate));
    }

    /**
     * 获取未读预警数量
     */
    @GetMapping("/unread-count")
    public AjaxResult getUnreadAlertCount() {
        return AjaxResult.success(alertService.getUnreadAlertCount());
    }
}
