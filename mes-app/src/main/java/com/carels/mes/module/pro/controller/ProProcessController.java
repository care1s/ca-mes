package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProProcess;
import com.carels.mes.module.pro.service.IProProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工序Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/pro/process")
public class ProProcessController extends BaseController {

    @Autowired
    private IProProcessService processService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              ProProcess process) {
        startPage(pageNum, pageSize);
        List<ProProcess> list = processService.selectProProcessList(process);
        return getDataTable(list);
    }

    @GetMapping("/{processId}")
    public AjaxResult getInfo(@PathVariable Long processId) {
        return AjaxResult.success(processService.selectProProcessById(processId));
    }

    @GetMapping("/byCode/{processCode}")
    public AjaxResult getByCode(@PathVariable String processCode) {
        return AjaxResult.success(processService.selectProProcessByCode(processCode));
    }

    @GetMapping("/byWorkshop/{workshopId}")
    public AjaxResult getByWorkshop(@PathVariable Long workshopId) {
        List<ProProcess> list = processService.selectByWorkshopId(workshopId);
        return AjaxResult.success(list);
    }

    @PostMapping
    public AjaxResult add(@RequestBody ProProcess process) {
        return toAjax(processService.insertProProcess(process));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ProProcess process) {
        return toAjax(processService.updateProProcess(process));
    }

    @DeleteMapping("/{processId}")
    public AjaxResult remove(@PathVariable Long processId) {
        return toAjax(processService.deleteProProcessById(processId));
    }

    @DeleteMapping("/batch/{processIds}")
    public AjaxResult removeBatch(@PathVariable Long[] processIds) {
        return toAjax(processService.deleteProProcessByIds(processIds));
    }

    @PutMapping("/{processId}/enable")
    public AjaxResult enable(@PathVariable Long processId) {
        processService.enableProcess(processId);
        return AjaxResult.success("工序已启用");
    }

    @PutMapping("/{processId}/disable")
    public AjaxResult disable(@PathVariable Long processId) {
        processService.disableProcess(processId);
        return AjaxResult.success("工序已停用");
    }
}
