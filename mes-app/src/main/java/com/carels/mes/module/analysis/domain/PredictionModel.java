package com.carels.mes.module.analysis.domain;

import lombok.Data;

import java.util.Date;

/**
 * 预测分析模型 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Data
public class PredictionModel {

    /** 预测类型: DELIVERY-交期预测, QUALITY-质量预测, DEVICE-设备预测, DEMAND-需求预测 */
    private String predictionType;

    /** 预测对象ID */
    private Long targetId;

    /** 预测对象名称 */
    private String targetName;

    /** 预测值 */
    private Double predictedValue;

    /** 预测区间下限 */
    private Double lowerBound;

    /** 预测区间上限 */
    private Double upperBound;

    /** 置信度（%） */
    private Double confidence;

    /** 预测日期 */
    private Date predictionDate;

    /** 预测依据 */
    private String basis;

    /** 风险等级: HIGH-高风险, MEDIUM-中风险, LOW-低风险 */
    private String riskLevel;

    /** 建议措施 */
    private String suggestion;
}
