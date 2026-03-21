package com.carels.mes.module.analysis.service;

import com.carels.mes.module.analysis.domain.PredictionModel;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预测分析Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
public interface IPredictionAnalysisService {

    /**
     * 预测工单完成时间
     *
     * @param workorderId 工单ID
     * @return 预测结果
     */
    PredictionModel predictDeliveryTime(Long workorderId);

    /**
     * 批量预测工单完成时间
     *
     * @param workorderIds 工单ID列表
     * @return 预测结果列表
     */
    List<PredictionModel> predictDeliveryTimeBatch(List<Long> workorderIds);

    /**
     * 预测设备故障
     *
     * @param deviceId 设备ID
     * @return 预测结果
     */
    PredictionModel predictDeviceFailure(Long deviceId);

    /**
     * 预测质量趋势
     *
     * @param processId 工序ID
     * @param days 预测天数
     * @return 质量趋势预测
     */
    List<Map<String, Object>> predictQualityTrend(Long processId, int days);

    /**
     * 预测物料需求
     *
     * @param itemId 物料ID
     * @param days 预测天数
     * @return 需求预测
     */
    List<Map<String, Object>> predictMaterialDemand(Long itemId, int days);

    /**
     * 产能预测
     *
     * @param workstationId 工作站ID
     * @param days 预测天数
     * @return 产能预测
     */
    List<Map<String, Object>> predictCapacity(Long workstationId, int days);

    /**
     * 获取综合预测看板数据
     *
     * @return 看板数据
     */
    Map<String, Object> getPredictionDashboard();

    /**
     * 预测准确率统计
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 准确率统计
     */
    Map<String, Object> getPredictionAccuracy(Date startDate, Date endDate);
}
