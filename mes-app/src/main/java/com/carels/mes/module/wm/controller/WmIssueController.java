package com.carels.mes.module.wm.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.wm.domain.WmIssue;
import com.carels.mes.module.wm.service.IWmIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库管理Controller
 */
@RestController
@RequestMapping("/mes/wm/issue")
public class WmIssueController extends BaseController {

    @Autowired
    private IWmIssueService issueService;

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              WmIssue issue) {
        startPage(pageNum, pageSize);
        List<WmIssue> list = issueService.selectWmIssueList(issue);
        return getDataTable(list);
    }

    @GetMapping("/{issueId}")
    public AjaxResult getInfo(@PathVariable Long issueId) {
        return AjaxResult.success(issueService.selectWmIssueById(issueId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody WmIssue issue) {
        return toAjax(issueService.insertWmIssue(issue));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody WmIssue issue) {
        return toAjax(issueService.updateWmIssue(issue));
    }

    @DeleteMapping("/{issueId}")
    public AjaxResult remove(@PathVariable Long issueId) {
        return toAjax(issueService.deleteWmIssueById(issueId));
    }

    @PutMapping("/{issueId}/status")
    public AjaxResult updateStatus(@PathVariable Long issueId, @RequestParam String status) {
        issueService.updateStatus(issueId, status);
        return AjaxResult.success("状态更新成功");
    }

    @PostMapping("/{issueId}/confirm")
    public AjaxResult confirm(@PathVariable Long issueId) {
        issueService.confirmIssue(issueId);
        return AjaxResult.success("出库确认成功");
    }

    /**
     * 从销售订单生成出库单
     */
    @PostMapping("/fromSalesOrder")
    public AjaxResult createFromSalesOrder(@RequestParam Long salesOrderId, @RequestParam Long warehouseId) {
        WmIssue issue = issueService.createIssueFromSalesOrder(salesOrderId, warehouseId);
        return AjaxResult.success("出库单生成成功", issue);
    }
}
