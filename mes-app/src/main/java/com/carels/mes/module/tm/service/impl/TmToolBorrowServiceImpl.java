package com.carels.mes.module.tm.service.impl;



import com.carels.mes.module.tm.domain.TmTool;
import com.carels.mes.module.tm.domain.TmToolBorrow;
import com.carels.mes.module.tm.mapper.TmToolMapper;
import com.carels.mes.module.tm.mapper.TmToolBorrowMapper;
import com.carels.mes.module.tm.service.ITmToolBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工装机具领用Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class TmToolBorrowServiceImpl implements ITmToolBorrowService {

    @Autowired
    private TmToolBorrowMapper borrowMapper;

    @Autowired
    private TmToolMapper toolMapper;

    @Override
    public List<TmToolBorrow> selectTmToolBorrowList(TmToolBorrow borrow) {
        return borrowMapper.selectTmToolBorrowList(borrow);
    }

    @Override
    public TmToolBorrow selectTmToolBorrowById(Long borrowId) {
        return borrowMapper.selectTmToolBorrowById(borrowId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int borrowTool(TmToolBorrow borrow) {
        borrow.setCreateTime(new Date());
        borrow.setCreateBy("admin");
        borrow.setStatus("1"); // 已领用
        borrow.setBorrowNo(generateBorrowNo());

        // 更新工装机具状态
        toolMapper.updateStatus(borrow.getToolId(), "2"); // 维修中/使用中

        return borrowMapper.insertTmToolBorrow(borrow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int returnTool(Long borrowId, Integer usedCount) {
        TmToolBorrow borrow = borrowMapper.selectTmToolBorrowById(borrowId);
        if (borrow == null) {
            return 0;
        }

        // 更新领用记录
        borrow.setActualReturnDate(new Date());
        borrow.setUsedCount(usedCount);
        borrow.setStatus("2"); // 已归还
        borrow.setUpdateTime(new Date());
        borrow.setUpdateBy("admin");
        borrowMapper.updateTmToolBorrow(borrow);

        // 更新工装机具状态和使用寿命
        TmTool tool = toolMapper.selectTmToolById(borrow.getToolId());
        if (tool != null) {
            tool.setUsedLife((tool.getUsedLife() != null ? tool.getUsedLife() : 0) + usedCount);
            if (tool.getLifeLimit() != null) {
                tool.setRemainLife(tool.getLifeLimit() - tool.getUsedLife());
            }
            // 如果剩余寿命不足，标记为待报废
            if (tool.getRemainLife() != null && tool.getRemainLife() <= 0) {
                tool.setStatus("3"); // 报废
            } else {
                tool.setStatus("1"); // 可用
            }
            tool.setUpdateTime(new Date());
            tool.setUpdateBy("admin");
            toolMapper.updateTmTool(tool);
        }

        return 1;
    }

    @Override
    public int deleteTmToolBorrowById(Long borrowId) {
        return borrowMapper.deleteTmToolBorrowById(borrowId);
    }

    @Override
    public int deleteTmToolBorrowByIds(Long[] borrowIds) {
        return borrowMapper.deleteTmToolBorrowByIds(borrowIds);
    }

    /**
     * 生成领用单号
     */
    private String generateBorrowNo() {
        String prefix = "LY";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }
}
