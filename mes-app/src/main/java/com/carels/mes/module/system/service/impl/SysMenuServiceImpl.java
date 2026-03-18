package com.carels.mes.module.system.service.impl;

import com.carels.mes.module.system.domain.SysMenu;
import com.carels.mes.module.system.mapper.SysMenuMapper;
import com.carels.mes.module.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统菜单Service实现
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService {
    
    @Autowired
    private SysMenuMapper menuMapper;
    
    @Override
    public List<SysMenu> selectSysMenuList(SysMenu menu) {
        return menuMapper.selectSysMenuList(menu);
    }
    
    @Override
    public SysMenu selectSysMenuById(Long menuId) {
        return menuMapper.selectSysMenuById(menuId);
    }
    
    @Override
    public List<SysMenu> selectAllMenus() {
        return menuMapper.selectAllMenus();
    }
    
    @Override
    public List<SysMenu> selectMenusByRoleId(Long roleId) {
        return menuMapper.selectMenusByRoleId(roleId);
    }
    
    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        return menuMapper.selectMenusByUserId(userId);
    }
    
    @Override
    public int insertSysMenu(SysMenu menu) {
        menu.setStatus(0);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        return menuMapper.insertSysMenu(menu);
    }
    
    @Override
    public int updateSysMenu(SysMenu menu) {
        menu.setUpdateTime(LocalDateTime.now());
        return menuMapper.updateSysMenu(menu);
    }
    
    @Override
    public int deleteSysMenuById(Long menuId) {
        return menuMapper.deleteSysMenuById(menuId);
    }
    
    @Override
    public void updateStatus(Long menuId, Integer status) {
        menuMapper.updateStatus(menuId, status);
    }
}
