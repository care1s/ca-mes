package com.carels.mes.module.qc.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.qc.domain.QcRecord;
import com.carels.mes.module.qc.service.IQcRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 质量检验移动端Controller - carels
 * 移动检验功能
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/mobile/qc")
public class QcRecordMobController {

    @Autowired
    private IQcRecordService qcRecordService;

    /**
     * 扫码查询检验任务
     * 支持扫描：工单码、物料码
     */
    @GetMapping("/scan")
    public AjaxResult scan(@RequestParam String barcode,
                          @RequestParam(required = false, defaultValue = "IQC") String qcType) {
        Map<String, Object> task = qcRecordService.scanQcTask(barcode, qcType);
        if (task == null) {
            return AjaxResult.error("未找到对应的检验任务，请检查条码是否正确");
        }
        return AjaxResult.success(task);
    }

    /**
     * 获取待检验列表
     */
    @GetMapping("/pending")
    public AjaxResult pending(@RequestParam(required = false, defaultValue = "IQC") String qcType,
                             @RequestParam(required = false) Long inspectorId) {
        List<Map<String, Object>> list = qcRecordService.getPendingQcList(qcType, inspectorId);
        return AjaxResult.success(list);
    }

    /**
     * 获取我的检验列表
     */
    @GetMapping("/my")
    public AjaxResult myQcList(@RequestParam Long inspectorId) {
        List<Map<String, Object>> list = qcRecordService.getMyQcList(inspectorId);
        return AjaxResult.success(list);
    }

    /**
     * 扫码快捷报检 - 核心加强功能
     * 扫码 → 自动带出工单信息 → 输入报检数量 → 提交报检
     */
    @PostMapping("/inspect/scan")
    public AjaxResult scanInspect(@RequestBody Map<String, Object> params) {
        try {
            String barcode = (String) params.get("barcode");
            String qcType = params.get("qcType") != null ? params.get("qcType").toString() : "IQC";
            Double quantity = Double.valueOf(params.get("quantity").toString());
            Long inspectorId = Long.valueOf(params.get("inspectorId").toString());
            String inspectorName = (String) params.get("inspectorName");

            // 1. 扫码查询任务
            Map<String, Object> task = qcRecordService.scanQcTask(barcode, qcType);
            if (task == null) {
                return AjaxResult.error("未找到对应的检验任务");
            }

            // 2. 创建检验记录
            QcRecord record = new QcRecord();
            record.setQcType(qcType);
            record.setWorkorderId(task.get("workorderId") != null ? Long.valueOf(task.get("workorderId").toString()) : null);
            record.setWorkorderCode((String) task.get("workorderCode"));
            record.setItemId(task.get("itemId") != null ? Long.valueOf(task.get("itemId").toString()) : null);
            record.setItemCode((String) task.get("itemCode"));
            record.setItemName((String) task.get("itemName"));
            record.setSpecification((String) task.get("specification"));
            record.setSupplierId(task.get("supplierId") != null ? Long.valueOf(task.get("supplierId").toString()) : null);
            record.setSupplierName((String) task.get("supplierName"));
            record.setInspectQuantity(quantity);
            record.setInspectorId(inspectorId);
            record.setInspectorName(inspectorName);
            record.setCreateBy(inspectorName);

            qcRecordService.insertQcRecord(record);

            return AjaxResult.success("报检成功", record);
        } catch (Exception e) {
            return AjaxResult.error("报检失败: " + e.getMessage());
        }
    }

    /**
     * 开始检验
     */
    @PutMapping("/start/{recordId}")
    public AjaxResult startInspect(@PathVariable Long recordId) {
        return AjaxResult.success(qcRecordService.startInspect(recordId));
    }

    /**
     * 提交检验结果 - 核心功能
     */
    @PostMapping("/result")
    public AjaxResult submitResult(@RequestBody Map<String, Object> params) {
        try {
            Long recordId = Long.valueOf(params.get("recordId").toString());
            Double qualifiedQty = Double.valueOf(params.get("qualifiedQty").toString());
            Double defectiveQty = params.get("defectiveQty") != null ? Double.valueOf(params.get("defectiveQty").toString()) : 0.0;
            String defectDesc = (String) params.get("defectDesc");
            String defectReason = (String) params.get("defectReason");
            String handleMethod = (String) params.get("handleMethod");

            Map<String, Object> result = qcRecordService.submitResult(recordId, qualifiedQty, defectiveQty,
                    defectDesc, defectReason, handleMethod);

            return AjaxResult.success("检验结果提交成功", result);
        } catch (Exception e) {
            return AjaxResult.error("检验结果提交失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询检验记录详情
     */
    @GetMapping("/{recordId}")
    public AjaxResult getInfo(@PathVariable Long recordId) {
        return AjaxResult.success(qcRecordService.selectQcRecordById(recordId));
    }
}
