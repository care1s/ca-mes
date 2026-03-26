package com.carels.mes.module.tm.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.tm.domain.TmTool;
import com.carels.mes.module.tm.service.ITmToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工装机具Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/tm/tool")
public class TmToolController extends BaseController {

    @Autowired
    private ITmToolService toolService;

    @GetMapping("/list")
    public TableDataInfo list(TmTool tool) {
        startPage();
        List<TmTool> list = toolService.selectTmToolList(tool);
        return getDataTable(list);
    }

    @GetMapping("/{toolId}")
    public AjaxResult getInfo(@PathVariable Long toolId) {
        return AjaxResult.success(toolService.selectTmToolById(toolId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody TmTool tool) {
        return toAjax(toolService.insertTmTool(tool));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody TmTool tool) {
        return toAjax(toolService.updateTmTool(tool));
    }

    @PutMapping("/{toolId}/maintain")
    public AjaxResult maintain(@PathVariable Long toolId) {
        return toAjax(toolService.maintainTool(toolId));
    }

    @PutMapping("/{toolId}/scrap")
    public AjaxResult scrap(@PathVariable Long toolId) {
        return toAjax(toolService.scrapTool(toolId));
    }

    @DeleteMapping("/{toolIds}")
    public AjaxResult remove(@PathVariable Long[] toolIds) {
        return toAjax(toolService.deleteTmToolByIds(toolIds));
    }
}
