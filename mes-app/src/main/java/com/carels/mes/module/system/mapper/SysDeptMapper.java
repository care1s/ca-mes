package com.carels.mes.module.system.mapper;

import com.carels.mes.module.system.domain.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
public interface SysDeptMapper {

    /**
     * 查询部门列表
     */
    List<SysDept> selectDeptList(SysDept dept);

    /**
     * 根据ID查询部门
     */
    SysDept selectDeptById(Long deptId);

    /**
     * 新增部门
     */
    int insertDept(SysDept dept);

    /**
     * 修改部门
     */
    int updateDept(SysDept dept);

    /**
     * 删除部门
     */
    int deleteDeptById(Long deptId);

    /**
     * 批量删除部门
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 检查部门编码是否存在
     */
    int checkDeptCodeUnique(@Param("code") String code, @Param("deptId") Long deptId);

    /**
     * 检查部门下是否有用户
     */
    int checkDeptExistUser(Long deptId);

    /**
     * 检查部门下是否有子部门
     */
    int checkDeptExistChildren(Long deptId);

    /**
     * 查询所有部门
     */
    List<SysDept> selectDeptAll();
}
