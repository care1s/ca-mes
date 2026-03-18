package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmWarning;
import com.carels.mes.module.wm.service.IWmWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存预警Controller
 */
@RestController
@RequestMapping("/mes/wm/warning")
public class WmWarningController extends BaseController {
    
    @Autowired
    private IWmWarningService warningService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmWarning warning) {
        startPage(pageNum, pageSize);
        List<WmWarning> list = warningService.selectWmWarningList(warning);
        return getDataTable(list);
    }
    
    @GetMapping("/{warningId}")
    public AjaxResult getInfo(@PathVariable Long warningId) {
        return AjaxResult.success(warningService.selectWmWarningById(warningId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmWarning warning) {
        return toAjax(warningService.insertWmWarning(warning));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmWarning warning) {
        return toAjax(warningService.updateWmWarning(warning));
    }
    
    @DeleteMapping("/{warningId}")
    public AjaxResult remove(@PathVariable Long warningId) {
        return toAjax(warningService.deleteWmWarningById(warningId));
    }
    
    @PutMapping("/{warningId}/handle")
    public AjaxResult handle(@PathVariable Long warningId, @RequestBody WmWarning warning) {
        warningService.handleWarning(warningId, warning.getHandleRemark(), warning.getHandlerName());
        return AjaxResult.success("预警已处理");
    }
    
    @GetMapping("/count")
    public AjaxResult count(@RequestParam(required = false) Integer warningType,
                            @RequestParam(required = false) Integer warningLevel) {
        return AjaxResult.success(warningService.countWarning(warningType, warningLevel));
    }
}
