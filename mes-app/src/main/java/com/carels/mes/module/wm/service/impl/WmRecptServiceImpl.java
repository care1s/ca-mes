package com.carels.mes.module.wm.service.impl;

import com.carels.mes.module.wm.domain.WmRecpt;
import com.carels.mes.module.wm.domain.WmRecptItem;
import com.carels.mes.module.wm.mapper.WmRecptMapper;
import com.carels.mes.module.wm.service.IWmRecptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class WmRecptServiceImpl implements IWmRecptService {

    @Autowired
    private WmRecptMapper recptMapper;

    @Override
    public List<WmRecpt> selectWmRecptList(WmRecpt recpt) {
        return recptMapper.selectWmRecptList(recpt);
    }

    @Override
    public WmRecpt selectWmRecptById(Long recptId) {
        return recptMapper.selectWmRecptById(recptId);
    }

    @Override
    @Transactional
    public int insertWmRecpt(WmRecpt recpt) {
        // 生成入库单号
        if (recpt.getRecptNo() == null || recpt.getRecptNo().isEmpty()) {
            recpt.setRecptNo("RECPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        // 设置默认状态
        if (recpt.getStatus() == null || recpt.getStatus().isEmpty()) {
            recpt.setStatus("PENDING");
        }
        int rows = recptMapper.insertWmRecpt(recpt);
        // 插入明细
        if (recpt.getItems() != null && !recpt.getItems().isEmpty()) {
            for (WmRecptItem item : recpt.getItems()) {
                item.setRecptId(recpt.getId());
                recptMapper.insertWmRecptItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateWmRecpt(WmRecpt recpt) {
        int rows = recptMapper.updateWmRecpt(recpt);
        // 更新明细：先删除再插入
        if (recpt.getItems() != null) {
            recptMapper.deleteWmRecptItemsByRecptId(recpt.getId());
            for (WmRecptItem item : recpt.getItems()) {
                item.setRecptId(recpt.getId());
                recptMapper.insertWmRecptItem(item);
            }
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteWmRecptById(Long recptId) {
        // 先删除明细
        recptMapper.deleteWmRecptItemsByRecptId(recptId);
        return recptMapper.deleteWmRecptById(recptId);
    }

    @Override
    public void updateStatus(Long recptId, String status) {
        recptMapper.updateStatus(recptId, status);
    }

    @Override
    public void confirmRecpt(Long recptId) {
        recptMapper.updateStatus(recptId, "CONFIRMED");
    }
}
