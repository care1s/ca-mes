package com.carels.mes.module.cal.mapper;

import com.carels.mes.module.cal.domain.CalShift;

import java.util.List;

/**
 * 班次Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface CalShiftMapper {

    /**
     * 查询班次列表
     */
    List<CalShift> selectCalShiftList(CalShift calShift);

    /**
     * 查询所有启用的班次
     */
    List<CalShift> selectAllActiveShifts();

    /**
     * 根据ID查询
     */
    CalShift selectCalShiftById(Long shiftId);

    /**
     * 新增班次
     */
    int insertCalShift(CalShift calShift);

    /**
     * 修改班次
     */
    int updateCalShift(CalShift calShift);

    /**
     * 删除班次
     */
    int deleteCalShiftById(Long shiftId);

    /**
     * 批量删除
     */
    int deleteCalShiftByIds(Long[] shiftIds);

    /**
     * 检查编码是否唯一
     */
    int checkShiftCodeUnique(String shiftCode);
}
