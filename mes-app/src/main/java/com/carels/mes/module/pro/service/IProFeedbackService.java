package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProFeedback;

import java.util.List;
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
     * 查询报工列表
     */
    List<ProFeedback> selectProFeedbackList(ProFeedback feedback);

    /**
     * 根据ID查询报工
     */
    ProFeedback selectProFeedbackById(Long feedbackId);

    /**
     * 新增报工
     */
    int insertProFeedback(ProFeedback feedback);

    /**
     * 修改报工
     */
    int updateProFeedback(ProFeedback feedback);

    /**
     * 删除报工
     */
    int deleteProFeedbackById(Long feedbackId);

    /**
     * 批量删除报工
     */
    int deleteProFeedbackByIds(Long[] feedbackIds);

    /**
     * 审核报工
     */
    int approveFeedback(Long feedbackId, String status);

    /**
     * 提交报工（移动端用）
     *
     * @param taskId 任务ID
     * @param quantity 报工数量
     * @param operatorId 操作员ID
     * @param operatorName 操作员名称
     * @return 报工结果
     */
    Map<String, Object> submitFeedback(Long taskId, Double quantity, Long operatorId, String operatorName);

    /**
     * 提交报工（带合格/不合格数量，移动端用）
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
