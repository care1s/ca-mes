package com.carels.mes.module.system.service;

import com.carels.mes.module.system.domain.SysUser;

import java.util.List;

/**
 * 系统用户Service接口
 */
public interface ISysUserService {
    
    /**
     * 查询用户列表
     */
    List<SysUser> selectSysUserList(SysUser user);
    
    /**
     * 根据ID查询用户
     */
    SysUser selectSysUserById(Long userId);
    
    /**
     * 根据用户名查询用户
     */
    SysUser selectSysUserByUsername(String username);
    
    /**
     * 新增用户
     */
    int insertSysUser(SysUser user);
    
    /**
     * 修改用户
     */
    int updateSysUser(SysUser user);
    
    /**
     * 删除用户
     */
    int deleteSysUserById(Long userId);
    
    /**
     * 重置密码
     */
    void resetPassword(Long userId, String password);
    
    /**
     * 更新用户状态
     */
    void updateStatus(Long userId, Integer status);
    
    /**
     * 分配用户角色
     */
    void assignUserRoles(Long userId, Long[] roleIds);
}
