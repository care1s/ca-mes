package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmLocation;
import com.carels.mes.module.wm.service.IWmLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 仓位管理Controller
 */
@RestController
@RequestMapping("/mes/wm/location")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WmLocationController extends BaseController {
    
    @Autowired
    private IWmLocationService locationService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmLocation location) {
        startPage(pageNum, pageSize);
        List<WmLocation> list = locationService.selectWmLocationList(location);
        return getDataTable(list);
    }
    
    @GetMapping("/{locationId}")
    public AjaxResult getInfo(@PathVariable Long locationId) {
        return AjaxResult.success(locationService.selectWmLocationById(locationId));
    }
    
    @GetMapping("/zone/{zoneId}")
    public AjaxResult getByZone(@PathVariable Long zoneId) {
        return AjaxResult.success(locationService.selectWmLocationByZoneId(zoneId));
    }
    
    @GetMapping("/warehouse/{warehouseId}")
    public AjaxResult getByWarehouse(@PathVariable Long warehouseId) {
        return AjaxResult.success(locationService.selectWmLocationByWarehouseId(warehouseId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmLocation location) {
        return toAjax(locationService.insertWmLocation(location));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmLocation location) {
        return toAjax(locationService.updateWmLocation(location));
    }
    
    @DeleteMapping("/{locationId}")
    public AjaxResult remove(@PathVariable Long locationId) {
        return toAjax(locationService.deleteWmLocationById(locationId));
    }
    
    @DeleteMapping("/batch/{locationIds}")
    public AjaxResult removeBatch(@PathVariable Long[] locationIds) {
        return toAjax(locationService.deleteWmLocationByIds(locationIds));
    }
}
