package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmWarning;

import java.util.List;

/**
 * 库存预警Service接口
 */
public interface IWmWarningService {
    
    /**
     * 查询预警列表
     */
    List<WmWarning> selectWmWarningList(WmWarning warning);
    
    /**
     * 根据ID查询预警
     */
    WmWarning selectWmWarningById(Long warningId);
    
    /**
     * 新增预警记录
     */
    int insertWmWarning(WmWarning warning);
    
    /**
     * 修改预警记录
     */
    int updateWmWarning(WmWarning warning);
    
    /**
     * 删除预警记录
     */
    int deleteWmWarningById(Long warningId);
    
    /**
     * 处理预警
     */
    void handleWarning(Long warningId, String handleRemark, String handlerName);
    
    /**
     * 统计预警数量
     */
    int countWarning(Integer warningType, Integer warningLevel);
}
