package com.carels.mes.module.cost.service.impl;



import com.carels.mes.module.cost.domain.CostCalculation;
import com.carels.mes.module.cost.domain.CostCalculationLine;
import com.carels.mes.module.cost.mapper.CostCalculationMapper;
import com.carels.mes.module.cost.mapper.CostCalculationLineMapper;
import com.carels.mes.module.cost.service.ICostCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 成本核算Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class CostCalculationServiceImpl implements ICostCalculationService {

    @Autowired
    private CostCalculationMapper calculationMapper;

    @Autowired
    private CostCalculationLineMapper lineMapper;

    @Override
    public List<CostCalculation> selectCostCalculationList(CostCalculation calculation) {
        return calculationMapper.selectCostCalculationList(calculation);
    }

    @Override
    public CostCalculation selectCostCalculationById(Long calcId) {
        CostCalculation calculation = calculationMapper.selectCostCalculationById(calcId);
        if (calculation != null) {
            List<CostCalculationLine> lines = lineMapper.selectLinesByCalcId(calcId);
            calculation.setLines(lines);
        }
        return calculation;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertCostCalculation(CostCalculation calculation) {
        calculation.setCreateTime(new Date());
        calculation.setCreateBy("admin");
        calculation.setStatus("0"); // 草稿状态
        calculation.setCalcNo(generateCalcNo());
        return calculationMapper.insertCostCalculation(calculation);
    }

    @Override
    public int updateCostCalculation(CostCalculation calculation) {
        calculation.setUpdateTime(new Date());
        calculation.setUpdateBy("admin");
        return calculationMapper.updateCostCalculation(calculation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int calculateCost(Long calcId) {
        CostCalculation calculation = calculationMapper.selectCostCalculationById(calcId);
        if (calculation == null) {
            return 0;
        }

        // 删除旧明细
        lineMapper.deleteLinesByCalcId(calcId);

        // 模拟计算成本明细
        List<CostCalculationLine> lines = new ArrayList<>();

        // 直接材料
        CostCalculationLine materialLine = new CostCalculationLine();
        materialLine.setCalcId(calcId);
        materialLine.setItemId(1L);
        materialLine.setItemName("直接材料");
        materialLine.setCostType("1");
        materialLine.setAmount(calculation.getMaterialCost());
        materialLine.setAllocationRate(BigDecimal.ONE);
        materialLine.setAllocatedAmount(calculation.getMaterialCost());
        lines.add(materialLine);

        // 直接人工
        CostCalculationLine laborLine = new CostCalculationLine();
        laborLine.setCalcId(calcId);
        laborLine.setItemId(2L);
        laborLine.setItemName("直接人工");
        laborLine.setCostType("2");
        laborLine.setAmount(calculation.getLaborCost());
        laborLine.setAllocationRate(BigDecimal.ONE);
        laborLine.setAllocatedAmount(calculation.getLaborCost());
        lines.add(laborLine);

        // 制造费用
        CostCalculationLine overheadLine = new CostCalculationLine();
        overheadLine.setCalcId(calcId);
        overheadLine.setItemId(3L);
        overheadLine.setItemName("制造费用");
        overheadLine.setCostType("3");
        overheadLine.setAmount(calculation.getOverheadCost());
        overheadLine.setAllocationRate(BigDecimal.ONE);
        overheadLine.setAllocatedAmount(calculation.getOverheadCost());
        lines.add(overheadLine);

        // 插入明细
        if (!lines.isEmpty()) {
            lineMapper.batchInsertLines(lines);
        }

        // 计算总成本和单位成本
        BigDecimal totalCost = calculation.getMaterialCost()
                .add(calculation.getLaborCost())
                .add(calculation.getOverheadCost());
        BigDecimal unitCost = BigDecimal.ZERO;
        if (calculation.getCompletedQty() != null && calculation.getCompletedQty().compareTo(BigDecimal.ZERO) > 0) {
            unitCost = totalCost.divide(calculation.getCompletedQty(), 4, BigDecimal.ROUND_HALF_UP);
        }

        calculation.setTotalCost(totalCost);
        calculation.setUnitCost(unitCost);
        calculation.setStatus("1"); // 已核算
        calculation.setUpdateTime(new Date());
        calculation.setUpdateBy("admin");

        return calculationMapper.updateCostCalculation(calculation);
    }

    @Override
    public int carryForward(Long calcId) {
        return calculationMapper.updateStatus(calcId, "2"); // 已结转
    }

    @Override
    public int deleteCostCalculationById(Long calcId) {
        lineMapper.deleteLinesByCalcId(calcId);
        return calculationMapper.deleteCostCalculationById(calcId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCostCalculationByIds(Long[] calcIds) {
        for (Long calcId : calcIds) {
            lineMapper.deleteLinesByCalcId(calcId);
        }
        return calculationMapper.deleteCostCalculationByIds(calcIds);
    }

    /**
     * 生成核算单号
     */
    private String generateCalcNo() {
        String prefix = "HS";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }
}
