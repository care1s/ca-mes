package com.carels.mes.module.pur.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.pur.domain.PurReceipt;
import com.carels.mes.module.pur.domain.PurReceiptItem;
import com.carels.mes.module.pur.service.IPurReceiptService;
import com.carels.mes.module.wm.domain.WmRecpt;
import com.carels.mes.module.wm.domain.WmRecptItem;
import com.carels.mes.module.wm.service.IWmRecptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 采购入库Controller - 统一走入库管理流程
 */
@RestController
@RequestMapping("/mes/pur/receipt")
public class PurReceiptController extends BaseController {

    @Autowired
    private IPurReceiptService purReceiptService;

    @Autowired
    private IWmRecptService wmRecptService;

    /**
     * 将PurReceipt转换为WmRecpt
     */
    private WmRecpt convertToWmRecpt(PurReceipt purReceipt) {
        WmRecpt recpt = new WmRecpt();
        // 基础字段
        recpt.setId(purReceipt.getId());
        recpt.setRecptNo(purReceipt.getReceiptNo());
        recpt.setRecptType("PURCHASE");
        recpt.setSourceType("PUR_ORDER");
        recpt.setSourceId(purReceipt.getOrderId());
        recpt.setSourceNo(purReceipt.getOrderNo());
        recpt.setWarehouseId(purReceipt.getWarehouseId());
        recpt.setWarehouseName(purReceipt.getWarehouseName());
        recpt.setStatus(purReceipt.getStatus());
        recpt.setRecptDate(purReceipt.getReceiptDate());
        recpt.setRemark(purReceipt.getRemark());

        // 采购特有字段
        recpt.setVendorId(purReceipt.getVendorId());
        recpt.setVendorCode(purReceipt.getVendorCode());
        recpt.setVendorName(purReceipt.getVendorName());
        recpt.setOrderId(purReceipt.getOrderId());
        recpt.setOrderNo(purReceipt.getOrderNo());
        recpt.setTotalAmount(purReceipt.getTotalAmount());
        recpt.setTotalQuantity(purReceipt.getTotalQuantity());
        recpt.setQualifiedQty(purReceipt.getQualifiedQty());
        recpt.setUnqualifiedQty(purReceipt.getUnqualifiedQty());
        recpt.setInspectorId(purReceipt.getInspectorId());
        recpt.setInspectorName(purReceipt.getInspectorName());
        recpt.setInspectionDate(purReceipt.getInspectionDate());
        recpt.setInspectionRemark(purReceipt.getInspectionRemark());
        recpt.setAuditStatus(purReceipt.getAuditStatus());

        // 转换明细
        if (purReceipt.getItems() != null && !purReceipt.getItems().isEmpty()) {
            List<WmRecptItem> items = new ArrayList<>();
            for (PurReceiptItem purItem : purReceipt.getItems()) {
                WmRecptItem item = new WmRecptItem();
                item.setItemId(purItem.getItemId());
                item.setItemId2(purItem.getItemId()); // 物料ID
                item.setItemCode(purItem.getItemCode());
                item.setItemName(purItem.getItemName());
                item.setBatchCode(purItem.getBatchNo());
                item.setQuantity(purItem.getQuantity());
                item.setUnit(purItem.getUnit());
                item.setRemark(purItem.getRemark());
                items.add(item);
            }
            recpt.setItems(items);
        }

        return recpt;
    }

    /**
     * 将WmRecpt转换为PurReceipt（用于返回给前端）
     */
    private PurReceipt convertToPurReceipt(WmRecpt recpt) {
        PurReceipt purReceipt = new PurReceipt();
        // 基础字段
        purReceipt.setId(recpt.getId());
        purReceipt.setReceiptNo(recpt.getRecptNo());
        purReceipt.setOrderId(recpt.getOrderId());
        purReceipt.setOrderNo(recpt.getOrderNo());
        purReceipt.setVendorId(recpt.getVendorId());
        purReceipt.setVendorCode(recpt.getVendorCode());
        purReceipt.setVendorName(recpt.getVendorName());
        purReceipt.setReceiptDate(recpt.getRecptDate());
        purReceipt.setTotalAmount(recpt.getTotalAmount());
        purReceipt.setTotalQuantity(recpt.getTotalQuantity());
        purReceipt.setQualifiedQty(recpt.getQualifiedQty());
        purReceipt.setUnqualifiedQty(recpt.getUnqualifiedQty());
        purReceipt.setWarehouseId(recpt.getWarehouseId());
        purReceipt.setWarehouseName(recpt.getWarehouseName());
        purReceipt.setStatus(recpt.getStatus());
        purReceipt.setAuditStatus(recpt.getAuditStatus());
        purReceipt.setInspectorId(recpt.getInspectorId());
        purReceipt.setInspectorName(recpt.getInspectorName());
        purReceipt.setInspectionDate(recpt.getInspectionDate());
        purReceipt.setInspectionRemark(recpt.getInspectionRemark());
        purReceipt.setRemark(recpt.getRemark());
        purReceipt.setCreateBy(recpt.getCreateBy());
        purReceipt.setCreateTime(recpt.getCreateTime());
        purReceipt.setUpdateBy(recpt.getUpdateBy());
        purReceipt.setUpdateTime(recpt.getUpdateTime());

        return purReceipt;
    }

    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              PurReceipt purReceipt) {
        startPage(pageNum, pageSize);
        // 查询采购类型的入库单
        WmRecpt query = new WmRecpt();
        query.setRecptType("PURCHASE");
        query.setVendorName(purReceipt.getVendorName());
        query.setStatus(purReceipt.getStatus());
        query.setSourceNo(purReceipt.getOrderNo()); // 用sourceNo查订单号

        List<WmRecpt> list = wmRecptService.selectWmRecptList(query);
        List<PurReceipt> result = new ArrayList<>();
        for (WmRecpt recpt : list) {
            result.add(convertToPurReceipt(recpt));
        }
        return getDataTable(result);
    }

    @GetMapping("/{receiptId}")
    public AjaxResult getInfo(@PathVariable Long receiptId) {
        WmRecpt recpt = wmRecptService.selectWmRecptById(receiptId);
        if (recpt != null && !"PURCHASE".equals(recpt.getRecptType())) {
            return AjaxResult.error("该入库单不是采购入库");
        }
        return AjaxResult.success(convertToPurReceipt(recpt));
    }

    @DeleteMapping("/{receiptId}")
    public AjaxResult remove(@PathVariable Long receiptId) {
        return toAjax(wmRecptService.deleteWmRecptById(receiptId));
    }

    /**
     * 确认入库 - 会更新库存
     */
    @PostMapping("/{receiptId}/confirm")
    public AjaxResult confirm(@PathVariable Long receiptId) {
        wmRecptService.confirmRecpt(receiptId);
        return AjaxResult.success("入库确认成功");
    }
}
