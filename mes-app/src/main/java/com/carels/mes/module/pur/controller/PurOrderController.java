package com.carels.mes.module.pur.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pur.domain.PurOrder;
import com.carels.mes.module.pur.service.IPurOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 采购订单Controller
 */
@RestController
@RequestMapping("/mes/pur/order")
public class PurOrderController extends BaseController {
    
    @Autowired
    private IPurOrderService purOrderService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              PurOrder purOrder) {
        startPage(pageNum, pageSize);
        List<PurOrder> list = purOrderService.selectPurOrderList(purOrder);
        return getDataTable(list);
    }
    
    @GetMapping("/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId) {
        return AjaxResult.success(purOrderService.selectPurOrderById(orderId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody PurOrder purOrder) {
        return toAjax(purOrderService.insertPurOrder(purOrder));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody PurOrder purOrder) {
        return toAjax(purOrderService.updatePurOrder(purOrder));
    }
    
    @DeleteMapping("/{orderId}")
    public AjaxResult remove(@PathVariable Long orderId) {
        return toAjax(purOrderService.deletePurOrderById(orderId));
    }
}
