package com.carels.mes.module.system.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.system.domain.SysRole;
import com.carels.mes.module.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色Controller
 */
@RestController
@RequestMapping("/mes/system/role")
public class SysRoleController extends BaseController {
    
    @Autowired
    private ISysRoleService roleService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              SysRole role) {
        startPage(pageNum, pageSize);
        List<SysRole> list = roleService.selectSysRoleList(role);
        return getDataTable(list);
    }
    
    @GetMapping("/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        return AjaxResult.success(roleService.selectSysRoleById(roleId));
    }
    
    @GetMapping("/code/{roleCode}")
    public AjaxResult getInfoByCode(@PathVariable String roleCode) {
        // 通过编码查询角色
        return AjaxResult.success("功能开发中");
    }
    
    @GetMapping("/user/{userId}")
    public AjaxResult getRolesByUserId(@PathVariable Long userId) {
        return AjaxResult.success(roleService.selectRolesByUserId(userId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody SysRole role) {
        return toAjax(roleService.insertSysRole(role));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody SysRole role) {
        return toAjax(roleService.updateSysRole(role));
    }
    
    @DeleteMapping("/{roleId}")
    public AjaxResult remove(@PathVariable Long roleId) {
        return toAjax(roleService.deleteSysRoleById(roleId));
    }
    
    @PutMapping("/{roleId}/status")
    public AjaxResult updateStatus(@PathVariable Long roleId, @RequestParam Integer status) {
        roleService.updateStatus(roleId, status);
        return AjaxResult.success("状态更新成功");
    }
}
