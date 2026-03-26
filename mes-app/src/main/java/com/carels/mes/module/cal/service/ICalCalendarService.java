package com.carels.mes.module.cal.service;

import com.carels.mes.module.cal.domain.CalCalendar;

import java.time.LocalDate;
import java.util.List;

/**
 * 日历Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface ICalCalendarService {

    /**
     * 查询日历列表
     */
    List<CalCalendar> selectCalCalendarList(CalCalendar calCalendar);

    /**
     * 根据日期查询
     */
    CalCalendar selectByDate(LocalDate calendarDate);

    /**
     * 生成年度日历
     */
    int generateYearCalendar(Integer year);

    /**
     * 修改日历
     */
    int updateCalCalendar(CalCalendar calCalendar);

    /**
     * 删除日历
     */
    int deleteCalCalendarById(Long calendarId);

    /**
     * 根据ID查询
     */
    CalCalendar selectCalCalendarById(Long calendarId);
}
