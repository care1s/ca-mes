package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmStock;
import com.carels.mes.module.wm.service.IWmStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mes/wm/stock")
public class WmStockController extends BaseController {
    
    @Autowired
    private IWmStockService stockService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmStock stock) {
        startPage(pageNum, pageSize);
        List<WmStock> list = stockService.selectWmStockList(stock);
        return getDataTable(list);
    }
    
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(stockService.selectWmStockById(id));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmStock stock) {
        return toAjax(stockService.insertWmStock(stock));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmStock stock) {
        return toAjax(stockService.updateWmStock(stock));
    }
    
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(stockService.deleteWmStockById(id));
    }
}
