package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProFeedback;
import com.carels.mes.module.pro.domain.ProTask;
import com.carels.mes.module.pro.mapper.ProFeedbackMapper;
import com.carels.mes.module.pro.mapper.ProTaskMapper;
import com.carels.mes.module.pro.service.IProFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产报工Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@Service
public class ProFeedbackServiceImpl implements IProFeedbackService {

    @Autowired
    private ProFeedbackMapper feedbackMapper;

    @Autowired
    private ProTaskMapper taskMapper;

    @Override
    public List<ProFeedback> selectProFeedbackList(ProFeedback feedback) {
        return feedbackMapper.selectProFeedbackList(feedback);
    }

    @Override
    public ProFeedback selectProFeedbackById(Long feedbackId) {
        return feedbackMapper.selectProFeedbackById(feedbackId);
    }

    @Override
    public int insertProFeedback(ProFeedback feedback) {
        return feedbackMapper.insertProFeedback(feedback);
    }

    @Override
    public int updateProFeedback(ProFeedback feedback) {
        return feedbackMapper.updateProFeedback(feedback);
    }

    @Override
    public int deleteProFeedbackById(Long feedbackId) {
        return feedbackMapper.deleteProFeedbackById(feedbackId);
    }

    @Override
    public int deleteProFeedbackByIds(Long[] feedbackIds) {
        return feedbackMapper.deleteProFeedbackByIds(feedbackIds);
    }

    @Override
    public int approveFeedback(Long feedbackId, String status) {
        return feedbackMapper.approveFeedback(feedbackId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitFeedback(Long taskId, Double quantity, Long operatorId, String operatorName) {
        // 查询任务
        ProTask task = taskMapper.selectProTaskById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        // 校验任务状态
        if ("COMPLETED".equals(task.getStatus())) {
            throw new RuntimeException("任务已完成，不能继续报工");
        }

        // 校验报工数量
        double remainingQty = task.getPlanQuantity() - (task.getCompletedQuantity() != null ? task.getCompletedQuantity() : 0);
        if (quantity > remainingQty) {
            throw new RuntimeException("报工数量超过剩余数量，剩余可报工: " + remainingQty);
        }

        // 如果是首次报工，自动开始任务
        if ("PENDING".equals(task.getStatus())) {
            taskMapper.updateStatus(taskId, "PROCESSING");
        }

        // 更新完工数量
        Double newCompletedQty = (task.getCompletedQuantity() != null ? task.getCompletedQuantity() : 0) + quantity;
        task.setCompletedQuantity(newCompletedQty);
        taskMapper.updateProTask(task);

        // 重新查询任务获取最新状态
        task = taskMapper.selectProTaskById(taskId);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("feedbackQty", quantity);
        result.put("completedQuantity", task.getCompletedQuantity());
        result.put("planQuantity", task.getPlanQuantity());
        result.put("status", task.getStatus());
        result.put("progress", Math.round(task.getCompletedQuantity() / task.getPlanQuantity() * 100));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> submitFeedbackWithQuality(Long taskId, Double qualifiedQty, Double defectiveQty,
                                                           Long operatorId, String operatorName, String remark) {
        // 查询任务
        ProTask task = taskMapper.selectProTaskById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        // 校验任务状态
        if ("COMPLETED".equals(task.getStatus())) {
            throw new RuntimeException("任务已完成，不能继续报工");
        }

        // 计算总报工数量
        double totalQty = (qualifiedQty != null ? qualifiedQty : 0) + (defectiveQty != null ? defectiveQty : 0);

        // 校验报工数量
        double remainingQty = task.getPlanQuantity() - (task.getCompletedQuantity() != null ? task.getCompletedQuantity() : 0);
        if (totalQty > remainingQty) {
            throw new RuntimeException("报工数量超过剩余数量，剩余可报工: " + remainingQty);
        }

        // 如果是首次报工，自动开始任务
        if ("PENDING".equals(task.getStatus())) {
            taskMapper.updateStatus(taskId, "PROCESSING");
        }

        // 更新完工数量
        Double newCompletedQty = (task.getCompletedQuantity() != null ? task.getCompletedQuantity() : 0) + totalQty;
        Double newQualifiedQty = (task.getQualifiedQuantity() != null ? task.getQualifiedQuantity() : 0) + qualifiedQty;
        Double newDefectiveQty = (task.getDefectiveQuantity() != null ? task.getDefectiveQuantity() : 0) + defectiveQty;

        task.setCompletedQuantity(newCompletedQty);
        task.setQualifiedQuantity(newQualifiedQty);
        task.setDefectiveQuantity(newDefectiveQty);
        taskMapper.updateProTask(task);

        // 重新查询任务获取最新状态
        task = taskMapper.selectProTaskById(taskId);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskId);
        result.put("feedbackQty", totalQty);
        result.put("qualifiedQty", qualifiedQty);
        result.put("defectiveQty", defectiveQty);
        result.put("completedQuantity", task.getCompletedQuantity());
        result.put("planQuantity", task.getPlanQuantity());
        result.put("status", task.getStatus());
        result.put("progress", Math.round(task.getCompletedQuantity() / task.getPlanQuantity() * 100));

        return result;
    }
}
