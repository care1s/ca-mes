package com.carels.mes.module.wm.mapper;

import com.carels.mes.module.wm.domain.WmStocktaking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 库存盘点Mapper接口
 */
public interface WmStocktakingMapper {
    
    /**
     * 查询盘点列表
     */
    List<WmStocktaking> selectWmStocktakingList(WmStocktaking stocktaking);
    
    /**
     * 根据ID查询盘点单
     */
    WmStocktaking selectWmStocktakingById(Long stocktakingId);
    
    /**
     * 根据单号查询盘点单
     */
    WmStocktaking selectWmStocktakingByNo(String stocktakingNo);
    
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
     * 批量删除盘点单
     */
    int deleteWmStocktakingByIds(Long[] stocktakingIds);
    
    /**
     * 更新盘点状态
     */
    int updateStatus(@Param("stocktakingId") Long stocktakingId, @Param("status") Integer status);
}
