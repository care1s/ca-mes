package com.carels.mes.module.plan.service;

import com.carels.mes.module.plan.domain.CapacityModel;
import com.carels.mes.module.plan.domain.PlanSchedule;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 智能排产Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
public interface ISmartSchedulingService {

    /**
     * 智能排产（自动为所有待排产工单生成生产计划）
     *
     * @param startDate 排产开始日期
     * @param endDate 排产结束日期
     * @param algorithmType 算法类型: GENETIC-遗传算法, GREEDY-贪心算法
     * @return 排产结果列表
     */
    List<PlanSchedule> autoSchedule(Date startDate, Date endDate, String algorithmType);

    /**
     * 单个工单智能排产
     *
     * @param workorderId 工单ID
     * @param algorithmType 算法类型
     * @return 排产结果
     */
    PlanSchedule scheduleWorkorder(Long workorderId, String algorithmType);

    /**
     * 获取产能分析
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 各工作站产能负载情况
     */
    List<CapacityModel> getCapacityAnalysis(Date startDate, Date endDate);

    /**
     * 检查排产冲突
     *
     * @param schedule 排产计划
     * @return 冲突信息列表
     */
    List<Map<String, Object>> checkConflicts(PlanSchedule schedule);

    /**
     * 优化排产（针对已有排产进行优化调整）
     *
     * @param scheduleIds 需要优化的排产ID列表
     * @return 优化后的排产结果
     */
    List<PlanSchedule> optimizeSchedule(List<Long> scheduleIds);

    /**
     * 重新排产（手动调整后的重新计算）
     *
     * @param scheduleId 排产ID
     * @param newStartTime 新的开始时间
     * @param newWorkstationId 新的工作站ID
     * @return 调整后的排产结果
     */
    PlanSchedule reschedule(Long scheduleId, Date newStartTime, Long newWorkstationId);

    /**
     * 获取排产甘特图数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 甘特图数据
     */
    Map<String, Object> getGanttData(Date startDate, Date endDate);

    /**
     * 评估排产可行性
     *
     * @param workorderId 工单ID
     * @param deliveryDate 要求交期
     * @return 可行性评估结果
     */
    Map<String, Object> evaluateFeasibility(Long workorderId, Date deliveryDate);
}
