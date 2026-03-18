package com.carels.mes.module.pur.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pur.domain.PurReceipt;
import com.carels.mes.module.pur.service.IPurReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 采购入库Controller
 */
@RestController
@RequestMapping("/mes/pur/receipt")
public class PurReceiptController extends BaseController {
    
    @Autowired
    private IPurReceiptService purReceiptService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              PurReceipt purReceipt) {
        startPage(pageNum, pageSize);
        List<PurReceipt> list = purReceiptService.selectPurReceiptList(purReceipt);
        return getDataTable(list);
    }
    
    @GetMapping("/{receiptId}")
    public AjaxResult getInfo(@PathVariable Long receiptId) {
        return AjaxResult.success(purReceiptService.selectPurReceiptById(receiptId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody PurReceipt purReceipt) {
        return toAjax(purReceiptService.insertPurReceipt(purReceipt));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody PurReceipt purReceipt) {
        return toAjax(purReceiptService.updatePurReceipt(purReceipt));
    }
    
    @DeleteMapping("/{receiptId}")
    public AjaxResult remove(@PathVariable Long receiptId) {
        return toAjax(purReceiptService.deletePurReceiptById(receiptId));
    }
}
