package com.carels.mes.module.report.service;

import java.util.Map;

/**
 * 报表图表Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface IReportChartService {

    /**
     * 获取生产图表数据
     */
    Map<String, Object> getProductionChartData();

    /**
     * 获取工单统计数据
     */
    Map<String, Object> getWorkorderStats();

    /**
     * 获取质量统计数据
     */
    Map<String, Object> getQualityStats();
}
