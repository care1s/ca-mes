package com.carels.mes.module.tm.mapper;

import com.carels.mes.module.tm.domain.TmToolBorrow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工装机具领用Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface TmToolBorrowMapper {

    /**
     * 查询领用列表
     */
    List<TmToolBorrow> selectTmToolBorrowList(TmToolBorrow borrow);

    /**
     * 根据ID查询
     */
    TmToolBorrow selectTmToolBorrowById(Long borrowId);

    /**
     * 新增领用记录
     */
    int insertTmToolBorrow(TmToolBorrow borrow);

    /**
     * 修改领用记录
     */
    int updateTmToolBorrow(TmToolBorrow borrow);

    /**
     * 归还
     */
    int returnTool(@Param("borrowId") Long borrowId, @Param("status") String status);

    /**
     * 删除领用记录
     */
    int deleteTmToolBorrowById(Long borrowId);

    /**
     * 批量删除
     */
    int deleteTmToolBorrowByIds(Long[] borrowIds);
}
