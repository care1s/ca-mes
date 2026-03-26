package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.pro.domain.ProRouteProcess;
import com.carels.mes.module.pro.service.IProRouteProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工艺路线工序Controller - carels
 */
@RestController
@RequestMapping("/mes/pro/route/process")
public class ProRouteProcessController extends BaseController {

    @Autowired
    private IProRouteProcessService routeProcessService;

    /**
     * 根据工艺路线ID查询工序列表
     */
    @GetMapping("/list/{routeId}")
    public AjaxResult listByRouteId(@PathVariable Long routeId) {
        List<ProRouteProcess> list = routeProcessService.selectByRouteId(routeId);
        return AjaxResult.success(list);
    }

    /**
     * 新增工艺路线工序
     */
    @PostMapping
    public AjaxResult add(@RequestBody ProRouteProcess routeProcess) {
        routeProcess.setCreateBy(getUsername());
        int result = routeProcessService.insert(routeProcess);
        return toAjax(result);
    }

    /**
     * 修改工艺路线工序
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ProRouteProcess routeProcess) {
        int result = routeProcessService.update(routeProcess);
        return toAjax(result);
    }

    /**
     * 删除工艺路线工序
     */
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        int result = routeProcessService.deleteById(id);
        return toAjax(result);
    }

    /**
     * 保存工艺路线的工序配置（批量保存）
     */
    @PostMapping("/save/{routeId}")
    public AjaxResult saveRouteProcesses(@PathVariable Long routeId, @RequestBody List<ProRouteProcess> processes) {
        int result = routeProcessService.saveRouteProcesses(routeId, processes, getUsername());
        return toAjax(result);
    }

    /**
     * 获取当前登录用户名
     */
    private String getUsername() {
        // 从SecurityUtils获取当前用户名，简化处理
        return "admin";
    }
}
