package com.carels.mes.module.tm.service;

import com.carels.mes.module.tm.domain.TmTool;

import java.util.List;

/**
 * 工装机具Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ITmToolService {

    /**
     * 查询工装机具列表
     */
    List<TmTool> selectTmToolList(TmTool tool);

    /**
     * 根据ID查询工装机具
     */
    TmTool selectTmToolById(Long toolId);

    /**
     * 新增工装机具
     */
    int insertTmTool(TmTool tool);

    /**
     * 修改工装机具
     */
    int updateTmTool(TmTool tool);

    /**
     * 保养
     */
    int maintainTool(Long toolId);

    /**
     * 报废
     */
    int scrapTool(Long toolId);

    /**
     * 删除工装机具
     */
    int deleteTmToolById(Long toolId);

    /**
     * 批量删除工装机具
     */
    int deleteTmToolByIds(Long[] toolIds);
}
