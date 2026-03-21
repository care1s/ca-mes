package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.qc.domain.QcRecord;
import com.carels.mes.module.qc.service.IQcRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检验记录Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mes/qc/record")
public class QcRecordController extends BaseController {

    @Autowired
    private IQcRecordService qcRecordService;

    /**
     * 查询检验记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(QcRecord record) {
        startPage();
        List<QcRecord> list = qcRecordService.selectQcRecordList(record);
        return getDataTable(list);
    }

    /**
     * 根据ID查询检验记录详情
     */
    @GetMapping("/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId) {
        return AjaxResult.success(qcRecordService.selectQcRecordById(recordId));
    }

    /**
     * 新增检验记录
     */
    @PostMapping
    public AjaxResult add(@RequestBody QcRecord record) {
        return toAjax(qcRecordService.insertQcRecord(record));
    }

    /**
     * 修改检验记录
     */
    @PutMapping
    public AjaxResult edit(@RequestBody QcRecord record) {
        return toAjax(qcRecordService.updateQcRecord(record));
    }

    /**
     * 删除检验记录
     */
    @DeleteMapping("/{recordId}")
    public AjaxResult remove(@PathVariable Long recordId) {
        return toAjax(qcRecordService.deleteQcRecordById(recordId));
    }

    /**
     * 开始检验
     */
    @PutMapping("/start/{recordId}")
    public AjaxResult start(@PathVariable Long recordId) {
        return toAjax(qcRecordService.startInspect(recordId));
    }

    /**
     * 提交检验结果
     */
    @PostMapping("/result")
    public AjaxResult submitResult(@RequestBody java.util.Map<String, Object> params) {
        Long recordId = Long.valueOf(params.get("recordId").toString());
        Double qualifiedQty = Double.valueOf(params.get("qualifiedQty").toString());
        Double defectiveQty = params.get("defectiveQty") != null ? Double.valueOf(params.get("defectiveQty").toString()) : 0.0;
        String defectDesc = (String) params.get("defectDesc");
        String defectReason = (String) params.get("defectReason");
        String handleMethod = (String) params.get("handleMethod");

        return AjaxResult.success(qcRecordService.submitResult(recordId, qualifiedQty, defectiveQty,
                defectDesc, defectReason, handleMethod));
    }
}
