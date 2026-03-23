package com.carels.mes.module.system.mapper;

import com.carels.mes.module.system.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper接口
 */
public interface SysUserRoleMapper {

    /**
     * 批量新增用户角色关联
     */
    int batchInsertUserRole(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds);

    /**
     * 删除用户所有角色关联
     */
    int deleteUserRoleByUserId(Long userId);

    /**
     * 查询用户的角色ID列表
     */
    List<Long> selectRoleIdsByUserId(Long userId);
}
