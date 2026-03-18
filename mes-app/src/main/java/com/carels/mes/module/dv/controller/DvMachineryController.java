package com.carels.mes.module.dv.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.dv.domain.DvMachinery;
import com.carels.mes.module.dv.service.IDvMachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mes/dv/machinery")
public class DvMachineryController extends BaseController {
    
    @Autowired
    private IDvMachineryService machineryService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              DvMachinery machinery) {
        startPage(pageNum, pageSize);
        List<DvMachinery> list = machineryService.selectDvMachineryList(machinery);
        return getDataTable(list);
    }
    
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(machineryService.selectDvMachineryById(id));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody DvMachinery machinery) {
        return toAjax(machineryService.insertDvMachinery(machinery));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody DvMachinery machinery) {
        return toAjax(machineryService.updateDvMachinery(machinery));
    }
    
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(machineryService.deleteDvMachineryById(id));
    }
    
    @PutMapping("/{id}/status")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String status) {
        machineryService.updateStatus(id, status);
        return AjaxResult.success("状态更新成功");
    }
}
