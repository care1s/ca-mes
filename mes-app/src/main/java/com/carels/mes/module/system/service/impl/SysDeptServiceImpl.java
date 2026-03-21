package com.carels.mes.module.system.service.impl;

import com.carels.mes.module.system.domain.SysDept;
import com.carels.mes.module.system.mapper.SysDeptMapper;
import com.carels.mes.module.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门服务实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService {

    @Autowired
    private SysDeptMapper deptMapper;

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        return deptMapper.selectDeptList(dept);
    }

    @Override
    public SysDept selectDeptById(Long deptId) {
        return deptMapper.selectDeptById(deptId);
    }

    @Override
    public List<SysDept> selectDeptAll() {
        return deptMapper.selectDeptAll();
    }

    @Override
    public int insertDept(SysDept dept) {
        return deptMapper.insertDept(dept);
    }

    @Override
    public int updateDept(SysDept dept) {
        return deptMapper.updateDept(dept);
    }

    @Override
    public int deleteDeptById(Long deptId) {
        return deptMapper.deleteDeptById(deptId);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        return deptMapper.deleteDeptByIds(deptIds);
    }

    @Override
    public boolean checkDeptCodeUnique(SysDept dept) {
        int count = deptMapper.checkDeptCodeUnique(dept.getCode(), dept.getDeptId());
        return count == 0;
    }
}
