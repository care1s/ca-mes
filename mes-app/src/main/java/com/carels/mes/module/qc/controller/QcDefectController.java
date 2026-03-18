package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.qc.domain.QcDefect;
import com.carels.mes.module.qc.service.IQcDefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mes/qc/defect")
public class QcDefectController extends BaseController {
    
    @Autowired
    private IQcDefectService defectService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              QcDefect defect) {
        startPage(pageNum, pageSize);
        List<QcDefect> list = defectService.selectQcDefectList(defect);
        return getDataTable(list);
    }
    
    @GetMapping("/{defectId}")
    public AjaxResult getInfo(@PathVariable Long defectId) {
        return AjaxResult.success(defectService.selectQcDefectById(defectId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody QcDefect defect) {
        return toAjax(defectService.insertQcDefect(defect));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody QcDefect defect) {
        return toAjax(defectService.updateQcDefect(defect));
    }
    
    @DeleteMapping("/{defectId}")
    public AjaxResult remove(@PathVariable Long defectId) {
        return toAjax(defectService.deleteQcDefectById(defectId));
    }
    
    @PutMapping("/{defectId}/status")
    public AjaxResult updateStatus(@PathVariable Long defectId, @RequestParam Integer status) {
        defectService.updateStatus(defectId, status);
        return AjaxResult.success("状态更新成功");
    }
}
