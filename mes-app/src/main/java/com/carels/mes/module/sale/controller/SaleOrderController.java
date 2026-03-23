package com.carels.mes.module.sale.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.sale.domain.SaleOrder;
import com.carels.mes.module.sale.service.ISaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 销售订单Controller
 */
@RestController
@RequestMapping("/mes/sale/order")
public class SaleOrderController extends BaseController {

    @Autowired
    private ISaleOrderService orderService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              SaleOrder order) {
        startPage(pageNum, pageSize);
        List<SaleOrder> list = orderService.selectSaleOrderList(order);
        return getDataTable(list);
    }

    @GetMapping("/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId) {
        return AjaxResult.success(orderService.selectSaleOrderById(orderId));
    }

    @GetMapping("/byNo/{orderNo}")
    public AjaxResult getByNo(@PathVariable String orderNo) {
        return AjaxResult.success(orderService.selectSaleOrderByNo(orderNo));
    }

    @PostMapping
    public AjaxResult add(@RequestBody SaleOrder order) {
        return toAjax(orderService.insertSaleOrder(order));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody SaleOrder order) {
        return toAjax(orderService.updateSaleOrder(order));
    }

    @DeleteMapping("/{orderId}")
    public AjaxResult remove(@PathVariable Long orderId) {
        return toAjax(orderService.deleteSaleOrderById(orderId));
    }

    @DeleteMapping("/batch/{orderIds}")
    public AjaxResult removeBatch(@PathVariable Long[] orderIds) {
        return toAjax(orderService.deleteSaleOrderByIds(orderIds));
    }

    @PutMapping("/{orderId}/submit")
    public AjaxResult submit(@PathVariable Long orderId) {
        orderService.submitOrder(orderId);
        return AjaxResult.success("订单提交成功");
    }

    @PutMapping("/{orderId}/audit")
    public AjaxResult audit(@PathVariable Long orderId) {
        orderService.auditOrder(orderId);
        return AjaxResult.success("订单审核通过");
    }

    @PutMapping("/{orderId}/cancel")
    public AjaxResult cancel(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return AjaxResult.success("订单已取消");
    }

    @PutMapping("/{orderId}/complete")
    public AjaxResult complete(@PathVariable Long orderId) {
        orderService.completeOrder(orderId);
        return AjaxResult.success("订单已完成");
    }

    @PutMapping("/{orderId}/relatePlan")
    public AjaxResult relatePlan(@PathVariable Long orderId, @RequestParam Long planId) {
        orderService.relatePlan(orderId, planId);
        return AjaxResult.success("关联生产计划成功");
    }

    @GetMapping("/availableForIssue")
    public AjaxResult availableForIssue() {
        List<SaleOrder> list = orderService.selectAvailableOrdersForIssue();
        return AjaxResult.success(list);
    }
}
