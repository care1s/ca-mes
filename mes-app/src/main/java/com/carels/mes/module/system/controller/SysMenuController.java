package com.carels.mes.module.system.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.system.domain.SysMenu;
import com.carels.mes.module.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单Controller
 */
@RestController
@RequestMapping("/mes/system/menu")
public class SysMenuController extends BaseController {
    
    @Autowired
    private ISysMenuService menuService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              SysMenu menu) {
        startPage(pageNum, pageSize);
        List<SysMenu> list = menuService.selectSysMenuList(menu);
        return getDataTable(list);
    }
    
    @GetMapping("/tree")
    public AjaxResult tree() {
        return AjaxResult.success(menuService.selectAllMenus());
    }
    
    @GetMapping("/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return AjaxResult.success(menuService.selectSysMenuById(menuId));
    }
    
    @GetMapping("/role/{roleId}")
    public AjaxResult getMenusByRoleId(@PathVariable Long roleId) {
        return AjaxResult.success(menuService.selectMenusByRoleId(roleId));
    }
    
    @GetMapping("/user/{userId}")
    public AjaxResult getMenusByUserId(@PathVariable Long userId) {
        return AjaxResult.success(menuService.selectMenusByUserId(userId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody SysMenu menu) {
        return toAjax(menuService.insertSysMenu(menu));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody SysMenu menu) {
        return toAjax(menuService.updateSysMenu(menu));
    }
    
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable Long menuId) {
        return toAjax(menuService.deleteSysMenuById(menuId));
    }
    
    @PutMapping("/{menuId}/status")
    public AjaxResult updateStatus(@PathVariable Long menuId, @RequestParam Integer status) {
        menuService.updateStatus(menuId, status);
        return AjaxResult.success("状态更新成功");
    }
}
