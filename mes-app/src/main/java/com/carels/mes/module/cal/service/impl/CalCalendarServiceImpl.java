package com.carels.mes.module.cal.service.impl;

import com.carels.mes.module.cal.domain.CalCalendar;
import com.carels.mes.module.cal.mapper.CalCalendarMapper;
import com.carels.mes.module.cal.service.ICalCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 日历Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Service
public class CalCalendarServiceImpl implements ICalCalendarService {

    @Autowired
    private CalCalendarMapper calCalendarMapper;

    @Override
    public List<CalCalendar> selectCalCalendarList(CalCalendar calCalendar) {
        return calCalendarMapper.selectCalCalendarList(calCalendar);
    }

    @Override
    public CalCalendar selectByDate(LocalDate calendarDate) {
        return calCalendarMapper.selectByDate(calendarDate);
    }

    @Override
    public int generateYearCalendar(Integer year) {
        // 删除该年度数据
        LocalDate beginDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        calCalendarMapper.deleteByDateRange(beginDate, endDate);

        // 生成全年日历
        List<CalCalendar> list = new ArrayList<>();
        LocalDate date = beginDate;
        while (!date.isAfter(endDate)) {
            CalCalendar calendar = new CalCalendar();
            calendar.setCalendarDate(date);
            calendar.setYear(date.getYear());
            calendar.setMonth(date.getMonthValue());
            calendar.setDay(date.getDayOfMonth());
            calendar.setWeekDay(date.getDayOfWeek().getValue());

            // 计算周数
            calendar.setWeekOfYear(date.get(java.time.temporal.WeekFields.of(java.util.Locale.getDefault()).weekOfWeekBasedYear()));

            // 周六日为非工作日
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            calendar.setIsWorkday(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY ? "N" : "Y");

            calendar.setStatus("0");
            list.add(calendar);
            date = date.plusDays(1);
        }

        return calCalendarMapper.batchInsert(list);
    }

    @Override
    public int updateCalCalendar(CalCalendar calCalendar) {
        return calCalendarMapper.updateCalCalendar(calCalendar);
    }

    @Override
    public int deleteCalCalendarById(Long calendarId) {
        return calCalendarMapper.deleteCalCalendarById(calendarId);
    }

    @Override
    public CalCalendar selectCalCalendarById(Long calendarId) {
        return calCalendarMapper.selectCalCalendarById(calendarId);
    }
}
