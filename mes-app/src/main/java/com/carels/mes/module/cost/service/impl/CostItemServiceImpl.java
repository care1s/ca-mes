package com.carels.mes.module.cost.service.impl;



import com.carels.mes.module.cost.domain.CostItem;
import com.carels.mes.module.cost.mapper.CostItemMapper;
import com.carels.mes.module.cost.service.ICostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 成本项目Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class CostItemServiceImpl implements ICostItemService {

    @Autowired
    private CostItemMapper costItemMapper;

    @Override
    public List<CostItem> selectCostItemList(CostItem costItem) {
        return costItemMapper.selectCostItemList(costItem);
    }

    @Override
    public CostItem selectCostItemById(Long itemId) {
        return costItemMapper.selectCostItemById(itemId);
    }

    @Override
    public int insertCostItem(CostItem costItem) {
        costItem.setCreateTime(new Date());
        costItem.setCreateBy("admin");
        return costItemMapper.insertCostItem(costItem);
    }

    @Override
    public int updateCostItem(CostItem costItem) {
        costItem.setUpdateTime(new Date());
        costItem.setUpdateBy("admin");
        return costItemMapper.updateCostItem(costItem);
    }

    @Override
    public int deleteCostItemById(Long itemId) {
        return costItemMapper.deleteCostItemById(itemId);
    }

    @Override
    public int deleteCostItemByIds(Long[] itemIds) {
        return costItemMapper.deleteCostItemByIds(itemIds);
    }
}
