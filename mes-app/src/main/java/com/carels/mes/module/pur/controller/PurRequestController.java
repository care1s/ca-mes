package com.carels.mes.module.pur.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pur.domain.PurRequest;
import com.carels.mes.module.pur.service.IPurRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 采购申请Controller
 */
@RestController
@RequestMapping("/mes/pur/request")
public class PurRequestController extends BaseController {
    
    @Autowired
    private IPurRequestService purRequestService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              PurRequest purRequest) {
        startPage(pageNum, pageSize);
        List<PurRequest> list = purRequestService.selectPurRequestList(purRequest);
        return getDataTable(list);
    }
    
    @GetMapping("/{requestId}")
    public AjaxResult getInfo(@PathVariable Long requestId) {
        return AjaxResult.success(purRequestService.selectPurRequestById(requestId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody PurRequest purRequest) {
        return toAjax(purRequestService.insertPurRequest(purRequest));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody PurRequest purRequest) {
        return toAjax(purRequestService.updatePurRequest(purRequest));
    }
    
    @DeleteMapping("/{requestId}")
    public AjaxResult remove(@PathVariable Long requestId) {
        return toAjax(purRequestService.deletePurRequestById(requestId));
    }

    /**
     * 提交审批
     */
    @PutMapping("/{requestId}/submit")
    public AjaxResult submit(@PathVariable Long requestId) {
        return toAjax(purRequestService.submitPurRequest(requestId));
    }

    /**
     * 审批通过
     */
    @PutMapping("/{requestId}/approve")
    public AjaxResult approve(@PathVariable Long requestId, @RequestParam(required = false) String auditOpinion) {
        return toAjax(purRequestService.approvePurRequest(requestId, auditOpinion));
    }

    /**
     * 审批拒绝
     */
    @PutMapping("/{requestId}/reject")
    public AjaxResult reject(@PathVariable Long requestId, @RequestParam(required = false) String auditOpinion) {
        return toAjax(purRequestService.rejectPurRequest(requestId, auditOpinion));
    }
}
