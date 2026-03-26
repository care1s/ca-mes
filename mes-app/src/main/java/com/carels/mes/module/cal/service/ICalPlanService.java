package com.carels.mes.module.cal.service;

import com.carels.mes.module.cal.domain.CalPlan;

import java.util.List;

/**
 * 排班计划Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface ICalPlanService {

    /**
     * 查询排班计划列表
     */
    List<CalPlan> selectCalPlanList(CalPlan calPlan);

    /**
     * 根据ID查询
     */
    CalPlan selectCalPlanById(Long planId);

    /**
     * 新增排班计划
     */
    int insertCalPlan(CalPlan calPlan);

    /**
     * 修改排班计划
     */
    int updateCalPlan(CalPlan calPlan);

    /**
     * 删除排班计划
     */
    int deleteCalPlanById(Long planId);

    /**
     * 批量删除
     */
    int deleteCalPlanByIds(Long[] planIds);

    /**
     * 批量生成排班计划
     */
    int batchGeneratePlan(List<CalPlan> planList);
}
