package com.carels.mes.module.sale.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.sale.domain.SaleReturn;
import com.carels.mes.module.sale.service.ISaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 销售退货单Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/sale/return")
public class SaleReturnController extends BaseController {

    @Autowired
    private ISaleReturnService returnService;

    @GetMapping("/list")
    public TableDataInfo list(SaleReturn saleReturn) {
        startPage();
        List<SaleReturn> list = returnService.selectSaleReturnList(saleReturn);
        return getDataTable(list);
    }

    @GetMapping("/{returnId}")
    public AjaxResult getInfo(@PathVariable Long returnId) {
        return AjaxResult.success(returnService.selectSaleReturnById(returnId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody SaleReturn saleReturn) {
        return toAjax(returnService.insertSaleReturn(saleReturn));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody SaleReturn saleReturn) {
        return toAjax(returnService.updateSaleReturn(saleReturn));
    }

    @PutMapping("/{returnId}/submit")
    public AjaxResult submit(@PathVariable Long returnId) {
        return toAjax(returnService.submitReturn(returnId));
    }

    @PutMapping("/{returnId}/audit")
    public AjaxResult audit(@PathVariable Long returnId) {
        return toAjax(returnService.auditReturn(returnId));
    }

    @PutMapping("/{returnId}/receive")
    public AjaxResult receive(@PathVariable Long returnId) {
        return toAjax(returnService.receiveReturn(returnId));
    }

    @DeleteMapping("/{returnIds}")
    public AjaxResult remove(@PathVariable Long[] returnIds) {
        return toAjax(returnService.deleteSaleReturnByIds(returnIds));
    }
}
