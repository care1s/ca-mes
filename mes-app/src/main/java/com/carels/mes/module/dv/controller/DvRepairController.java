package com.carels.mes.module.dv.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.dv.domain.DvRepair;
import com.carels.mes.module.dv.service.IDvRepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备维修Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/dv/repair")
public class DvRepairController extends BaseController {

    @Autowired
    private IDvRepairService repairService;

    /**
     * 查询维修列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DvRepair repair) {
        startPage();
        List<DvRepair> list = repairService.selectDvRepairList(repair);
        return getDataTable(list);
    }

    /**
     * 获取维修详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(repairService.selectDvRepairById(id));
    }

    /**
     * 新增维修申请
     */
    @PostMapping
    public AjaxResult add(@RequestBody DvRepair repair) {
        return toAjax(repairService.insertDvRepair(repair));
    }

    /**
     * 修改维修
     */
    @PutMapping
    public AjaxResult edit(@RequestBody DvRepair repair) {
        return toAjax(repairService.updateDvRepair(repair));
    }

    /**
     * 删除维修
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += repairService.deleteDvRepairById(id);
        }
        return toAjax(rows);
    }

    /**
     * 派工
     */
    @PutMapping("/{id}/assign")
    public AjaxResult assign(@PathVariable Long id, @RequestParam Long repairmanId, @RequestParam String repairmanName) {
        repairService.assignRepair(id, repairmanId, repairmanName);
        return AjaxResult.success();
    }

    /**
     * 完成维修
     */
    @PutMapping("/{id}/complete")
    public AjaxResult complete(@PathVariable Long id, @RequestParam String repairContent, @RequestParam BigDecimal repairCost) {
        repairService.completeRepair(id, repairContent, repairCost);
        return AjaxResult.success();
    }

    /**
     * 更新维修状态
     */
    @PutMapping("/{id}/status")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String status) {
        repairService.updateStatus(id, status);
        return AjaxResult.success();
    }
}
