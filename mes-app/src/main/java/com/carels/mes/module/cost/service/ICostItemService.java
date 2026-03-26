package com.carels.mes.module.cost.service;

import com.carels.mes.module.cost.domain.CostItem;

import java.util.List;

/**
 * 成本项目Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ICostItemService {

    /**
     * 查询成本项目列表
     */
    List<CostItem> selectCostItemList(CostItem costItem);

    /**
     * 根据ID查询成本项目
     */
    CostItem selectCostItemById(Long itemId);

    /**
     * 新增成本项目
     */
    int insertCostItem(CostItem costItem);

    /**
     * 修改成本项目
     */
    int updateCostItem(CostItem costItem);

    /**
     * 删除成本项目
     */
    int deleteCostItemById(Long itemId);

    /**
     * 批量删除成本项目
     */
    int deleteCostItemByIds(Long[] itemIds);
}
