package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdWorkshop;
import com.carels.mes.module.md.service.IMdWorkshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车间管理Controller - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/mes/md/workshop")
public class MdWorkshopController extends BaseController {
    
    @Autowired
    private IMdWorkshopService mdWorkshopService;
    
    /**
     * 查询车间列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              MdWorkshop mdWorkshop) {
        startPage(pageNum, pageSize);
        List<MdWorkshop> list = mdWorkshopService.selectMdWorkshopList(mdWorkshop);
        return getDataTable(list);
    }
    
    /**
     * 查询所有启用的车间（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        return AjaxResult.success(mdWorkshopService.selectAllEnabledWorkshops());
    }
    
    /**
     * 根据ID查询车间详情
     */
    @GetMapping("/{workshopId}")
    public AjaxResult getInfo(@PathVariable Long workshopId) {
        return AjaxResult.success(mdWorkshopService.selectMdWorkshopById(workshopId));
    }
    
    /**
     * 根据编码查询车间
     */
    @GetMapping("/code/{workshopCode}")
    public AjaxResult getByCode(@PathVariable String workshopCode) {
        return AjaxResult.success(mdWorkshopService.selectMdWorkshopByCode(workshopCode));
    }
    
    /**
     * 新增车间
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdWorkshop mdWorkshop) {
        if (!mdWorkshopService.checkWorkshopCodeUnique(mdWorkshop.getWorkshopCode())) {
            return AjaxResult.error("新增车间'" + mdWorkshop.getWorkshopCode() + "'失败，车间编码已存在");
        }
        return toAjax(mdWorkshopService.insertMdWorkshop(mdWorkshop));
    }
    
    /**
     * 修改车间
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdWorkshop mdWorkshop) {
        return toAjax(mdWorkshopService.updateMdWorkshop(mdWorkshop));
    }
    
    /**
     * 删除车间
     */
    @DeleteMapping("/{workshopId}")
    public AjaxResult remove(@PathVariable Long workshopId) {
        if (!mdWorkshopService.canDeleteWorkshop(workshopId)) {
            return AjaxResult.error("删除失败，该车间下存在生产线或工作站");
        }
        return toAjax(mdWorkshopService.deleteMdWorkshopById(workshopId));
    }
    
    /**
     * 批量删除车间
     */
    @DeleteMapping("/batch/{workshopIds}")
    public AjaxResult removeBatch(@PathVariable Long[] workshopIds) {
        for (Long workshopId : workshopIds) {
            if (!mdWorkshopService.canDeleteWorkshop(workshopId)) {
                return AjaxResult.error("删除失败，车间ID:" + workshopId + "下存在生产线或工作站");
            }
        }
        return toAjax(mdWorkshopService.deleteMdWorkshopByIds(workshopIds));
    }
}
