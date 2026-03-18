package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurReturn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购退货Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface PurReturnMapper {

    /**
     * 查询采购退货列表
     */
    List<PurReturn> selectPurReturnList(PurReturn purReturn);

    /**
     * 根据ID查询采购退货
     */
    PurReturn selectPurReturnById(Long returnId);

    /**
     * 根据单号查询采购退货
     */
    PurReturn selectPurReturnByCode(String returnCode);

    /**
     * 查询入库的退货记录
     */
    List<PurReturn> selectReturnsByReceipt(Long receiptId);

    /**
     * 新增采购退货
     */
    int insertPurReturn(PurReturn purReturn);

    /**
     * 修改采购退货
     */
    int updatePurReturn(PurReturn purReturn);

    /**
     * 删除采购退货
     */
    int deletePurReturnById(Long returnId);

    /**
     * 批量删除采购退货
     */
    int deletePurReturnByIds(Long[] returnIds);

    /**
     * 更新退货状态
     */
    int updateReturnStatus(@Param("returnId") Long returnId, @Param("status") String status);
}
