package com.carels.mes.module.system.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.system.domain.SysUser;
import com.carels.mes.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户Controller
 */
@RestController
@RequestMapping("/mes/system/user")
public class SysUserController extends BaseController {
    
    @Autowired
    private ISysUserService userService;
    
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              SysUser user) {
        startPage(pageNum, pageSize);
        List<SysUser> list = userService.selectSysUserList(user);
        return getDataTable(list);
    }
    
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        return AjaxResult.success(userService.selectSysUserById(userId));
    }
    
    @PostMapping
    public AjaxResult add(@RequestBody SysUser user) {
        return toAjax(userService.insertSysUser(user));
    }
    
    @PutMapping
    public AjaxResult edit(@RequestBody SysUser user) {
        return toAjax(userService.updateSysUser(user));
    }
    
    @DeleteMapping("/{userId}")
    public AjaxResult remove(@PathVariable Long userId) {
        return toAjax(userService.deleteSysUserById(userId));
    }
    
    @PutMapping("/{userId}/resetPwd")
    public AjaxResult resetPassword(@PathVariable Long userId, @RequestParam String password) {
        userService.resetPassword(userId, password);
        return AjaxResult.success("密码重置成功");
    }
    
    @PutMapping("/{userId}/status")
    public AjaxResult updateStatus(@PathVariable Long userId, @RequestParam Integer status) {
        userService.updateStatus(userId, status);
        return AjaxResult.success("状态更新成功");
    }
}
