package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurReceipt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购入库Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface PurReceiptMapper {

    /**
     * 查询采购入库列表
     */
    List<PurReceipt> selectPurReceiptList(PurReceipt purReceipt);

    /**
     * 根据ID查询采购入库
     */
    PurReceipt selectPurReceiptById(Long receiptId);

    /**
     * 根据单号查询采购入库
     */
    PurReceipt selectPurReceiptByCode(String receiptCode);

    /**
     * 查询订单的入库记录
     */
    List<PurReceipt> selectReceiptsByOrder(Long orderId);

    /**
     * 新增采购入库
     */
    int insertPurReceipt(PurReceipt purReceipt);

    /**
     * 修改采购入库
     */
    int updatePurReceipt(PurReceipt purReceipt);

    /**
     * 删除采购入库
     */
    int deletePurReceiptById(Long receiptId);

    /**
     * 批量删除采购入库
     */
    int deletePurReceiptByIds(Long[] receiptIds);

    /**
     * 更新入库状态
     */
    int updateReceiptStatus(@Param("receiptId") Long receiptId, @Param("status") String status);

    /**
     * 质检通过
     */
    int passInspection(@Param("receiptId") Long receiptId, @Param("inspectorId") Long inspectorId, 
                       @Param("inspectorName") String inspectorName, @Param("remark") String remark);
}
