package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmWarehouse;
import com.carels.mes.module.wm.service.IWmWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库管理Controller
 */
@RestController
@RequestMapping("/mes/wm/warehouse")
public class WmWarehouseController extends BaseController {
    
    @Autowired
    private IWmWarehouseService warehouseService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmWarehouse warehouse) {
        startPage(pageNum, pageSize);
        List<WmWarehouse> list = warehouseService.selectWmWarehouseList(warehouse);
        return getDataTable(list);
    }
    
    @GetMapping("/{warehouseId}")
    public AjaxResult getInfo(@PathVariable Long warehouseId) {
        return AjaxResult.success(warehouseService.selectWmWarehouseById(warehouseId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmWarehouse warehouse) {
        return toAjax(warehouseService.insertWmWarehouse(warehouse));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmWarehouse warehouse) {
        return toAjax(warehouseService.updateWmWarehouse(warehouse));
    }
    
    @DeleteMapping("/{warehouseId}")
    public AjaxResult remove(@PathVariable Long warehouseId) {
        return toAjax(warehouseService.deleteWmWarehouseById(warehouseId));
    }
    
    @PutMapping("/{warehouseId}/status")
    public AjaxResult updateStatus(@PathVariable Long warehouseId, @RequestParam String status) {
        warehouseService.updateStatus(warehouseId, status);
        return AjaxResult.success("状态更新成功");
    }
}
