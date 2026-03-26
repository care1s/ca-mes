package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProPlan;
import com.carels.mes.module.pro.domain.ProWorkorder;
import com.carels.mes.module.pro.mapper.ProPlanMapper;
import com.carels.mes.module.pro.service.IProPlanService;
import com.carels.mes.module.pro.service.IProWorkorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 生产计划Service实现
 */
@Service
public class ProPlanServiceImpl implements IProPlanService {

    @Autowired
    private ProPlanMapper planMapper;

    @Autowired
    private IProWorkorderService workorderService;

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
        // 自动生成计划单号（格式：PPyyyyMMddXXXX）
        if (plan.getPlanNo() == null || plan.getPlanNo().trim().isEmpty()) {
            plan.setPlanNo(generatePlanNo());
        }
        plan.setStatus(0);
        plan.setCompletionRate(0.0);
        plan.setCreateTime(LocalDateTime.now());
        plan.setUpdateTime(LocalDateTime.now());
        return planMapper.insertProPlan(plan);
    }

    /**
     * 生成计划单号
     * 格式：PP + 年月日 + 4位序号（PP = Production Plan）
     */
    private String generatePlanNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 9000) + 1000);
        return "PP" + dateStr + seq;
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

    @Override
    public Long generateWorkorderFromPlan(Long planId) {
        // 获取计划详情
        ProPlan plan = planMapper.selectProPlanById(planId);
        if (plan == null) {
            throw new RuntimeException("生产计划不存在");
        }

        // 调用工单服务生成工单（默认车间为空，需要在前端选择车间）
        ProWorkorder workorder = workorderService.createWorkorderFromPlan(planId, null, null);

        return workorder.getWorkorderId();
    }
}
