package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProPlan;

import java.util.List;

/**
 * 生产计划Service接口
 */
public interface IProPlanService {
    
    /**
     * 查询计划列表
     */
    List<ProPlan> selectProPlanList(ProPlan plan);
    
    /**
     * 根据ID查询计划
     */
    ProPlan selectProPlanById(Long planId);
    
    /**
     * 新增计划
     */
    int insertProPlan(ProPlan plan);
    
    /**
     * 修改计划
     */
    int updateProPlan(ProPlan plan);
    
    /**
     * 删除计划
     */
    int deleteProPlanById(Long planId);
    
    /**
     * 发布计划
     */
    void publishPlan(Long planId);
    
    /**
     * 开始执行计划
     */
    void startPlan(Long planId);
    
    /**
     * 完成计划
     */
    void completePlan(Long planId);
    
    /**
     * 取消计划
     */
    void cancelPlan(Long planId);

    /**
     * 从计划生成工单
     * @param planId 计划ID
     * @return 生成的工单ID
     */
    Long generateWorkorderFromPlan(Long planId);
}
