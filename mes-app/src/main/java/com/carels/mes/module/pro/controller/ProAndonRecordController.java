package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.pro.domain.ProAndonRecord;
import com.carels.mes.module.pro.service.IProAndonRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 安东异常记录Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/pro/andon/record")
public class ProAndonRecordController extends BaseController {

    @Autowired
    private IProAndonRecordService andonRecordService;

    @GetMapping("/list")
    public TableDataInfo list(ProAndonRecord record) {
        startPage();
        List<ProAndonRecord> list = andonRecordService.selectProAndonRecordList(record);
        return getDataTable(list);
    }

    @GetMapping("/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId) {
        return AjaxResult.success(andonRecordService.selectProAndonRecordById(recordId));
    }

    @PostMapping("/report")
    public AjaxResult report(@RequestBody ProAndonRecord record) {
        return toAjax(andonRecordService.reportAndon(record));
    }

    @PutMapping("/{recordId}/accept")
    public AjaxResult accept(@PathVariable Long recordId, @RequestParam Long handlerId, @RequestParam String handlerName) {
        return toAjax(andonRecordService.acceptAndon(recordId, handlerId, handlerName));
    }

    @PutMapping("/{recordId}/handle")
    public AjaxResult handle(@PathVariable Long recordId, @RequestParam String handleResult) {
        return toAjax(andonRecordService.handleAndon(recordId, handleResult));
    }

    @PutMapping("/{recordId}/complete")
    public AjaxResult complete(@PathVariable Long recordId) {
        return toAjax(andonRecordService.completeAndon(recordId));
    }

    @PutMapping("/{recordId}/close")
    public AjaxResult close(@PathVariable Long recordId, @RequestParam String closeReason) {
        return toAjax(andonRecordService.closeAndon(recordId, closeReason));
    }

    @PutMapping("/{recordId}/escalate")
    public AjaxResult escalate(@PathVariable Long recordId, @RequestParam Integer escalateLevel) {
        return toAjax(andonRecordService.escalateAndon(recordId, escalateLevel));
    }

    @GetMapping("/timeout")
    public AjaxResult timeoutList() {
        return AjaxResult.success(andonRecordService.selectTimeoutRecords());
    }

    @PostMapping("/auto-escalate")
    public AjaxResult autoEscalate() {
        andonRecordService.autoEscalateTimeoutRecords();
        return AjaxResult.success();
    }
}
