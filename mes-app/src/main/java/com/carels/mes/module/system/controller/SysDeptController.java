package com.carels.mes.module.system.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.system.domain.SysDept;
import com.carels.mes.module.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理Controller
 */
@RestController
@RequestMapping("/mes/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private ISysDeptService deptService;

    /**
     * 查询部门列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              SysDept dept) {
        startPage(pageNum, pageSize);
        List<SysDept> list = deptService.selectDeptList(dept);
        return getDataTable(list);
    }

    /**
     * 获取所有部门列表（用于下拉选择）
     */
    @GetMapping("/all")
    public AjaxResult all() {
        List<SysDept> list = deptService.selectDeptAll();
        return AjaxResult.success(list);
    }

    /**
     * 根据ID获取部门
     */
    @GetMapping("/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId) {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     */
    @PostMapping
    public AjaxResult add(@RequestBody SysDept dept) {
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * 修改部门
     */
    @PutMapping
    public AjaxResult edit(@RequestBody SysDept dept) {
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * 删除部门
     */
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId) {
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
