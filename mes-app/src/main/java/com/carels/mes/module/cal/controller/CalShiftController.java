package com.carels.mes.module.cal.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.cal.domain.CalShift;
import com.carels.mes.module.cal.service.ICalShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班次管理Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/mes/cal/shift")
public class CalShiftController extends BaseController {

    @Autowired
    private ICalShiftService calShiftService;

    /**
     * 查询班次列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              CalShift calShift) {
        startPage(pageNum, pageSize);
        List<CalShift> list = calShiftService.selectCalShiftList(calShift);
        return getDataTable(list);
    }

    /**
     * 查询所有启用的班次
     */
    @GetMapping("/all")
    public AjaxResult all() {
        return AjaxResult.success(calShiftService.selectAllActiveShifts());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{shiftId}")
    public AjaxResult getInfo(@PathVariable Long shiftId) {
        return AjaxResult.success(calShiftService.selectCalShiftById(shiftId));
    }

    /**
     * 新增班次
     */
    @PostMapping
    public AjaxResult add(@RequestBody CalShift calShift) {
        if (!calShiftService.checkShiftCodeUnique(calShift.getShiftCode())) {
            return AjaxResult.error("新增班次'" + calShift.getShiftCode() + "'失败，班次编码已存在");
        }
        return toAjax(calShiftService.insertCalShift(calShift));
    }

    /**
     * 修改班次
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CalShift calShift) {
        return toAjax(calShiftService.updateCalShift(calShift));
    }

    /**
     * 删除班次
     */
    @DeleteMapping("/{shiftId}")
    public AjaxResult remove(@PathVariable Long shiftId) {
        return toAjax(calShiftService.deleteCalShiftById(shiftId));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch/{shiftIds}")
    public AjaxResult removeBatch(@PathVariable Long[] shiftIds) {
        return toAjax(calShiftService.deleteCalShiftByIds(shiftIds));
    }
}
