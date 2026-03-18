package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProPlan;
import com.carels.mes.module.pro.mapper.ProPlanMapper;
import com.carels.mes.module.pro.service.IProPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生产计划Service实现
 */
@Service
public class ProPlanServiceImpl implements IProPlanService {
    
    @Autowired
    private ProPlanMapper planMapper;
    
    @Override
    public List<ProPlan> selectProPlanList(ProPlan plan) {
        return planMapper.selectProPlanList(plan);
    }
    
    @Override
    public ProPlan selectProPlanById(Long planId) {
        return planMapper.selectProPlanById(planId);
    }
    
    @Override
    public int insertProPlan(ProPlan plan) {
        plan.setStatus(0);
        plan.setCompletionRate(0.0);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        return planMapper.insertProPlan(plan);
    }
    
    @Override
    public int updateProPlan(ProPlan plan) {
        plan.setUpdateTime(LocalDateTime.now());
        return planMapper.updateProPlan(plan);
    }
    
    @Override
    public int deleteProPlanById(Long planId) {
        return planMapper.deleteProPlanById(planId);
    }
    
    @Override
    public void publishPlan(Long planId) {
        planMapper.updateStatus(planId, 1);
    }
    
    @Override
    public void startPlan(Long planId) {
        planMapper.updateStatus(planId, 2);
    }
    
    @Override
    public void completePlan(Long planId) {
        planMapper.updateStatus(planId, 3);
    }
    
    @Override
    public void cancelPlan(Long planId) {
        planMapper.updateStatus(planId, 4);
    }
}
