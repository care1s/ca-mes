package com.carels.mes.module.pro.service;

import java.util.Map;

/**
 * 生产报工Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
public interface IProFeedbackService {

    /**
     * 提交报工
     *
     * @param taskId 任务ID
     * @param quantity 报工数量
     * @param operatorId 操作员ID
     * @param operatorName 操作员名称
     * @return 报工结果
     */
    Map<String, Object> submitFeedback(Long taskId, Double quantity, Long operatorId, String operatorName);

    /**
     * 提交报工（带合格/不合格数量）
     *
     * @param taskId 任务ID
     * @param qualifiedQty 合格数量
     * @param defectiveQty 不合格数量
     * @param operatorId 操作员ID
     * @param operatorName 操作员名称
     * @param remark 备注
     * @return 报工结果
     */
    Map<String, Object> submitFeedbackWithQuality(Long taskId, Double qualifiedQty, Double defectiveQty,
                                                   Long operatorId, String operatorName, String remark);
}
