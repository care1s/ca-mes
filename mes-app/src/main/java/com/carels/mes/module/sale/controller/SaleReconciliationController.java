package com.carels.mes.module.sale.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.sale.domain.SaleReconciliation;
import com.carels.mes.module.sale.service.ISaleReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售对账Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/sale/reconciliation")
public class SaleReconciliationController extends BaseController {

    @Autowired
    private ISaleReconciliationService reconciliationService;

    @GetMapping("/list")
    public TableDataInfo list(SaleReconciliation reconciliation) {
        startPage();
        List<SaleReconciliation> list = reconciliationService.selectSaleReconciliationList(reconciliation);
        return getDataTable(list);
    }

    @GetMapping("/{reconId}")
    public AjaxResult getInfo(@PathVariable Long reconId) {
        return AjaxResult.success(reconciliationService.selectSaleReconciliationById(reconId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody SaleReconciliation reconciliation) {
        return toAjax(reconciliationService.insertSaleReconciliation(reconciliation));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody SaleReconciliation reconciliation) {
        return toAjax(reconciliationService.updateSaleReconciliation(reconciliation));
    }

    @PutMapping("/{reconId}/confirm")
    public AjaxResult confirm(@PathVariable Long reconId) {
        return toAjax(reconciliationService.confirmReconciliation(reconId));
    }

    @PutMapping("/{reconId}/invoice")
    public AjaxResult invoice(@PathVariable Long reconId,
                              @RequestParam String invoiceNo,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate) {
        return toAjax(reconciliationService.invoiceReconciliation(reconId, invoiceNo, invoiceDate));
    }

    @PutMapping("/{reconId}/receive")
    public AjaxResult receive(@PathVariable Long reconId,
                              @RequestParam BigDecimal receivedAmount,
                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date receiveDate) {
        return toAjax(reconciliationService.receivePayment(reconId, receivedAmount, receiveDate));
    }

    @DeleteMapping("/{reconIds}")
    public AjaxResult remove(@PathVariable Long[] reconIds) {
        return toAjax(reconciliationService.deleteSaleReconciliationByIds(reconIds));
    }
}
