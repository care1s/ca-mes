package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurReceipt;

import java.util.List;

public interface IPurReceiptService {
    
    List<PurReceipt> selectPurReceiptList(PurReceipt purReceipt);
    
    PurReceipt selectPurReceiptById(Long receiptId);
    
    int insertPurReceipt(PurReceipt purReceipt);
    
    int updatePurReceipt(PurReceipt purReceipt);
    
    int deletePurReceiptById(Long receiptId);
}
