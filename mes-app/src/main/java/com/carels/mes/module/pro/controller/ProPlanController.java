package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProPlan;
import com.carels.mes.module.pro.service.IProPlanService;
import com.carels.mes.module.pro.service.IProWorkorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产计划Controller
 */
@RestController
@RequestMapping("/mes/pro/plan")
public class ProPlanController extends BaseController {

    @Autowired
    private IProPlanService planService;

    @Autowired
    private IProWorkorderService workorderService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              ProPlan plan) {
        startPage(pageNum, pageSize);
        List<ProPlan> list = planService.selectProPlanList(plan);
        return getDataTable(list);
    }
    
    @GetMapping("/{planId}")
    public AjaxResult getInfo(@PathVariable Long planId) {
        return AjaxResult.success(planService.selectProPlanById(planId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody ProPlan plan) {
        return toAjax(planService.insertProPlan(plan));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody ProPlan plan) {
        return toAjax(planService.updateProPlan(plan));
    }
    
    @DeleteMapping("/{planId}")
    public AjaxResult remove(@PathVariable Long planId) {
        return toAjax(planService.deleteProPlanById(planId));
    }
    
    @PutMapping("/{planId}/publish")
    public AjaxResult publish(@PathVariable Long planId,
                              @RequestParam(required = false) Long workshopId,
                              @RequestParam(required = false) String workshopName) {
        // 1. 发布计划
        planService.publishPlan(planId);

        // 2. 自动生成工单
        try {
            workorderService.createWorkorderFromPlan(planId, workshopId, workshopName);
            return AjaxResult.success("计划发布成功，已自动生成工单");
        } catch (Exception e) {
            return AjaxResult.success("计划发布成功，但工单生成失败：" + e.getMessage());
        }
    }
    
    @PutMapping("/{planId}/start")
    public AjaxResult start(@PathVariable Long planId) {
        planService.startPlan(planId);
        return AjaxResult.success("计划开始执行");
    }
    
    @PutMapping("/{planId}/complete")
    public AjaxResult complete(@PathVariable Long planId) {
        planService.completePlan(planId);
        return AjaxResult.success("计划已完成");
    }
    
    @PutMapping("/{planId}/cancel")
    public AjaxResult cancel(@PathVariable Long planId) {
        planService.cancelPlan(planId);
        return AjaxResult.success("计划已取消");
    }
}
