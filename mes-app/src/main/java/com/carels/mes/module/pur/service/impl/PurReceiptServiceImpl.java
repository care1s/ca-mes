package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurReceipt;
import com.carels.mes.module.pur.mapper.PurReceiptMapper;
import com.carels.mes.module.pur.service.IPurReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurReceiptServiceImpl implements IPurReceiptService {
    
    @Autowired
    private PurReceiptMapper purReceiptMapper;
    
    @Override
    public List<PurReceipt> selectPurReceiptList(PurReceipt purReceipt) {
        return purReceiptMapper.selectPurReceiptList(purReceipt);
    }
    
    @Override
    public PurReceipt selectPurReceiptById(Long receiptId) {
        return purReceiptMapper.selectPurReceiptById(receiptId);
    }
    
    @Override
    public int insertPurReceipt(PurReceipt purReceipt) {
        purReceipt.setStatus("PENDING");
        return purReceiptMapper.insertPurReceipt(purReceipt);
    }
    
    @Override
    public int updatePurReceipt(PurReceipt purReceipt) {
        return purReceiptMapper.updatePurReceipt(purReceipt);
    }
    
    @Override
    public int deletePurReceiptById(Long receiptId) {
        return purReceiptMapper.deletePurReceiptById(receiptId);
    }
}
