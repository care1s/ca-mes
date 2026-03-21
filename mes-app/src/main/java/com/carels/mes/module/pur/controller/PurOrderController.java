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

    @DeleteMapping("/{orderId}")
    public AjaxResult remove(@PathVariable Long orderId) {
        return toAjax(purOrderService.deletePurOrderById(orderId));
    }

    @PostMapping("/from-request/{requestId}")
    public AjaxResult createFromRequest(@PathVariable Long requestId) {
        Long orderId = purOrderService.createOrderFromRequest(requestId);
        return AjaxResult.success("订单生成成功", orderId);
    }
}
