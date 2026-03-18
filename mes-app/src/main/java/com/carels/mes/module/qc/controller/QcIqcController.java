package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.qc.domain.QcIqc;
import com.carels.mes.module.qc.service.IQcIqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 来料检验(IQC)Controller
 */
@RestController
@RequestMapping("/mes/qc/iqc")
public class QcIqcController extends BaseController {
    
    @Autowired
    private IQcIqcService iqcService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              QcIqc iqc) {
        startPage(pageNum, pageSize);
        List<QcIqc> list = iqcService.selectQcIqcList(iqc);
        return getDataTable(list);
    }
    
    @GetMapping("/{iqcId}")
    public AjaxResult getInfo(@PathVariable Long iqcId) {
        return AjaxResult.success(iqcService.selectQcIqcById(iqcId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody QcIqc iqc) {
        return toAjax(iqcService.insertQcIqc(iqc));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody QcIqc iqc) {
        return toAjax(iqcService.updateQcIqc(iqc));
    }
    
    @DeleteMapping("/{iqcId}")
    public AjaxResult remove(@PathVariable Long iqcId) {
        return toAjax(iqcService.deleteQcIqcById(iqcId));
    }
    
    @PutMapping("/{iqcId}/status")
    public AjaxResult updateStatus(@PathVariable Long iqcId, @RequestParam String status) {
        iqcService.updateStatus(iqcId, status);
        return AjaxResult.success("状态更新成功");
    }
}
