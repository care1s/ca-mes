package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmTransfer;
import com.carels.mes.module.wm.service.IWmTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存调拨Controller
 */
@RestController
@RequestMapping("/mes/wm/transfer")
public class WmTransferController extends BaseController {
    
    @Autowired
    private IWmTransferService transferService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmTransfer transfer) {
        startPage(pageNum, pageSize);
        List<WmTransfer> list = transferService.selectWmTransferList(transfer);
        return getDataTable(list);
    }
    
    @GetMapping("/{transferId}")
    public AjaxResult getInfo(@PathVariable Long transferId) {
        return AjaxResult.success(transferService.selectWmTransferById(transferId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody WmTransfer transfer) {
        return toAjax(transferService.insertWmTransfer(transfer));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody WmTransfer transfer) {
        return toAjax(transferService.updateWmTransfer(transfer));
    }
    
    @DeleteMapping("/{transferId}")
    public AjaxResult remove(@PathVariable Long transferId) {
        return toAjax(transferService.deleteWmTransferById(transferId));
    }
    
    @PutMapping("/{transferId}/submit")
    public AjaxResult submit(@PathVariable Long transferId) {
        transferService.submitTransfer(transferId);
        return AjaxResult.success("提交成功");
    }
    
    @PutMapping("/{transferId}/out")
    public AjaxResult outConfirm(@PathVariable Long transferId) {
        transferService.outConfirm(transferId);
        return AjaxResult.success("出库确认成功");
    }
    
    @PutMapping("/{transferId}/in")
    public AjaxResult inConfirm(@PathVariable Long transferId) {
        transferService.inConfirm(transferId);
        return AjaxResult.success("入库确认成功");
    }
    
    @PutMapping("/{transferId}/cancel")
    public AjaxResult cancel(@PathVariable Long transferId) {
        transferService.cancelTransfer(transferId);
        return AjaxResult.success("调拨已取消");
    }
}
