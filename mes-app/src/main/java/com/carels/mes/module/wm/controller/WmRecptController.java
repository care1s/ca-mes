package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmRecpt;
import com.carels.mes.module.wm.service.IWmRecptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库管理Controller
 */
@RestController
@RequestMapping("/mes/wm/recpt")
public class WmRecptController extends BaseController {

    @Autowired
    private IWmRecptService recptService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmRecpt recpt) {
        startPage(pageNum, pageSize);
        List<WmRecpt> list = recptService.selectWmRecptList(recpt);
        return getDataTable(list);
    }

    @GetMapping("/{recptId}")
    public AjaxResult getInfo(@PathVariable Long recptId) {
        return AjaxResult.success(recptService.selectWmRecptById(recptId));
    }

    @DeleteMapping("/{recptId}")
    public AjaxResult remove(@PathVariable Long recptId) {
        return toAjax(recptService.deleteWmRecptById(recptId));
    }

    @PutMapping("/{recptId}/status")
    public AjaxResult updateStatus(@PathVariable Long recptId, @RequestParam String status) {
        recptService.updateStatus(recptId, status);
        return AjaxResult.success("状态更新成功");
    }

    @PostMapping("/{recptId}/confirm")
    public AjaxResult confirm(@PathVariable Long recptId) {
        recptService.confirmRecpt(recptId);
        return AjaxResult.success("入库确认成功");
    }
}
