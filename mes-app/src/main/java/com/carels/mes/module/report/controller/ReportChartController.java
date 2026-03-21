package com.carels.mes.module.report.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.report.service.IReportChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 生产报表Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mes/report")
public class ReportChartController {

    @Autowired
    private IReportChartService reportChartService;

    /**
     * 生产报表图表数据
     */
    @GetMapping("/chart")
    public AjaxResult getChartData() {
        Map<String, Object> data = reportChartService.getProductionChartData();
        return AjaxResult.success(data);
    }

    /**
     * 工单统计报表
     */
    @GetMapping("/workorder/stats")
    public AjaxResult getWorkorderStats() {
        Map<String, Object> data = reportChartService.getWorkorderStats();
        return AjaxResult.success(data);
    }

    /**
     * 质量统计报表
     */
    @GetMapping("/quality/stats")
    public AjaxResult getQualityStats() {
        Map<String, Object> data = reportChartService.getQualityStats();
        return AjaxResult.success(data);
    }
}
