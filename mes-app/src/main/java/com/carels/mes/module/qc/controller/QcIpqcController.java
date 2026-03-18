package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.qc.domain.QcIpqc;
import com.carels.mes.module.qc.service.IQcIpqcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 过程检验(IPQC)Controller
 */
@RestController
@RequestMapping("/mes/qc/ipqc")
public class QcIpqcController extends BaseController {
    
    @Autowired
    private IQcIpqcService ipqcService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              QcIpqc ipqc) {
        startPage(pageNum, pageSize);
        List<QcIpqc> list = ipqcService.selectQcIpqcList(ipqc);
        return getDataTable(list);
    }
    
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(ipqcService.selectQcIpqcById(id));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody QcIpqc ipqc) {
        return toAjax(ipqcService.insertQcIpqc(ipqc));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody QcIpqc ipqc) {
        return toAjax(ipqcService.updateQcIpqc(ipqc));
    }
    
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(ipqcService.deleteQcIpqcById(id));
    }
    
    @PutMapping("/{id}/status")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String status) {
        ipqcService.updateStatus(id, status);
        return AjaxResult.success("状态更新成功");
    }
}
