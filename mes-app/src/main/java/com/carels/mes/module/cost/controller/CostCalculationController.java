package com.carels.mes.module.cost.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.cost.domain.CostCalculation;
import com.carels.mes.module.cost.service.ICostCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 成本核算Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/cost/calculation")
public class CostCalculationController extends BaseController {

    @Autowired
    private ICostCalculationService calculationService;

    @GetMapping("/list")
    public TableDataInfo list(CostCalculation calculation) {
        startPage();
        List<CostCalculation> list = calculationService.selectCostCalculationList(calculation);
        return getDataTable(list);
    }

    @GetMapping("/{calcId}")
    public AjaxResult getInfo(@PathVariable Long calcId) {
        return AjaxResult.success(calculationService.selectCostCalculationById(calcId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody CostCalculation calculation) {
        return toAjax(calculationService.insertCostCalculation(calculation));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody CostCalculation calculation) {
        return toAjax(calculationService.updateCostCalculation(calculation));
    }

    @PutMapping("/{calcId}/calculate")
    public AjaxResult calculate(@PathVariable Long calcId) {
        return toAjax(calculationService.calculateCost(calcId));
    }

    @PutMapping("/{calcId}/carry-forward")
    public AjaxResult carryForward(@PathVariable Long calcId) {
        return toAjax(calculationService.carryForward(calcId));
    }

    @DeleteMapping("/{calcIds}")
    public AjaxResult remove(@PathVariable Long[] calcIds) {
        return toAjax(calculationService.deleteCostCalculationByIds(calcIds));
    }
}
