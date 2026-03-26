package com.carels.mes.module.tm.controller;

import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.module.tm.domain.TmToolBorrow;
import com.carels.mes.module.tm.service.ITmToolBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工装机具领用Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/mes/tm/borrow")
public class TmToolBorrowController extends BaseController {

    @Autowired
    private ITmToolBorrowService borrowService;

    @GetMapping("/list")
    public TableDataInfo list(TmToolBorrow borrow) {
        startPage();
        List<TmToolBorrow> list = borrowService.selectTmToolBorrowList(borrow);
        return getDataTable(list);
    }

    @GetMapping("/{borrowId}")
    public AjaxResult getInfo(@PathVariable Long borrowId) {
        return AjaxResult.success(borrowService.selectTmToolBorrowById(borrowId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody TmToolBorrow borrow) {
        return toAjax(borrowService.borrowTool(borrow));
    }

    @PutMapping("/{borrowId}/return")
    public AjaxResult returnTool(@PathVariable Long borrowId, @RequestParam Integer usedCount) {
        return toAjax(borrowService.returnTool(borrowId, usedCount));
    }

    @DeleteMapping("/{borrowIds}")
    public AjaxResult remove(@PathVariable Long[] borrowIds) {
        return toAjax(borrowService.deleteTmToolBorrowByIds(borrowIds));
    }
}
