package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购订单明细Mapper接口
 */
public interface PurOrderItemMapper {

    /**
     * 根据订单ID查询明细列表
     */
    List<PurOrderItem> selectPurOrderItemsByOrderId(Long orderId);

    /**
     * 新增明细
     */
    int insertPurOrderItem(PurOrderItem item);

    /**
     * 批量新增明细
     */
    int batchInsertPurOrderItem(@Param("items") List<PurOrderItem> items);

    /**
     * 修改明细
     */
    int updatePurOrderItem(PurOrderItem item);

    /**
     * 删除明细
     */
    int deletePurOrderItemById(Long itemId);

    /**
     * 根据订单ID删除所有明细
     */
    int deletePurOrderItemsByOrderId(Long orderId);
}
