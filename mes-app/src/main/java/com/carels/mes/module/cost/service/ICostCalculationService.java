package com.carels.mes.module.cost.service;

import com.carels.mes.module.cost.domain.CostCalculation;

import java.util.List;

/**
 * 成本核算Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface ICostCalculationService {

    /**
     * 查询成本核算列表
     */
    List<CostCalculation> selectCostCalculationList(CostCalculation calculation);

    /**
     * 根据ID查询成本核算
     */
    CostCalculation selectCostCalculationById(Long calcId);

    /**
     * 新增成本核算
     */
    int insertCostCalculation(CostCalculation calculation);

    /**
     * 修改成本核算
     */
    int updateCostCalculation(CostCalculation calculation);

    /**
     * 成本核算计算
     */
    int calculateCost(Long calcId);

    /**
     * 成本结转
     */
    int carryForward(Long calcId);

    /**
     * 删除成本核算
     */
    int deleteCostCalculationById(Long calcId);

    /**
     * 批量删除成本核算
     */
    int deleteCostCalculationByIds(Long[] calcIds);
}
