package com.carels.mes.module.plan.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.plan.domain.PlanSchedule;
import com.carels.mes.module.plan.service.ISmartSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 智能排产Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@RestController
@RequestMapping("/mes/plan/scheduling")
public class SmartSchedulingController extends BaseController {

    @Autowired
    private ISmartSchedulingService schedulingService;

    /**
     * 智能排产（自动）
     */
    @PostMapping("/auto")
    public AjaxResult autoSchedule(@RequestBody Map<String, Object> params) {
        Date startDate = params.get("startDate") != null ? new Date((Long) params.get("startDate")) : new Date();
        Date endDate = params.get("endDate") != null ? new Date((Long) params.get("endDate")) : new Date(startDate.getTime() + 30L * 24 * 60 * 60 * 1000);
        String algorithmType = (String) params.getOrDefault("algorithmType", "GREEDY");

        List<PlanSchedule> schedules = schedulingService.autoSchedule(startDate, endDate, algorithmType);
        return AjaxResult.success("排产完成", schedules);
    }

    /**
     * 单个工单排产
     */
    @PostMapping("/workorder/{workorderId}")
    public AjaxResult scheduleWorkorder(@PathVariable Long workorderId,
                                        @RequestParam(defaultValue = "GREEDY") String algorithmType) {
        PlanSchedule schedule = schedulingService.scheduleWorkorder(workorderId, algorithmType);
        return AjaxResult.success(schedule);
    }

    /**
     * 获取产能分析
     */
    @GetMapping("/capacity")
    public AjaxResult getCapacityAnalysis(@RequestParam Date startDate,
                                          @RequestParam Date endDate) {
        return AjaxResult.success(schedulingService.getCapacityAnalysis(startDate, endDate));
    }

    /**
     * 检查排产冲突
     */
    @PostMapping("/check-conflict")
    public AjaxResult checkConflicts(@RequestBody PlanSchedule schedule) {
        List<Map<String, Object>> conflicts = schedulingService.checkConflicts(schedule);
        return AjaxResult.success(conflicts);
    }

    /**
     * 优化排产
     */
    @PostMapping("/optimize")
    public AjaxResult optimizeSchedule(@RequestBody List<Long> scheduleIds) {
        List<PlanSchedule> schedules = schedulingService.optimizeSchedule(scheduleIds);
        return AjaxResult.success("优化完成", schedules);
    }

    /**
     * 重新排产
     */
    @PostMapping("/reschedule/{scheduleId}")
    public AjaxResult reschedule(@PathVariable Long scheduleId,
                                 @RequestBody Map<String, Object> params) {
        Date newStartTime = new Date((Long) params.get("newStartTime"));
        Long newWorkstationId = ((Number) params.get("newWorkstationId")).longValue();

        PlanSchedule schedule = schedulingService.reschedule(scheduleId, newStartTime, newWorkstationId);
        return AjaxResult.success(schedule);
    }

    /**
     * 获取甘特图数据
     */
    @GetMapping("/gantt")
    public AjaxResult getGanttData(@RequestParam Date startDate,
                                   @RequestParam Date endDate) {
        return AjaxResult.success(schedulingService.getGanttData(startDate, endDate));
    }

    /**
     * 评估排产可行性
     */
    @GetMapping("/feasibility/{workorderId}")
    public AjaxResult evaluateFeasibility(@PathVariable Long workorderId,
                                          @RequestParam Date deliveryDate) {
        return AjaxResult.success(schedulingService.evaluateFeasibility(workorderId, deliveryDate));
    }
}
