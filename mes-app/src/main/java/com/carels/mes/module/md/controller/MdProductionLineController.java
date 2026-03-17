package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdProductionLine;
import com.carels.mes.module.md.service.IMdProductionLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 生产线管理Controller - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/mes/md/productionLine")
public class MdProductionLineController extends BaseController {
    
    @Autowired
    private IMdProductionLineService mdProductionLineService;
    
    /**
     * 查询生产线列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              MdProductionLine mdProductionLine) {
        startPage(pageNum, pageSize);
        List<MdProductionLine> list = mdProductionLineService.selectMdProductionLineList(mdProductionLine);
        return getDataTable(list);
    }
    
    /**
     * 查询所有启用的生产线（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        return AjaxResult.success(mdProductionLineService.selectAllEnabledLines());
    }
    
    /**
     * 根据车间ID查询生产线列表
     */
    @GetMapping("/workshop/{workshopId}")
    public AjaxResult getByWorkshopId(@PathVariable Long workshopId) {
        return AjaxResult.success(mdProductionLineService.selectMdProductionLineByWorkshopId(workshopId));
    }
    
    /**
     * 根据ID查询生产线详情
     */
    @GetMapping("/{lineId}")
    public AjaxResult getInfo(@PathVariable Long lineId) {
        return AjaxResult.success(mdProductionLineService.selectMdProductionLineById(lineId));
    }
    
    /**
     * 根据编码查询生产线
     */
    @GetMapping("/code/{lineCode}")
    public AjaxResult getByCode(@PathVariable String lineCode) {
        return AjaxResult.success(mdProductionLineService.selectMdProductionLineByCode(lineCode));
    }
    
    /**
     * 新增生产线
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdProductionLine mdProductionLine) {
        if (!mdProductionLineService.checkLineCodeUnique(mdProductionLine.getLineCode())) {
            return AjaxResult.error("新增生产线'" + mdProductionLine.getLineCode() + "'失败，生产线编码已存在");
        }
        return toAjax(mdProductionLineService.insertMdProductionLine(mdProductionLine));
    }
    
    /**
     * 修改生产线
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdProductionLine mdProductionLine) {
        return toAjax(mdProductionLineService.updateMdProductionLine(mdProductionLine));
    }
    
    /**
     * 删除生产线
     */
    @DeleteMapping("/{lineId}")
    public AjaxResult remove(@PathVariable Long lineId) {
        if (!mdProductionLineService.canDeleteLine(lineId)) {
            return AjaxResult.error("删除失败，该生产线下存在工作站");
        }
        return toAjax(mdProductionLineService.deleteMdProductionLineById(lineId));
    }
    
    /**
     * 批量删除生产线
     */
    @DeleteMapping("/batch/{lineIds}")
    public AjaxResult removeBatch(@PathVariable Long[] lineIds) {
        for (Long lineId : lineIds) {
            if (!mdProductionLineService.canDeleteLine(lineId)) {
                return AjaxResult.error("删除失败，生产线ID:" + lineId + "下存在工作站");
            }
        }
        return toAjax(mdProductionLineService.deleteMdProductionLineByIds(lineIds));
    }
}
