package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.pro.service.IProFeedbackService;
import com.carels.mes.module.pro.service.IProTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 生产任务移动端Controller - carels
 * 扫码报工功能
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mobile/pro/task")
public class ProTaskMobController {

    @Autowired
    private IProTaskService taskService;

    @Autowired
    private IProFeedbackService feedbackService;

    /**
     * 扫码查询任务
     * 支持扫描：工单码、任务码、工序码
     */
    @GetMapping("/scan")
    public AjaxResult scan(@RequestParam String barcode) {
        Map<String, Object> task = taskService.scanTask(barcode);
        if (task == null) {
            return AjaxResult.error("未找到对应的任务，请检查条码是否正确");
        }
        return AjaxResult.success(task);
    }

    /**
     * 获取待执行的任务列表
     */
    @GetMapping("/pending")
    public AjaxResult pending(@RequestParam(required = false) Long workstationId) {
        List<Map<String, Object>> list = taskService.getPendingTaskList(workstationId);
        return AjaxResult.success(list);
    }

    /**
     * 获取我的任务列表
     */
    @GetMapping("/my")
    public AjaxResult myTasks(@RequestParam Long operatorId) {
        List<Map<String, Object>> list = taskService.getMyTaskList(operatorId);
        return AjaxResult.success(list);
    }

    /**
     * 扫码快捷报工 - 核心加强功能
     * 扫码 → 自动带出任务信息 → 输入数量 → 提交报工
     */
    @PostMapping("/feedback/scan")
    public AjaxResult scanFeedback(@RequestBody Map<String, Object> params) {
        try {
            String barcode = (String) params.get("barcode");
            Double quantity = Double.valueOf(params.get("quantity").toString());
            Long operatorId = Long.valueOf(params.get("operatorId").toString());
            String operatorName = (String) params.get("operatorName");

            // 1. 扫码查询任务
            Map<String, Object> task = taskService.scanTask(barcode);
            if (task == null) {
                return AjaxResult.error("未找到对应的任务");
            }

            // 2. 提交报工
            Long taskId = Long.valueOf(task.get("taskId").toString());
            Map<String, Object> result = feedbackService.submitFeedback(taskId, quantity, operatorId, operatorName);

            return AjaxResult.success("报工成功", result);
        } catch (Exception e) {
            return AjaxResult.error("报工失败: " + e.getMessage());
        }
    }

    /**
     * 简单报工（已知任务ID）
     */
    @PostMapping("/feedback")
    public AjaxResult feedback(@RequestBody Map<String, Object> params) {
        try {
            Long taskId = Long.valueOf(params.get("taskId").toString());
            Double quantity = Double.valueOf(params.get("quantity").toString());
            Long operatorId = Long.valueOf(params.get("operatorId").toString());
            String operatorName = (String) params.get("operatorName");

            Map<String, Object> result = feedbackService.submitFeedback(taskId, quantity, operatorId, operatorName);
            return AjaxResult.success("报工成功", result);
        } catch (Exception e) {
            return AjaxResult.error("报工失败: " + e.getMessage());
        }
    }
}
