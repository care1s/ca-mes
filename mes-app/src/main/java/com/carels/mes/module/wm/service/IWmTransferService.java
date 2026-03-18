package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmTransfer;

import java.util.List;

/**
 * 库存调拨Service接口
 */
public interface IWmTransferService {
    
    /**
     * 查询调拨列表
     */
    List<WmTransfer> selectWmTransferList(WmTransfer transfer);
    
    /**
     * 根据ID查询调拨单
     */
    WmTransfer selectWmTransferById(Long transferId);
    
    /**
     * 新增调拨单
     */
    int insertWmTransfer(WmTransfer transfer);
    
    /**
     * 修改调拨单
     */
    int updateWmTransfer(WmTransfer transfer);
    
    /**
     * 删除调拨单
     */
    int deleteWmTransferById(Long transferId);
    
    /**
     * 提交调拨单
     */
    void submitTransfer(Long transferId);
    
    /**
     * 出库确认
     */
    void outConfirm(Long transferId);
    
    /**
     * 入库确认
     */
    void inConfirm(Long transferId);
    
    /**
     * 取消调拨单
     */
    void cancelTransfer(Long transferId);
}
