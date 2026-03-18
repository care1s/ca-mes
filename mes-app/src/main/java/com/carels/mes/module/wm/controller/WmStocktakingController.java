package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmStocktaking;
import com.carels.mes.module.wm.service.IWmStocktakingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存盘点Controller
 */
@RestController
@RequestMapping("/mes/wm/stocktaking")
public class WmStocktakingController extends BaseController {
    
    @Autowired
    private IWmStocktakingService stocktakingService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmStocktaking stocktaking) {
        startPage(pageNum, pageSize);
        List<WmStocktaking> list = stocktakingService.selectWmStocktakingList(stocktaking);
        return getDataTable(list);
    }
    
    @GetMapping("/{stocktakingId}")
    public AjaxResult getInfo(@PathVariable Long stocktakingId) {
        return AjaxResult.success(stocktakingService.selectWmStocktakingById(stocktakingId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmStocktaking stocktaking) {
        return toAjax(stocktakingService.insertWmStocktaking(stocktaking));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmStocktaking stocktaking) {
        return toAjax(stocktakingService.updateWmStocktaking(stocktaking));
    }
    
    @DeleteMapping("/{stocktakingId}")
    public AjaxResult remove(@PathVariable Long stocktakingId) {
        return toAjax(stocktakingService.deleteWmStocktakingById(stocktakingId));
    }
    
    @PutMapping("/{stocktakingId}/start")
    public AjaxResult start(@PathVariable Long stocktakingId) {
        stocktakingService.startStocktaking(stocktakingId);
        return AjaxResult.success("盘点开始成功");
    }
    
    @PutMapping("/{stocktakingId}/complete")
    public AjaxResult complete(@PathVariable Long stocktakingId) {
        stocktakingService.completeStocktaking(stocktakingId);
        return AjaxResult.success("盘点完成");
    }
    
    @PutMapping("/{stocktakingId}/cancel")
    public AjaxResult cancel(@PathVariable Long stocktakingId) {
        stocktakingService.cancelStocktaking(stocktakingId);
        return AjaxResult.success("盘点已取消");
    }
}
