package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdItem;
import com.carels.mes.module.md.mapper.MdItemMapper;
import com.carels.mes.module.md.service.IMdItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物料Service实现 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
@Service
public class MdItemServiceImpl implements IMdItemService {
    
    @Autowired
    private MdItemMapper mdItemMapper;
    
    @Override
    public List<MdItem> selectMdItemList(MdItem mdItem) {
        return mdItemMapper.selectMdItemList(mdItem);
    }
    
    @Override
    public MdItem selectMdItemById(Long itemId) {
        return mdItemMapper.selectMdItemById(itemId);
    }
    
    @Override
    public MdItem selectMdItemByCode(String itemCode) {
        return mdItemMapper.selectMdItemByCode(itemCode);
    }
    
    @Override
    public int insertMdItem(MdItem mdItem) {
        // 生成物料编码（如果不存在）
        if (mdItem.getItemCode() == null || mdItem.getItemCode().isEmpty()) {
            mdItem.setItemCode(generateItemCode());
        }
        return mdItemMapper.insertMdItem(mdItem);
    }
    
    @Override
    public int updateMdItem(MdItem mdItem) {
        return mdItemMapper.updateMdItem(mdItem);
    }
    
    @Override
    public int deleteMdItemById(Long itemId) {
        return mdItemMapper.deleteMdItemById(itemId);
    }
    
    @Override
    public int deleteMdItemByIds(Long[] itemIds) {
        return mdItemMapper.deleteMdItemByIds(itemIds);
    }
    
    @Override
    public boolean checkItemCodeUnique(String itemCode) {
        MdItem item = mdItemMapper.selectMdItemByCode(itemCode);
        return item == null;
    }
    
    @Override
    public String importMdItem(List<MdItem> itemList, boolean updateSupport) {
        // 导入逻辑
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        
        for (MdItem item : itemList) {
            try {
                MdItem existItem = mdItemMapper.selectMdItemByCode(item.getItemCode());
                if (existItem == null) {
                    mdItemMapper.insertMdItem(item);
                    successNum++;
                } else if (updateSupport) {
                    item.setItemId(existItem.getItemId());
                    mdItemMapper.updateMdItem(item);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、物料 ").append(item.getItemCode()).append(" 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、物料 ").append(item.getItemCode()).append(" 导入失败：").append(e.getMessage());
            }
        }
        
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new RuntimeException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        
        return successMsg.toString();
    }
    
    /**
     * 生成物料编码
     */
    private String generateItemCode() {
        return "ITEM" + System.currentTimeMillis();
    }
}
