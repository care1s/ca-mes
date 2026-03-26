package com.carels.mes.module.tm.service;

import com.carels.mes.module.tm.domain.TmToolBorrow;

import java.util.List;

/**
 * 工装机具领用Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ITmToolBorrowService {

    /**
     * 查询领用列表
     */
    List<TmToolBorrow> selectTmToolBorrowList(TmToolBorrow borrow);

    /**
     * 根据ID查询
     */
    TmToolBorrow selectTmToolBorrowById(Long borrowId);

    /**
     * 领用工装机具
     */
    int borrowTool(TmToolBorrow borrow);

    /**
     * 归还工装机具
     */
    int returnTool(Long borrowId, Integer usedCount);

    /**
     * 删除领用记录
     */
    int deleteTmToolBorrowById(Long borrowId);

    /**
     * 批量删除
     */
    int deleteTmToolBorrowByIds(Long[] borrowIds);
}
