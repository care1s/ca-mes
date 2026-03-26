package com.carels.mes.module.sale.service.impl;



import com.carels.mes.module.sale.domain.SaleReturn;
import com.carels.mes.module.sale.domain.SaleReturnLine;
import com.carels.mes.module.sale.mapper.SaleReturnMapper;
import com.carels.mes.module.sale.mapper.SaleReturnLineMapper;
import com.carels.mes.module.sale.service.ISaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 销售退货单Service实现类 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class SaleReturnServiceImpl implements ISaleReturnService {

    @Autowired
    private SaleReturnMapper returnMapper;

    @Autowired
    private SaleReturnLineMapper lineMapper;

    @Override
    public List<SaleReturn> selectSaleReturnList(SaleReturn saleReturn) {
        return returnMapper.selectSaleReturnList(saleReturn);
    }

    @Override
    public SaleReturn selectSaleReturnById(Long returnId) {
        SaleReturn saleReturn = returnMapper.selectSaleReturnById(returnId);
        if (saleReturn != null) {
            List<SaleReturnLine> lines = lineMapper.selectLinesByReturnId(returnId);
            saleReturn.setLines(lines);
        }
        return saleReturn;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSaleReturn(SaleReturn saleReturn) {
        saleReturn.setCreateTime(new Date());
        saleReturn.setCreateBy("admin");
        saleReturn.setStatus("0"); // 草稿状态
        saleReturn.setReturnNo(generateReturnNo());

        // 计算总金额和总数量
        calcTotal(saleReturn);

        int rows = returnMapper.insertSaleReturn(saleReturn);

        // 插入明细
        if (rows > 0 && saleReturn.getLines() != null && !saleReturn.getLines().isEmpty()) {
            for (SaleReturnLine line : saleReturn.getLines()) {
                line.setReturnId(saleReturn.getReturnId());
            }
            lineMapper.batchInsertLines(saleReturn.getLines());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSaleReturn(SaleReturn saleReturn) {
        saleReturn.setUpdateTime(new Date());
        saleReturn.setUpdateBy("admin");

        // 计算总金额和总数量
        calcTotal(saleReturn);

        int rows = returnMapper.updateSaleReturn(saleReturn);

        // 删除旧明细，插入新明细
        if (rows > 0 && saleReturn.getLines() != null) {
            lineMapper.deleteLinesByReturnId(saleReturn.getReturnId());
            for (SaleReturnLine line : saleReturn.getLines()) {
                line.setReturnId(saleReturn.getReturnId());
            }
            lineMapper.batchInsertLines(saleReturn.getLines());
        }
        return rows;
    }

    @Override
    public int submitReturn(Long returnId) {
        return returnMapper.updateStatus(returnId, "1"); // 已提交
    }

    @Override
    public int auditReturn(Long returnId) {
        return returnMapper.updateStatus(returnId, "2"); // 已审核
    }

    @Override
    public int receiveReturn(Long returnId) {
        return returnMapper.updateStatus(returnId, "3"); // 已入库
    }

    @Override
    public int deleteSaleReturnById(Long returnId) {
        lineMapper.deleteLinesByReturnId(returnId);
        return returnMapper.deleteSaleReturnById(returnId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSaleReturnByIds(Long[] returnIds) {
        for (Long returnId : returnIds) {
            lineMapper.deleteLinesByReturnId(returnId);
        }
        return returnMapper.deleteSaleReturnByIds(returnIds);
    }

    /**
     * 生成退货单号
     */
    private String generateReturnNo() {
        String prefix = "TH";
        String dateStr = new java.text.SimpleDateFormat("yyyyMMdd").format(new Date());
        String seq = String.format("%04d", (int)(Math.random() * 10000));
        return prefix + dateStr + seq;
    }

    /**
     * 计算总金额和总数量
     */
    private void calcTotal(SaleReturn saleReturn) {
        if (saleReturn.getLines() == null || saleReturn.getLines().isEmpty()) {
            return;
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (SaleReturnLine line : saleReturn.getLines()) {
            if (line.getQuantity() != null && line.getPrice() != null) {
                line.setAmount(line.getQuantity().multiply(line.getPrice()));
            }
            if (line.getAmount() != null) {
                totalAmount = totalAmount.add(line.getAmount());
            }
            if (line.getQuantity() != null) {
                totalQuantity = totalQuantity.add(line.getQuantity());
            }
        }
        saleReturn.setTotalAmount(totalAmount);
        saleReturn.setTotalQuantity(totalQuantity);
    }
}
