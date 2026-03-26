package com.carels.mes.module.cal.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.cal.domain.CalPlan;
import com.carels.mes.module.cal.service.ICalPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 排班计划管理Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/mes/cal/plan")
public class CalPlanController extends BaseController {

    @Autowired
    private ICalPlanService calPlanService;

    /**
     * 查询排班计划列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              CalPlan calPlan) {
        startPage(pageNum, pageSize);
        List<CalPlan> list = calPlanService.selectCalPlanList(calPlan);
        return getDataTable(list);
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{planId}")
    public AjaxResult getInfo(@PathVariable Long planId) {
        return AjaxResult.success(calPlanService.selectCalPlanById(planId));
    }

    /**
     * 新增排班计划
     */
    @PostMapping
    public AjaxResult add(@RequestBody CalPlan calPlan) {
        return toAjax(calPlanService.insertCalPlan(calPlan));
    }

    /**
     * 修改排班计划
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CalPlan calPlan) {
        return toAjax(calPlanService.updateCalPlan(calPlan));
    }

    /**
     * 删除排班计划
     */
    @DeleteMapping("/{planId}")
    public AjaxResult remove(@PathVariable Long planId) {
        return toAjax(calPlanService.deleteCalPlanById(planId));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch/{planIds}")
    public AjaxResult removeBatch(@PathVariable Long[] planIds) {
        return toAjax(calPlanService.deleteCalPlanByIds(planIds));
    }

    /**
     * 批量生成排班计划
     */
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CalPlan> planList) {
        return toAjax(calPlanService.batchGeneratePlan(planList));
    }
}
