package com.carels.mes.module.cal.mapper;

import com.carels.mes.module.cal.domain.CalCalendar;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 日历Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface CalCalendarMapper {

    /**
     * 查询日历列表
     */
    List<CalCalendar> selectCalCalendarList(CalCalendar calCalendar);

    /**
     * 根据日期查询
     */
    CalCalendar selectByDate(@Param("calendarDate") LocalDate calendarDate);

    /**
     * 批量插入日历
     */
    int batchInsert(List<CalCalendar> list);

    /**
     * 修改日历
     */
    int updateCalCalendar(CalCalendar calCalendar);

    /**
     * 删除日历
     */
    int deleteCalCalendarById(Long calendarId);

    /**
     * 根据日期范围删除
     */
    int deleteByDateRange(@Param("beginDate") LocalDate beginDate, @Param("endDate") LocalDate endDate);

    /**
     * 根据ID查询
     */
    CalCalendar selectCalCalendarById(Long calendarId);
}
