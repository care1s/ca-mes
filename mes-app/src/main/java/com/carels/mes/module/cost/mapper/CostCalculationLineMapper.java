package com.carels.mes.module.cost.mapper;

import com.carels.mes.module.cost.domain.CostCalculationLine;

import java.util.List;

/**
 * 成本核算明细Mapper接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface CostCalculationLineMapper {

    /**
     * 根据核算单ID查询明细
     */
    List<CostCalculationLine> selectLinesByCalcId(Long calcId);

    /**
     * 批量插入明细
     */
    int batchInsertLines(List<CostCalculationLine> lines);

    /**
     * 删除明细
     */
    int deleteLinesByCalcId(Long calcId);
}
