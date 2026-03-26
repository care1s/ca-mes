package com.carels.mes.module.cal.mapper;

import com.carels.mes.module.cal.domain.CalPlan;

import java.util.List;

/**
 * 排班计划Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface CalPlanMapper {

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
     * 检查日期班次是否唯一
     */
    int checkPlanUnique(CalPlan calPlan);

    /**
     * 批量新增
     */
    int batchInsert(List<CalPlan> list);
}
