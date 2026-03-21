package com.carels.mes.module.analysis.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.analysis.domain.PredictionModel;
import com.carels.mes.module.analysis.service.IPredictionAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预测分析Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@RestController
@RequestMapping("/mes/analysis/prediction")
public class PredictionAnalysisController {

    @Autowired
    private IPredictionAnalysisService predictionService;

    /**
     * 预测工单完成时间
     */
    @GetMapping("/delivery/{workorderId}")
    public AjaxResult predictDeliveryTime(@PathVariable Long workorderId) {
        PredictionModel prediction = predictionService.predictDeliveryTime(workorderId);
        return AjaxResult.success(prediction);
    }

    /**
     * 批量预测工单完成时间
     */
    @PostMapping("/delivery/batch")
    public AjaxResult predictDeliveryTimeBatch(@RequestBody List<Long> workorderIds) {
        List<PredictionModel> predictions = predictionService.predictDeliveryTimeBatch(workorderIds);
        return AjaxResult.success(predictions);
    }

    /**
     * 预测设备故障
     */
    @GetMapping("/device/{deviceId}")
    public AjaxResult predictDeviceFailure(@PathVariable Long deviceId) {
        PredictionModel prediction = predictionService.predictDeviceFailure(deviceId);
        return AjaxResult.success(prediction);
    }

    /**
     * 预测质量趋势
     */
    @GetMapping("/quality/{processId}")
    public AjaxResult predictQualityTrend(@PathVariable Long processId,
                                          @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> predictions = predictionService.predictQualityTrend(processId, days);
        return AjaxResult.success(predictions);
    }

    /**
     * 预测物料需求
     */
    @GetMapping("/demand/{itemId}")
    public AjaxResult predictMaterialDemand(@PathVariable Long itemId,
                                            @RequestParam(defaultValue = "30") int days) {
        List<Map<String, Object>> predictions = predictionService.predictMaterialDemand(itemId, days);
        return AjaxResult.success(predictions);
    }

    /**
     * 预测产能
     */
    @GetMapping("/capacity/{workstationId}")
    public AjaxResult predictCapacity(@PathVariable Long workstationId,
                                      @RequestParam(defaultValue = "7") int days) {
        List<Map<String, Object>> predictions = predictionService.predictCapacity(workstationId, days);
        return AjaxResult.success(predictions);
    }

    /**
     * 获取预测看板
     */
    @GetMapping("/dashboard")
    public AjaxResult getPredictionDashboard() {
        return AjaxResult.success(predictionService.getPredictionDashboard());
    }

    /**
     * 获取预测准确率
     */
    @GetMapping("/accuracy")
    public AjaxResult getPredictionAccuracy(@RequestParam Date startDate,
                                            @RequestParam Date endDate) {
        return AjaxResult.success(predictionService.getPredictionAccuracy(startDate, endDate));
    }
}
