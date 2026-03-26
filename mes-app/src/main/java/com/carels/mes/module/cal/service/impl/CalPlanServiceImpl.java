package com.carels.mes.module.cal.service.impl;

import com.carels.mes.module.cal.domain.CalPlan;
import com.carels.mes.module.cal.mapper.CalPlanMapper;
import com.carels.mes.module.cal.service.ICalPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班计划Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Service
public class CalPlanServiceImpl implements ICalPlanService {

    @Autowired
    private CalPlanMapper calPlanMapper;

    @Override
    public List<CalPlan> selectCalPlanList(CalPlan calPlan) {
        return calPlanMapper.selectCalPlanList(calPlan);
    }

    @Override
    public CalPlan selectCalPlanById(Long planId) {
        return calPlanMapper.selectCalPlanById(planId);
    }

    @Override
    public int insertCalPlan(CalPlan calPlan) {
        return calPlanMapper.insertCalPlan(calPlan);
    }

    @Override
    public int updateCalPlan(CalPlan calPlan) {
        return calPlanMapper.updateCalPlan(calPlan);
    }

    @Override
    public int deleteCalPlanById(Long planId) {
        return calPlanMapper.deleteCalPlanById(planId);
    }

    @Override
    public int deleteCalPlanByIds(Long[] planIds) {
        return calPlanMapper.deleteCalPlanByIds(planIds);
    }

    @Override
    public int batchGeneratePlan(List<CalPlan> planList) {
        return calPlanMapper.batchInsert(planList);
    }
}
