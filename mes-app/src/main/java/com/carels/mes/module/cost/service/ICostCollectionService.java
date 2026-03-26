package com.carels.mes.module.cost.service;

import com.carels.mes.module.cost.domain.CostCollection;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成本归集Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ICostCollectionService {

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
     * 确认成本归集
     */
    int confirmCollection(Long collectionId);

    /**
     * 删除成本归集
     */
    int deleteCostCollectionById(Long collectionId);

    /**
     * 批量删除成本归集
     */
    int deleteCostCollectionByIds(Long[] collectionIds);

    /**
     * 按工单汇总成本
     */
    List<Map<String, Object>> sumCostByWorkorder(Date startDate, Date endDate);
}
