package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.qc.domain.QcOqc;
import com.carels.mes.module.qc.service.IQcOqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mes/qc/oqc")
public class QcOqcController extends BaseController {
    
    @Autowired
    private IQcOqcService oqcService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              QcOqc oqc) {
        startPage(pageNum, pageSize);
        List<QcOqc> list = oqcService.selectQcOqcList(oqc);
        return getDataTable(list);
    }
    
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(oqcService.selectQcOqcById(id));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody QcOqc oqc) {
        return toAjax(oqcService.insertQcOqc(oqc));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody QcOqc oqc) {
        return toAjax(oqcService.updateQcOqc(oqc));
    }
    
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(oqcService.deleteQcOqcById(id));
    }
    
    @PutMapping("/{id}/status")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String status) {
        oqcService.updateStatus(id, status);
        return AjaxResult.success("状态更新成功");
    }
}
