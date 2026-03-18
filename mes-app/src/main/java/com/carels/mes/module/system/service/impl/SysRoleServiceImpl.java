package com.carels.mes.module.system.service.impl;

import com.carels.mes.module.system.domain.SysRole;
import com.carels.mes.module.system.mapper.SysRoleMapper;
import com.carels.mes.module.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统角色Service实现
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    
    @Autowired
    private SysRoleMapper roleMapper;
    
    @Override
    public List<SysRole> selectSysRoleList(SysRole role) {
        return roleMapper.selectSysRoleList(role);
    }
    
    @Override
    public SysRole selectSysRoleById(Long roleId) {
        return roleMapper.selectSysRoleById(roleId);
    }
    
    @Override
    public int insertSysRole(SysRole role) {
        role.setStatus(0);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.insertSysRole(role);
    }
    
    @Override
    public int updateSysRole(SysRole role) {
        role.setUpdateTime(LocalDateTime.now());
        return roleMapper.updateSysRole(role);
    }
    
    @Override
    public int deleteSysRoleById(Long roleId) {
        return roleMapper.deleteSysRoleById(roleId);
    }
    
    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }
    
    @Override
    public void updateStatus(Long roleId, Integer status) {
        roleMapper.updateStatus(roleId, status);
    }
    
    @Override
    public void assignRoleMenus(Long roleId, Long[] menuIds) {
        // 先删除原有菜单关联，再添加新的
        // 这里简化实现，实际应该调用SysRoleMenuMapper
    }
}
