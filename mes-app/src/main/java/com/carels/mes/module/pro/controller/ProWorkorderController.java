package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProWorkorder;
import com.carels.mes.module.pro.service.IProWorkorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生产工单Controller - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/pro/workorder")
public class ProWorkorderController extends BaseController {
    
    @Autowired
    private IProWorkorderService workorderService;
    
    /**
     * 查询工单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProWorkorder workorder) {
        startPage();
        List<ProWorkorder> list = workorderService.selectProWorkorderList(workorder);
        return getDataTable(list);
    }
    
    /**
     * 工单进度看板 - 核心加强功能
     */
    @GetMapping("/dashboard")
    public AjaxResult dashboard() {
        Map<String, Object> data = new HashMap<>();
        
        // 统计汇总
        Map<String, Object> summary = workorderService.getWorkorderSummary();
        data.put("summary", summary);
        
        // 工单列表
        List<Map<String, Object>> list = workorderService.getWorkorderDashboard();
        data.put("list", list);
        
        return AjaxResult.success(data);
    }
    
    /**
     * 根据ID查询工单详情
     */
    @GetMapping("/{workorderId}")
    public AjaxResult getInfo(@PathVariable Long workorderId) {
        return AjaxResult.success(workorderService.selectProWorkorderById(workorderId));
    }
    
    /**
     * 新增工单
     */
    @PostMapping
    public AjaxResult add(@RequestBody ProWorkorder workorder) {
        return toAjax(workorderService.insertProWorkorder(workorder));
    }
    
    /**
     * 修改工单
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ProWorkorder workorder) {
        return toAjax(workorderService.updateProWorkorder(workorder));
    }
    
    /**
     * 删除工单
     */
    @DeleteMapping("/{workorderId}")
    public AjaxResult remove(@PathVariable Long workorderId) {
        return toAjax(workorderService.deleteProWorkorderById(workorderId));
    }
    
    /**
     * 工单下达
     */
    @PutMapping("/release/{workorderId}")
    public AjaxResult release(@PathVariable Long workorderId) {
        return toAjax(workorderService.releaseWorkorder(workorderId));
    }
    
    /**
     * 工单关闭
     */
    @PutMapping("/close/{workorderId}")
    public AjaxResult close(@PathVariable Long workorderId) {
        return toAjax(workorderService.closeWorkorder(workorderId));
    }
}
