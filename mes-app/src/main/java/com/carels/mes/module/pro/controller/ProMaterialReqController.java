package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProMaterialReq;
import com.carels.mes.module.pro.service.IProMaterialReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料需求Controller
 */
@RestController
@RequestMapping("/mes/pro/materialReq")
public class ProMaterialReqController extends BaseController {
    
    @Autowired
    private IProMaterialReqService reqService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              ProMaterialReq req) {
        startPage(pageNum, pageSize);
        List<ProMaterialReq> list = reqService.selectProMaterialReqList(req);
        return getDataTable(list);
    }
    
    @GetMapping("/{reqId}")
    public AjaxResult getInfo(@PathVariable Long reqId) {
        return AjaxResult.success(reqService.selectProMaterialReqById(reqId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody ProMaterialReq req) {
        return toAjax(reqService.insertProMaterialReq(req));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody ProMaterialReq req) {
        return toAjax(reqService.updateProMaterialReq(req));
    }
    
    @DeleteMapping("/{reqId}")
    public AjaxResult remove(@PathVariable Long reqId) {
        return toAjax(reqService.deleteProMaterialReqById(reqId));
    }
    
    @PutMapping("/{reqId}/submit")
    public AjaxResult submit(@PathVariable Long reqId) {
        reqService.submitReq(reqId);
        return AjaxResult.success("提交成功");
    }
    
    @PutMapping("/{reqId}/approve")
    public AjaxResult approve(@PathVariable Long reqId) {
        reqService.approveReq(reqId);
        return AjaxResult.success("审核通过");
    }
    
    @PutMapping("/{reqId}/issue")
    public AjaxResult issue(@PathVariable Long reqId) {
        reqService.issueMaterial(reqId);
        return AjaxResult.success("发料完成");
    }
    
    @PutMapping("/{reqId}/complete")
    public AjaxResult complete(@PathVariable Long reqId) {
        reqService.completeReq(reqId);
        return AjaxResult.success("需求完成");
    }
    
    @PutMapping("/{reqId}/cancel")
    public AjaxResult cancel(@PathVariable Long reqId) {
        reqService.cancelReq(reqId);
        return AjaxResult.success("需求已取消");
    }
}
