package com.carels.mes.module.system.service.impl;

import com.carels.mes.module.system.domain.SysUser;
import com.carels.mes.module.system.mapper.SysUserMapper;
import com.carels.mes.module.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户Service实现
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    
    @Autowired
    private SysUserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public List<SysUser> selectSysUserList(SysUser user) {
        return userMapper.selectSysUserList(user);
    }
    
    @Override
    public SysUser selectSysUserById(Long userId) {
        return userMapper.selectSysUserById(userId);
    }
    
    @Override
    public SysUser selectSysUserByUsername(String username) {
        return userMapper.selectSysUserByUsername(username);
    }
    
    @Override
    public int insertSysUser(SysUser user) {
        // 加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setStatus(0);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.insertSysUser(user);
    }
    
    @Override
    public int updateSysUser(SysUser user) {
        // 如果密码不为空，需要加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateSysUser(user);
    }
    
    @Override
    public int deleteSysUserById(Long userId) {
        return userMapper.deleteSysUserById(userId);
    }
    
    @Override
    public void resetPassword(Long userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userMapper.resetPassword(userId, encodedPassword);
    }
    
    @Override
    public void updateStatus(Long userId, Integer status) {
        userMapper.updateStatus(userId, status);
    }
    
    @Override
    public void assignUserRoles(Long userId, Long[] roleIds) {
        // 先删除原有角色关联，再添加新的
        // 这里简化实现，实际应该调用SysUserRoleMapper
    }
}
