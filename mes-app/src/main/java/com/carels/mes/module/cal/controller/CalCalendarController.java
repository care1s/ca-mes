package com.carels.mes.module.cal.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.cal.domain.CalCalendar;
import com.carels.mes.module.cal.service.ICalCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 日历管理Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/mes/cal/calendar")
public class CalCalendarController extends BaseController {

    @Autowired
    private ICalCalendarService calCalendarService;

    /**
     * 查询日历列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              CalCalendar calCalendar) {
        startPage(pageNum, pageSize);
        List<CalCalendar> list = calCalendarService.selectCalCalendarList(calCalendar);
        return getDataTable(list);
    }

    /**
     * 根据日期查询
     */
    @GetMapping("/date/{calendarDate}")
    public AjaxResult getByDate(@PathVariable String calendarDate) {
        return AjaxResult.success(calCalendarService.selectByDate(LocalDate.parse(calendarDate)));
    }

    /**
     * 生成年度日历
     */
    @PostMapping("/generate/{year}")
    public AjaxResult generate(@PathVariable Integer year) {
        return toAjax(calCalendarService.generateYearCalendar(year));
    }

    /**
     * 修改日历
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CalCalendar calCalendar) {
        return toAjax(calCalendarService.updateCalCalendar(calCalendar));
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{calendarId}")
    public AjaxResult getInfo(@PathVariable Long calendarId) {
        return AjaxResult.success(calCalendarService.selectCalCalendarById(calendarId));
    }
}
