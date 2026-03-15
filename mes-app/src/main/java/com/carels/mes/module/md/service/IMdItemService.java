package com.carels.mes.module.md.service;

import com.carels.mes.module.md.domain.MdItem;

import java.util.List;

/**
 * 物料Service接口 - carels
 * 
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
public interface IMdItemService {
    
    /**
     * 查询物料列表
     */
    List<MdItem> selectMdItemList(MdItem mdItem);
    
    /**
     * 根据ID查询物料
     */
    MdItem selectMdItemById(Long itemId);
    
    /**
     * 根据编码查询物料
     */
    MdItem selectMdItemByCode(String itemCode);
    
    /**
     * 新增物料
     */
    int insertMdItem(MdItem mdItem);
    
    /**
     * 修改物料
     */
    int updateMdItem(MdItem mdItem);
    
    /**
     * 删除物料
     */
    int deleteMdItemById(Long itemId);
    
    /**
     * 批量删除物料
     */
    int deleteMdItemByIds(Long[] itemIds);
    
    /**
     * 校验物料编码唯一性
     */
    boolean checkItemCodeUnique(String itemCode);
    
    /**
     * 导入物料数据
     */
    String importMdItem(List<MdItem> itemList, boolean updateSupport);
}
