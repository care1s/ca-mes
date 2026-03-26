package com.carels.mes.module.cost.mapper;

import com.carels.mes.module.cost.domain.CostCollection;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成本归集Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface CostCollectionMapper {

    /**
     * 查询成本归集列表
     */
    List<CostCollection> selectCostCollectionList(CostCollection collection);

    /**
     * 根据ID查询成本归集
     */
    CostCollection selectCostCollectionById(Long collectionId);

    /**
     * 新增成本归集
     */
    int insertCostCollection(CostCollection collection);

    /**
     * 修改成本归集
     */
    int updateCostCollection(CostCollection collection);

    /**
     * 修改状态
     */
    int updateStatus(@Param("collectionId") Long collectionId, @Param("status") String status);

    /**
     * 删除成本归集
     */
    int deleteCostCollectionById(Long collectionId);

    /**
     * 批量删除成本归集
     */
    int deleteCostCollectionByIds(Long[] collectionIds);

    /**
     * 汇总成本数据
     */
    List<Map<String, Object>> sumCostByWorkorder(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
