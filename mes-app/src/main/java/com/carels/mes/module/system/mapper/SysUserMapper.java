package com.carels.mes.module.system.mapper;

import com.carels.mes.module.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Mapper接口
 */
public interface SysUserMapper {
    
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
     * 批量删除用户
     */
    int deleteSysUserByIds(Long[] userIds);
    
    /**
     * 更新用户状态
     */
    int updateStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    /**
     * 重置密码
     */
    int resetPassword(@Param("userId") Long userId, @Param("password") String password);
}
