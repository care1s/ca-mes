package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.module.md.domain.MdItem;
import com.carels.mes.module.md.service.IMdItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料管理移动端Controller - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mobile/md/item")
public class MdItemMobController {
    
    @Autowired
    private IMdItemService mdItemService;
    
    /**
     * 扫码查询物料（简化版）
     */
    @GetMapping("/scan/{barcode}")
    public AjaxResult scan(@PathVariable String barcode) {
        // 从条码解析物料编码
        String itemCode = parseBarcode(barcode);
        MdItem item = mdItemService.selectMdItemByCode(itemCode);
        if (item == null) {
            return AjaxResult.error("未找到物料信息");
        }
        return AjaxResult.success(item);
    }
    
    /**
     * 快速查询物料列表（移动端精简）
     */
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) String keyword) {
        MdItem mdItem = new MdItem();
        mdItem.setItemName(keyword);
        mdItem.setItemCode(keyword);
        List<MdItem> list = mdItemService.selectMdItemList(mdItem);
        return AjaxResult.success(list);
    }
    
    /**
     * 解析条码
     */
    private String parseBarcode(String barcode) {
        // 条码格式: MES-ITEM-XXX
        if (barcode.startsWith("MES-ITEM-")) {
            return barcode.substring(9);
        }
        return barcode;
    }
}
