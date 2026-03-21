package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmZone;
import com.carels.mes.module.wm.service.IWmZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 仓区管理Controller
 */
@RestController
@RequestMapping("/mes/wm/zone")
public class WmZoneController extends BaseController {
    
    @Autowired
    private IWmZoneService zoneService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmZone zone) {
        startPage(pageNum, pageSize);
        List<WmZone> list = zoneService.selectWmZoneList(zone);
        return getDataTable(list);
    }
    
    @GetMapping("/{zoneId}")
    public AjaxResult getInfo(@PathVariable Long zoneId) {
        return AjaxResult.success(zoneService.selectWmZoneById(zoneId));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public AjaxResult getByWarehouse(@PathVariable Long warehouseId) {
        return AjaxResult.success(zoneService.selectWmZoneByWarehouseId(warehouseId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmZone zone) {
        return toAjax(zoneService.insertWmZone(zone));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmZone zone) {
        return toAjax(zoneService.updateWmZone(zone));
    }
    
    @DeleteMapping("/{zoneId}")
    public AjaxResult remove(@PathVariable Long zoneId) {
        return toAjax(zoneService.deleteWmZoneById(zoneId));
    }
    
    @DeleteMapping("/batch/{zoneIds}")
    public AjaxResult removeBatch(@PathVariable Long[] zoneIds) {
        return toAjax(zoneService.deleteWmZoneByIds(zoneIds));
    }
}
