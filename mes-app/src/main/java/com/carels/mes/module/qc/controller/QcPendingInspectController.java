package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.qc.service.IQcPendingInspectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 待检任务看板Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mes/qc/pending")
public class QcPendingInspectController {

    @Autowired
    private IQcPendingInspectService pendingInspectService;

    /**
     * 待检任务看板 - 核心加强功能
     */
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        Map<String, Object> data = pendingInspectService.getPendingInspectDashboard();
        return AjaxResult.success(data);
    }

    /**
     * 按检验类型获取待检任务列表
     */
    @GetMapping("/list")
    public AjaxResult list(String inspectType) {
        List<Map<String, Object>> list = pendingInspectService.getPendingInspectList(inspectType);
        return AjaxResult.success(list);
    }
}
