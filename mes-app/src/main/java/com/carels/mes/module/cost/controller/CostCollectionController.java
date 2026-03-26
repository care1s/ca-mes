package com.carels.mes.module.cost.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.cost.domain.CostCollection;
import com.carels.mes.module.cost.service.ICostCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成本归集Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/cost/collection")
public class CostCollectionController extends BaseController {

    @Autowired
    private ICostCollectionService collectionService;

    @GetMapping("/list")
    public TableDataInfo list(CostCollection collection) {
        startPage();
        List<CostCollection> list = collectionService.selectCostCollectionList(collection);
        return getDataTable(list);
    }

    @GetMapping("/{collectionId}")
    public AjaxResult getInfo(@PathVariable Long collectionId) {
        return AjaxResult.success(collectionService.selectCostCollectionById(collectionId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody CostCollection collection) {
        return toAjax(collectionService.insertCostCollection(collection));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody CostCollection collection) {
        return toAjax(collectionService.updateCostCollection(collection));
    }

    @PutMapping("/{collectionId}/confirm")
    public AjaxResult confirm(@PathVariable Long collectionId) {
        return toAjax(collectionService.confirmCollection(collectionId));
    }

    @DeleteMapping("/{collectionIds}")
    public AjaxResult remove(@PathVariable Long[] collectionIds) {
        return toAjax(collectionService.deleteCostCollectionByIds(collectionIds));
    }

    @GetMapping("/sum-by-workorder")
    public AjaxResult sumByWorkorder(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Map<String, Object>> result = collectionService.sumCostByWorkorder(startDate, endDate);
        return AjaxResult.success(result);
    }
}
