package com.carels.mes.module.sale.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.sale.domain.SaleDelivery;
import com.carels.mes.module.sale.service.ISaleDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 销售出库单Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/sale/delivery")
public class SaleDeliveryController extends BaseController {

    @Autowired
    private ISaleDeliveryService deliveryService;

    @GetMapping("/list")
    public TableDataInfo list(SaleDelivery delivery) {
        startPage();
        List<SaleDelivery> list = deliveryService.selectSaleDeliveryList(delivery);
        return getDataTable(list);
    }

    @GetMapping("/{deliveryId}")
    public AjaxResult getInfo(@PathVariable Long deliveryId) {
        return AjaxResult.success(deliveryService.selectSaleDeliveryById(deliveryId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody SaleDelivery delivery) {
        return toAjax(deliveryService.insertSaleDelivery(delivery));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody SaleDelivery delivery) {
        return toAjax(deliveryService.updateSaleDelivery(delivery));
    }

    @PutMapping("/{deliveryId}/submit")
    public AjaxResult submit(@PathVariable Long deliveryId) {
        return toAjax(deliveryService.submitDelivery(deliveryId));
    }

    @PutMapping("/{deliveryId}/audit")
    public AjaxResult audit(@PathVariable Long deliveryId) {
        return toAjax(deliveryService.auditDelivery(deliveryId));
    }

    @PutMapping("/{deliveryId}/ship")
    public AjaxResult ship(@PathVariable Long deliveryId,
                           @RequestParam String trackingNo,
                           @RequestParam String logisticsCompany) {
        return toAjax(deliveryService.shipDelivery(deliveryId, trackingNo, logisticsCompany));
    }

    @PutMapping("/{deliveryId}/complete")
    public AjaxResult complete(@PathVariable Long deliveryId) {
        return toAjax(deliveryService.completeDelivery(deliveryId));
    }

    @DeleteMapping("/{deliveryIds}")
    public AjaxResult remove(@PathVariable Long[] deliveryIds) {
        return toAjax(deliveryService.deleteSaleDeliveryByIds(deliveryIds));
    }
}
