package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmTransfer;
import com.carels.mes.module.wm.mapper.WmTransferMapper;
import com.carels.mes.module.wm.service.IWmTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存调拨Service实现
 */
@Service
public class WmTransferServiceImpl implements IWmTransferService {
    
    @Autowired
    private WmTransferMapper transferMapper;
    
    @Override
    public List<WmTransfer> selectWmTransferList(WmTransfer transfer) {
        return transferMapper.selectWmTransferList(transfer);
    }
    
    @Override
    public WmTransfer selectWmTransferById(Long transferId) {
        return transferMapper.selectWmTransferById(transferId);
    }
    
    @Override
    public int insertWmTransfer(WmTransfer transfer) {
        transfer.setStatus(0);
        transfer.setCreateTime(LocalDateTime.now());
        transfer.setUpdateTime(LocalDateTime.now());
        return transferMapper.insertWmTransfer(transfer);
    }
    
    @Override
    public int updateWmTransfer(WmTransfer transfer) {
        transfer.setUpdateTime(LocalDateTime.now());
        return transferMapper.updateWmTransfer(transfer);
    }
    
    @Override
    public int deleteWmTransferById(Long transferId) {
        return transferMapper.deleteWmTransferById(transferId);
    }
    
    @Override
    public void submitTransfer(Long transferId) {
        transferMapper.updateStatus(transferId, 1);
    }
    
    @Override
    public void outConfirm(Long transferId) {
        transferMapper.updateStatus(transferId, 2);
    }
    
    @Override
    public void inConfirm(Long transferId) {
        transferMapper.updateStatus(transferId, 3);
    }
    
    @Override
    public void cancelTransfer(Long transferId) {
        transferMapper.updateStatus(transferId, 4);
    }
}
