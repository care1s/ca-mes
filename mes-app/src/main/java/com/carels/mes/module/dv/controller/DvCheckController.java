package com.carels.mes.module.dv.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.dv.domain.DvCheck;
import com.carels.mes.module.dv.service.IDvCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备点检Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/dv/check")
public class DvCheckController extends BaseController {

    @Autowired
    private IDvCheckService checkService;

    /**
     * 查询点检列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DvCheck check) {
        startPage();
        List<DvCheck> list = checkService.selectDvCheckList(check);
        return getDataTable(list);
    }

    /**
     * 获取点检详情
     */
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(checkService.selectDvCheckById(id));
    }

    /**
     * 新增点检
     */
    @PostMapping
    public AjaxResult add(@RequestBody DvCheck check) {
        return toAjax(checkService.insertDvCheck(check));
    }

    /**
     * 修改点检
     */
    @PutMapping
    public AjaxResult edit(@RequestBody DvCheck check) {
        return toAjax(checkService.updateDvCheck(check));
    }

    /**
     * 删除点检
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        int rows = 0;
        for (Long id : ids) {
            rows += checkService.deleteDvCheckById(id);
        }
        return toAjax(rows);
    }

    /**
     * 更新点检状态
     */
    @PutMapping("/{id}/status")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String status) {
        checkService.updateStatus(id, status);
        return AjaxResult.success();
    }
}
