package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdItem;
import com.carels.mes.module.md.service.IMdItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物料管理Controller - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@RestController
@RequestMapping("/mes/md/item")
public class MdItemController extends BaseController {
    
    @Autowired
    private IMdItemService mdItemService;
    
    /**
     * 查询物料列表
     */
    @GetMapping("/list")
    public TableDataInfo list(MdItem mdItem) {
        startPage();
        List<MdItem> list = mdItemService.selectMdItemList(mdItem);
        return getDataTable(list);
    }
    
    /**
     * 根据ID查询物料详情
     */
    @GetMapping("/{itemId}")
    public AjaxResult getInfo(@PathVariable Long itemId) {
        return AjaxResult.success(mdItemService.selectMdItemById(itemId));
    }
    
    /**
     * 根据编码查询物料
     */
    @GetMapping("/code/{itemCode}")
    public AjaxResult getByCode(@PathVariable String itemCode) {
        return AjaxResult.success(mdItemService.selectMdItemByCode(itemCode));
    }
    
    /**
     * 新增物料
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdItem mdItem) {
        if (!mdItemService.checkItemCodeUnique(mdItem.getItemCode())) {
            return AjaxResult.error("新增物料'" + mdItem.getItemCode() + "'失败，物料编码已存在");
        }
        return toAjax(mdItemService.insertMdItem(mdItem));
    }
    
    /**
     * 修改物料
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdItem mdItem) {
        return toAjax(mdItemService.updateMdItem(mdItem));
    }
    
    /**
     * 删除物料
     */
    @DeleteMapping("/{itemId}")
    public AjaxResult remove(@PathVariable Long itemId) {
        return toAjax(mdItemService.deleteMdItemById(itemId));
    }
    
    /**
     * 批量删除物料
     */
    @DeleteMapping("/batch/{itemIds}")
    public AjaxResult removeBatch(@PathVariable Long[] itemIds) {
        return toAjax(mdItemService.deleteMdItemByIds(itemIds));
    }
}
