package com.carels.mes.module.cost.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.cost.domain.CostItem;
import com.carels.mes.module.cost.service.ICostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成本项目Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/cost/item")
public class CostItemController extends BaseController {

    @Autowired
    private ICostItemService costItemService;

    @GetMapping("/list")
    public TableDataInfo list(CostItem costItem) {
        startPage();
        List<CostItem> list = costItemService.selectCostItemList(costItem);
        return getDataTable(list);
    }

    @GetMapping("/{itemId}")
    public AjaxResult getInfo(@PathVariable Long itemId) {
        return AjaxResult.success(costItemService.selectCostItemById(itemId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody CostItem costItem) {
        return toAjax(costItemService.insertCostItem(costItem));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody CostItem costItem) {
        return toAjax(costItemService.updateCostItem(costItem));
    }

    @DeleteMapping("/{itemIds}")
    public AjaxResult remove(@PathVariable Long[] itemIds) {
        return toAjax(costItemService.deleteCostItemByIds(itemIds));
    }
}
