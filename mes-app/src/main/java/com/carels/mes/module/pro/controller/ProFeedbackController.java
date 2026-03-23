package com.carels.mes.module.pro.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pro.domain.ProFeedback;
import com.carels.mes.module.pro.service.IProFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产报工Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mes/pro/feedback")
public class ProFeedbackController extends BaseController {

    @Autowired
    private IProFeedbackService feedbackService;

    /**
     * 查询报工列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ProFeedback feedback) {
        startPage();
        List<ProFeedback> list = feedbackService.selectProFeedbackList(feedback);
        return getDataTable(list);
    }

    /**
     * 根据ID查询报工详情
     */
    @GetMapping("/{feedbackId}")
    public AjaxResult getInfo(@PathVariable Long feedbackId) {
        return AjaxResult.success(feedbackService.selectProFeedbackById(feedbackId));
    }

    /**
     * 新增报工
     */
    @PostMapping
    public AjaxResult add(@RequestBody ProFeedback feedback) {
        return toAjax(feedbackService.insertProFeedback(feedback));
    }

    /**
     * 修改报工
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ProFeedback feedback) {
        return toAjax(feedbackService.updateProFeedback(feedback));
    }

    /**
     * 删除报工
     */
    @DeleteMapping("/{feedbackId}")
    public AjaxResult remove(@PathVariable Long feedbackId) {
        return toAjax(feedbackService.deleteProFeedbackById(feedbackId));
    }

    /**
     * 批量删除报工
     */
    @DeleteMapping("/batch/{feedbackIds}")
    public AjaxResult removeBatch(@PathVariable Long[] feedbackIds) {
        return toAjax(feedbackService.deleteProFeedbackByIds(feedbackIds));
    }

    /**
     * 审核报工
     */
    @PutMapping("/approve/{feedbackId}")
    public AjaxResult approve(@PathVariable Long feedbackId, @RequestParam String status) {
        return toAjax(feedbackService.approveFeedback(feedbackId, status));
    }
}
