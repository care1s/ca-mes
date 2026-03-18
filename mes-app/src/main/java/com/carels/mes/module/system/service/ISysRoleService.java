package com.carels.mes.module.system.service;

import com.carels.mes.module.system.domain.SysRole;

import java.util.List;

/**
 * 系统角色Service接口
 */
public interface ISysRoleService {
    
    /**
     * 查询角色列表
     */
    List<SysRole> selectSysRoleList(SysRole role);
    
    /**
     * 根据ID查询角色
     */
    SysRole selectSysRoleById(Long roleId);
    
    /**
     * 新增角色
     */
    int insertSysRole(SysRole role);
    
    /**
     * 修改角色
     */
    int updateSysRole(SysRole role);
    
    /**
     * 删除角色
     */
    int deleteSysRoleById(Long roleId);
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
    
    /**
     * 更新角色状态
     */
    void updateStatus(Long roleId, Integer status);
    
    /**
     * 分配角色菜单权限
     */
    void assignRoleMenus(Long roleId, Long[] menuIds);
}
