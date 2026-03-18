package com.carels.mes.module.pur.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pur.domain.PurReturn;
import com.carels.mes.module.pur.service.IPurReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 采购退货Controller
 */
@RestController
@RequestMapping("/mes/pur/return")
public class PurReturnController extends BaseController {
    
    @Autowired
    private IPurReturnService purReturnService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              PurReturn purReturn) {
        startPage(pageNum, pageSize);
        List<PurReturn> list = purReturnService.selectPurReturnList(purReturn);
        return getDataTable(list);
    }
    
    @GetMapping("/{returnId}")
    public AjaxResult getInfo(@PathVariable Long returnId) {
        return AjaxResult.success(purReturnService.selectPurReturnById(returnId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody PurReturn purReturn) {
        return toAjax(purReturnService.insertPurReturn(purReturn));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody PurReturn purReturn) {
        return toAjax(purReturnService.updatePurReturn(purReturn));
    }
    
    @DeleteMapping("/{returnId}")
    public AjaxResult remove(@PathVariable Long returnId) {
        return toAjax(purReturnService.deletePurReturnById(returnId));
    }
}
