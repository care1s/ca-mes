package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProTask;
import com.carels.mes.module.pro.service.IProTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产任务Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/pro/task")
public class ProTaskController extends BaseController {

    @Autowired
    private IProTaskService taskService;

    /**
     * 查询任务列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProTask task) {
        startPage();
        List<ProTask> list = taskService.selectProTaskList(task);
        return getDataTable(list);
    }

    /**
     * 根据ID查询任务详情
     */
    @GetMapping("/{taskId}")
    public AjaxResult getInfo(@PathVariable Long taskId) {
        return AjaxResult.success(taskService.selectProTaskById(taskId));
    }

    /**
     * 新增任务
     */
    @PostMapping
    public AjaxResult add(@RequestBody ProTask task) {
        return toAjax(taskService.insertProTask(task));
    }

    /**
     * 修改任务
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ProTask task) {
        return toAjax(taskService.updateProTask(task));
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{taskId}")
    public AjaxResult remove(@PathVariable Long taskId) {
        return toAjax(taskService.deleteProTaskById(taskId));
    }

    /**
     * 开始任务
     */
    @PutMapping("/start/{taskId}")
    public AjaxResult start(@PathVariable Long taskId) {
        return toAjax(taskService.startTask(taskId));
    }

    /**
     * 完成任务
     */
    @PutMapping("/complete/{taskId}")
    public AjaxResult complete(@PathVariable Long taskId) {
        return toAjax(taskService.completeTask(taskId));
    }
}
