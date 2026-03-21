package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurRequestItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购申请明细Mapper接口
 */
public interface PurRequestItemMapper {

    /**
     * 根据申请ID查询明细列表
     */
    List<PurRequestItem> selectPurRequestItemsByRequestId(Long requestId);

    /**
     * 新增明细
     */
    int insertPurRequestItem(PurRequestItem item);

    /**
     * 批量新增明细
     */
    int batchInsertPurRequestItem(@Param("items") List<PurRequestItem> items);

    /**
     * 修改明细
     */
    int updatePurRequestItem(PurRequestItem item);

    /**
     * 删除明细
     */
    int deletePurRequestItemById(Long itemId);

    /**
     * 根据申请ID删除所有明细
     */
    int deletePurRequestItemsByRequestId(Long requestId);
}
