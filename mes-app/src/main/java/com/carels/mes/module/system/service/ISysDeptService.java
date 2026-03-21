package com.carels.mes.module.system.service;

import com.carels.mes.module.system.domain.SysDept;

import java.util.List;

/**
 * 部门Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
public interface ISysDeptService {

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
     * 检查部门编码唯一性
     */
    boolean checkDeptCodeUnique(SysDept dept);

    /**
     * 查询所有部门
     */
    List<SysDept> selectDeptAll();
}
