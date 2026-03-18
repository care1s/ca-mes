package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmWarning;
import com.carels.mes.module.wm.mapper.WmWarningMapper;
import com.carels.mes.module.wm.service.IWmWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存预警Service实现
 */
@Service
public class WmWarningServiceImpl implements IWmWarningService {
    
    @Autowired
    private WmWarningMapper warningMapper;
    
    @Override
    public List<WmWarning> selectWmWarningList(WmWarning warning) {
        return warningMapper.selectWmWarningList(warning);
    }
    
    @Override
    public WmWarning selectWmWarningById(Long warningId) {
        return warningMapper.selectWmWarningById(warningId);
    }
    
    @Override
    public int insertWmWarning(WmWarning warning) {
        warning.setStatus(0);
        warning.setCreateTime(LocalDateTime.now());
        warning.setUpdateTime(LocalDateTime.now());
        return warningMapper.insertWmWarning(warning);
    }
    
    @Override
    public int updateWmWarning(WmWarning warning) {
        warning.setUpdateTime(LocalDateTime.now());
        return warningMapper.updateWmWarning(warning);
    }
    
    @Override
    public int deleteWmWarningById(Long warningId) {
        return warningMapper.deleteWmWarningById(warningId);
    }
    
    @Override
    public void handleWarning(Long warningId, String handleRemark, String handlerName) {
        WmWarning warning = new WmWarning();
        warning.setWarningId(warningId);
        warning.setStatus(2);
        warning.setHandleRemark(handleRemark);
        warning.setHandlerName(handlerName);
        warning.setHandleTime(LocalDateTime.now());
        warning.setUpdateTime(LocalDateTime.now());
        warningMapper.updateWmWarning(warning);
    }
    
    @Override
    public int countWarning(Integer warningType, Integer warningLevel) {
        return warningMapper.countWarning(warningType, warningLevel);
    }
}
