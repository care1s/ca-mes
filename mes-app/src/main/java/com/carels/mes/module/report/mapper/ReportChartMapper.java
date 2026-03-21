package com.carels.mes.module.report.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 报表图表Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface ReportChartMapper {

    /**
     * 获取生产统计（最近7天）
     */
    List<Map<String, Object>> selectProductionDailyStats();

    /**
     * 获取工单状态分布
     */
    Map<String, Object> selectWorkorderStatusDistribution();

    /**
     * 获取工单月度趋势
     */
    List<Map<String, Object>> selectWorkorderMonthlyTrend();

    /**
     * 获取质量检验统计
     */
    Map<String, Object> selectQualityInspectStats();

    /**
     * 获取缺陷类型分布
     */
    List<Map<String, Object>> selectDefectTypeDistribution();

    /**
     * 获取车间产量排行
     */
    List<Map<String, Object>> selectWorkshopOutputRanking();
}
