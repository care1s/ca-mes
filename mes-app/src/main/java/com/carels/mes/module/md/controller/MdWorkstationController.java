package com.carels.mes.module.md.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.md.domain.MdWorkstation;
import com.carels.mes.module.md.service.IMdWorkstationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工作站管理Controller - carels
 * 
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
@RestController
@RequestMapping("/mes/md/workstation")
public class MdWorkstationController extends BaseController {
    
    @Autowired
    private IMdWorkstationService mdWorkstationService;
    
    /**
     * 查询工作站列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              MdWorkstation mdWorkstation) {
        startPage(pageNum, pageSize);
        List<MdWorkstation> list = mdWorkstationService.selectMdWorkstationList(mdWorkstation);
        return getDataTable(list);
    }
    
    /**
     * 查询所有启用的工作站（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        return AjaxResult.success(mdWorkstationService.selectAllEnabledWorkstations());
    }
    
    /**
     * 根据车间ID查询工作站列表
     */
    @GetMapping("/workshop/{workshopId}")
    public AjaxResult getByWorkshopId(@PathVariable Long workshopId) {
        return AjaxResult.success(mdWorkstationService.selectMdWorkstationByWorkshopId(workshopId));
    }
    
    /**
     * 根据生产线ID查询工作站列表
     */
    @GetMapping("/line/{lineId}")
    public AjaxResult getByLineId(@PathVariable Long lineId) {
        return AjaxResult.success(mdWorkstationService.selectMdWorkstationByLineId(lineId));
    }
    
    /**
     * 根据ID查询工作站详情
     */
    @GetMapping("/{workstationId}")
    public AjaxResult getInfo(@PathVariable Long workstationId) {
        return AjaxResult.success(mdWorkstationService.selectMdWorkstationById(workstationId));
    }
    
    /**
     * 根据编码查询工作站
     */
    @GetMapping("/code/{workstationCode}")
    public AjaxResult getByCode(@PathVariable String workstationCode) {
        return AjaxResult.success(mdWorkstationService.selectMdWorkstationByCode(workstationCode));
    }
    
    /**
     * 新增工作站
     */
    @PostMapping
    public AjaxResult add(@RequestBody MdWorkstation mdWorkstation) {
        if (!mdWorkstationService.checkWorkstationCodeUnique(mdWorkstation.getWorkstationCode())) {
            return AjaxResult.error("新增工作站'" + mdWorkstation.getWorkstationCode() + "'失败，工作站编码已存在");
        }
        return toAjax(mdWorkstationService.insertMdWorkstation(mdWorkstation));
    }
    
    /**
     * 修改工作站
     */
    @PutMapping
    public AjaxResult edit(@RequestBody MdWorkstation mdWorkstation) {
        return toAjax(mdWorkstationService.updateMdWorkstation(mdWorkstation));
    }
    
    /**
     * 删除工作站
     */
    @DeleteMapping("/{workstationId}")
    public AjaxResult remove(@PathVariable Long workstationId) {
        return toAjax(mdWorkstationService.deleteMdWorkstationById(workstationId));
    }
    
    /**
     * 批量删除工作站
     */
    @DeleteMapping("/batch/{workstationIds}")
    public AjaxResult removeBatch(@PathVariable Long[] workstationIds) {
        return toAjax(mdWorkstationService.deleteMdWorkstationByIds(workstationIds));
    }
}
