package com.carels.mes.module.tm.service.impl;



import com.carels.mes.module.tm.domain.TmTool;
import com.carels.mes.module.tm.mapper.TmToolMapper;
import com.carels.mes.module.tm.service.ITmToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 工装机具Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class TmToolServiceImpl implements ITmToolService {

    @Autowired
    private TmToolMapper toolMapper;

    @Override
    public List<TmTool> selectTmToolList(TmTool tool) {
        return toolMapper.selectTmToolList(tool);
    }

    @Override
    public TmTool selectTmToolById(Long toolId) {
        return toolMapper.selectTmToolById(toolId);
    }

    @Override
    public int insertTmTool(TmTool tool) {
        tool.setCreateTime(new Date());
        tool.setCreateBy("admin");
        tool.setStatus("1"); // 可用状态
        tool.setUsedLife(0);
        if (tool.getLifeLimit() != null) {
            tool.setRemainLife(tool.getLifeLimit());
        }
        return toolMapper.insertTmTool(tool);
    }

    @Override
    public int updateTmTool(TmTool tool) {
        tool.setUpdateTime(new Date());
        tool.setUpdateBy("admin");
        // 重新计算剩余寿命
        if (tool.getLifeLimit() != null && tool.getUsedLife() != null) {
            tool.setRemainLife(tool.getLifeLimit() - tool.getUsedLife());
        }
        return toolMapper.updateTmTool(tool);
    }

    @Override
    public int maintainTool(Long toolId) {
        TmTool tool = new TmTool();
        tool.setToolId(toolId);
        tool.setLastMaintainDate(new Date());
        tool.setUpdateTime(new Date());
        tool.setUpdateBy("admin");
        return toolMapper.updateTmTool(tool);
    }

    @Override
    public int scrapTool(Long toolId) {
        return toolMapper.updateStatus(toolId, "3"); // 报废状态
    }

    @Override
    public int deleteTmToolById(Long toolId) {
        return toolMapper.deleteTmToolById(toolId);
    }

    @Override
    public int deleteTmToolByIds(Long[] toolIds) {
        return toolMapper.deleteTmToolByIds(toolIds);
    }
}
