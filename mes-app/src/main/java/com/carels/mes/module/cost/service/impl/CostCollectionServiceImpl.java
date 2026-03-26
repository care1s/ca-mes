package com.carels.mes.module.cost.service.impl;



import com.carels.mes.module.cost.domain.CostCollection;
import com.carels.mes.module.cost.mapper.CostCollectionMapper;
import com.carels.mes.module.cost.service.ICostCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成本归集Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class CostCollectionServiceImpl implements ICostCollectionService {

    @Autowired
    private CostCollectionMapper collectionMapper;

    @Override
    public List<CostCollection> selectCostCollectionList(CostCollection collection) {
        return collectionMapper.selectCostCollectionList(collection);
    }

    @Override
    public CostCollection selectCostCollectionById(Long collectionId) {
        return collectionMapper.selectCostCollectionById(collectionId);
    }

    @Override
    public int insertCostCollection(CostCollection collection) {
        collection.setCreateTime(new Date());
        collection.setCreateBy("admin");
        collection.setStatus("0"); // 草稿状态
        collection.setCollectionNo(generateCollectionNo());
        return collectionMapper.insertCostCollection(collection);
    }

    @Override
    public int updateCostCollection(CostCollection collection) {
        collection.setUpdateTime(new Date());
        collection.setUpdateBy("admin");
        return collectionMapper.updateCostCollection(collection);
    }

    @Override
    public int confirmCollection(Long collectionId) {
        return collectionMapper.updateStatus(collectionId, "1"); // 已确认
    }

    @Override
    public int deleteCostCollectionById(Long collectionId) {
        return collectionMapper.deleteCostCollectionById(collectionId);
    }

    @Override
    public int deleteCostCollectionByIds(Long[] collectionIds) {
        return collectionMapper.deleteCostCollectionByIds(collectionIds);
    }

    @Override
    public List<Map<String, Object>> sumCostByWorkorder(Date startDate, Date endDate) {
        return collectionMapper.sumCostByWorkorder(startDate, endDate);
    }

    /**
     * 生成归集单号
     */
    private String generateCollectionNo() {
        String prefix = "CB";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }
}
