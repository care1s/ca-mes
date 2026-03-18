package com.carels.mes.module.system.mapper;

import com.carels.mes.module.system.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单Mapper接口
 */
public interface SysMenuMapper {
    
    /**
     * 查询菜单列表
     */
    List<SysMenu> selectSysMenuList(SysMenu menu);
    
    /**
     * 根据ID查询菜单
     */
    SysMenu selectSysMenuById(Long menuId);
    
    /**
     * 查询所有菜单（树形结构）
     */
    List<SysMenu> selectAllMenus();
    
    /**
     * 根据角色ID查询菜单
     */
    List<SysMenu> selectMenusByRoleId(Long roleId);
    
    /**
     * 根据用户ID查询菜单
     */
    List<SysMenu> selectMenusByUserId(Long userId);
    
    /**
     * 新增菜单
     */
    int insertSysMenu(SysMenu menu);
    
    /**
     * 修改菜单
     */
    int updateSysMenu(SysMenu menu);
    
    /**
     * 删除菜单
     */
    int deleteSysMenuById(Long menuId);
    
    /**
     * 更新菜单状态
     */
    int updateStatus(@Param("menuId") Long menuId, @Param("status") Integer status);
}
