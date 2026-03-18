package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmStocktaking;

import java.util.List;

/**
 * 库存盘点Service接口
 */
public interface IWmStocktakingService {
    
    /**
     * 查询盘点列表
     */
    List<WmStocktaking> selectWmStocktakingList(WmStocktaking stocktaking);
    
    /**
     * 根据ID查询盘点单
     */
    WmStocktaking selectWmStocktakingById(Long stocktakingId);
    
    /**
     * 新增盘点单
     */
    int insertWmStocktaking(WmStocktaking stocktaking);
    
    /**
     * 修改盘点单
     */
    int updateWmStocktaking(WmStocktaking stocktaking);
    
    /**
     * 删除盘点单
     */
    int deleteWmStocktakingById(Long stocktakingId);
    
    /**
     * 开始盘点
     */
    void startStocktaking(Long stocktakingId);
    
    /**
     * 完成盘点
     */
    void completeStocktaking(Long stocktakingId);
    
    /**
     * 取消盘点
     */
    void cancelStocktaking(Long stocktakingId);
}
