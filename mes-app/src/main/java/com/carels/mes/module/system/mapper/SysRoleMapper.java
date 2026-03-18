package com.carels.mes.module.system.mapper;

import com.carels.mes.module.system.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色Mapper接口
 */
public interface SysRoleMapper {
    
    /**
     * 查询角色列表
     */
    List<SysRole> selectSysRoleList(SysRole role);
    
    /**
     * 根据ID查询角色
     */
    SysRole selectSysRoleById(Long roleId);
    
    /**
     * 根据编码查询角色
     */
    SysRole selectSysRoleByCode(String roleCode);
    
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
     * 批量删除角色
     */
    int deleteSysRoleByIds(Long[] roleIds);
    
    /**
     * 更新角色状态
     */
    int updateStatus(@Param("roleId") Long roleId, @Param("status") Integer status);
    
    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);
}
