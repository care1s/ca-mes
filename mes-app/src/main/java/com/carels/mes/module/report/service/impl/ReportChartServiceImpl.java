package com.carels.mes.module.report.service.impl;

import com.carels.mes.module.report.mapper.ReportChartMapper;
import com.carels.mes.module.report.service.IReportChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报表图表Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Service
public class ReportChartServiceImpl implements IReportChartService {

    @Autowired
    private ReportChartMapper reportChartMapper;

    @Override
    public Map<String, Object> getProductionChartData() {
        Map<String, Object> data = new HashMap<>();

        // 最近7天生产数据
        List<Map<String, Object>> dailyStats = reportChartMapper.selectProductionDailyStats();
        data.put("dailyStats", dailyStats != null ? dailyStats : new ArrayList<>());

        // 工单状态分布
        Map<String, Object> statusDistribution = reportChartMapper.selectWorkorderStatusDistribution();
        data.put("statusDistribution", statusDistribution != null ? statusDistribution : new HashMap<>());

        // 车间产量排行
        List<Map<String, Object>> workshopRanking = reportChartMapper.selectWorkshopOutputRanking();
        data.put("workshopRanking", workshopRanking != null ? workshopRanking : new ArrayList<>());

        return data;
    }

    @Override
    public Map<String, Object> getWorkorderStats() {
        Map<String, Object> data = new HashMap<>();

        // 工单状态分布
        Map<String, Object> statusDistribution = reportChartMapper.selectWorkorderStatusDistribution();
        data.put("statusDistribution", statusDistribution != null ? statusDistribution : new HashMap<>());

        // 月度趋势
        List<Map<String, Object>> monthlyTrend = reportChartMapper.selectWorkorderMonthlyTrend();
        data.put("monthlyTrend", monthlyTrend != null ? monthlyTrend : new ArrayList<>());

        // 车间产量排行
        List<Map<String, Object>> workshopRanking = reportChartMapper.selectWorkshopOutputRanking();
        data.put("workshopRanking", workshopRanking != null ? workshopRanking : new ArrayList<>());

        return data;
    }

    @Override
    public Map<String, Object> getQualityStats() {
        Map<String, Object> data = new HashMap<>();

        // 检验统计
        Map<String, Object> inspectStats = reportChartMapper.selectQualityInspectStats();
        data.put("inspectStats", inspectStats != null ? inspectStats : new HashMap<>());

        // 缺陷类型分布
        List<Map<String, Object>> defectDistribution = reportChartMapper.selectDefectTypeDistribution();
        data.put("defectDistribution", defectDistribution != null ? defectDistribution : new ArrayList<>());

        // 计算合格率
        if (inspectStats != null) {
            int iqcCount = ((Number) inspectStats.getOrDefault("iqcCount", 0)).intValue();
            int iqcPassed = ((Number) inspectStats.getOrDefault("iqcPassed", 0)).intValue();
            int ipqcCount = ((Number) inspectStats.getOrDefault("ipqcCount", 0)).intValue();
            int ipqcPassed = ((Number) inspectStats.getOrDefault("ipqcPassed", 0)).intValue();
            int oqcCount = ((Number) inspectStats.getOrDefault("oqcCount", 0)).intValue();
            int oqcPassed = ((Number) inspectStats.getOrDefault("oqcPassed", 0)).intValue();

            double iqcRate = iqcCount > 0 ? Math.round(iqcPassed * 100.0 / iqcCount * 10) / 10.0 : 0;
            double ipqcRate = ipqcCount > 0 ? Math.round(ipqcPassed * 100.0 / ipqcCount * 10) / 10.0 : 0;
            double oqcRate = oqcCount > 0 ? Math.round(oqcPassed * 100.0 / oqcCount * 10) / 10.0 : 0;

            Map<String, Object> passRate = new HashMap<>();
            passRate.put("iqc", iqcRate);
            passRate.put("ipqc", ipqcRate);
            passRate.put("oqc", oqcRate);
            data.put("passRate", passRate);
        }

        return data;
    }
}
