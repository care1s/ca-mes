package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurRequest;
import com.carels.mes.module.pur.domain.PurRequestItem;
import com.carels.mes.module.pur.mapper.PurRequestMapper;
import com.carels.mes.module.pur.mapper.PurRequestItemMapper;
import com.carels.mes.module.pur.service.IPurRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PurRequestServiceImpl implements IPurRequestService {

    @Autowired
    private PurRequestMapper purRequestMapper;

    @Autowired
    private PurRequestItemMapper purRequestItemMapper;

    @Override
    public List<PurRequest> selectPurRequestList(PurRequest purRequest) {
        return purRequestMapper.selectPurRequestList(purRequest);
    }

    @Override
    public PurRequest selectPurRequestById(Long requestId) {
        PurRequest request = purRequestMapper.selectPurRequestById(requestId);
        if (request != null) {
            // 查询明细
            List<PurRequestItem> items = purRequestItemMapper.selectPurRequestItemsByRequestId(requestId);
            request.setItems(items);
        }
        return request;
    }

    @Override
    @Transactional
    public int insertPurRequest(PurRequest purRequest) {
        // 自动生成申请单号（如果为空）
        if (purRequest.getRequestCode() == null || purRequest.getRequestCode().isEmpty()) {
            String requestCode = generateRequestCode();
            purRequest.setRequestCode(requestCode);
        }
        // 设置默认状态
        if (purRequest.getStatus() == null || purRequest.getStatus().isEmpty()) {
            purRequest.setStatus("DRAFT");
        }
        // 计算总金额
        calculateTotalAmount(purRequest);
        // 插入主表
        int result = purRequestMapper.insertPurRequest(purRequest);
        // 插入明细
        if (purRequest.getItems() != null && !purRequest.getItems().isEmpty()) {
            for (PurRequestItem item : purRequest.getItems()) {
                item.setRequestId(purRequest.getRequestId());
                // 计算明细金额
                if (item.getQuantity() != null && item.getPrice() != null) {
                    item.setAmount(item.getQuantity().multiply(item.getPrice()));
                }
                purRequestItemMapper.insertPurRequestItem(item);
            }
        }
        return result;
    }

    /**
     * 生成申请单号：PRQ + 年月日 + 3位序号
     */
    private String generateRequestCode() {
        String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 查询当天最大单号
        String maxCode = purRequestMapper.selectMaxRequestCodeByDate(dateStr);
        int seq = 1;
        if (maxCode != null && maxCode.length() > 11) {
            try {
                String seqStr = maxCode.substring(maxCode.length() - 3);
                seq = Integer.parseInt(seqStr) + 1;
            } catch (NumberFormatException e) {
                seq = 1;
            }
        }
        return String.format("PRQ%s%03d", dateStr, seq);
    }

    @Override
    @Transactional
    public int updatePurRequest(PurRequest purRequest) {
        // 计算总金额
        calculateTotalAmount(purRequest);
        // 更新主表
        int result = purRequestMapper.updatePurRequest(purRequest);
        // 删除旧明细
        purRequestItemMapper.deletePurRequestItemsByRequestId(purRequest.getRequestId());
        // 插入新明细
        if (purRequest.getItems() != null && !purRequest.getItems().isEmpty()) {
            for (PurRequestItem item : purRequest.getItems()) {
                item.setRequestId(purRequest.getRequestId());
                // 计算明细金额
                if (item.getQuantity() != null && item.getPrice() != null) {
                    item.setAmount(item.getQuantity().multiply(item.getPrice()));
                }
                purRequestItemMapper.insertPurRequestItem(item);
            }
        }
        return result;
    }

    @Override
    @Transactional
    public int deletePurRequestById(Long requestId) {
        // 删除明细
        purRequestItemMapper.deletePurRequestItemsByRequestId(requestId);
        // 删除主表
        return purRequestMapper.deletePurRequestById(requestId);
    }

    @Override
    public List<PurRequestItem> selectPurRequestItems(Long requestId) {
        return purRequestItemMapper.selectPurRequestItemsByRequestId(requestId);
    }

    @Override
    public int insertPurRequestItem(PurRequestItem item) {
        return purRequestItemMapper.insertPurRequestItem(item);
    }

    @Override
    public int deletePurRequestItemsByRequestId(Long requestId) {
        return purRequestItemMapper.deletePurRequestItemsByRequestId(requestId);
    }

    @Override
    public int submitPurRequest(Long requestId) {
        return purRequestMapper.updateRequestStatus(requestId, "PENDING");
    }

    @Override
    public int approvePurRequest(Long requestId, String auditOpinion) {
        return purRequestMapper.updateRequestStatus(requestId, "APPROVED");
    }

    @Override
    public int rejectPurRequest(Long requestId, String auditOpinion) {
        return purRequestMapper.updateRequestStatus(requestId, "REJECTED");
    }

    /**
     * 计算总金额
     */
    private void calculateTotalAmount(PurRequest purRequest) {
        if (purRequest.getItems() != null && !purRequest.getItems().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PurRequestItem item : purRequest.getItems()) {
                if (item.getQuantity() != null && item.getPrice() != null) {
                    BigDecimal amount = item.getQuantity().multiply(item.getPrice());
                    totalAmount = totalAmount.add(amount);
                }
            }
            purRequest.setTotalAmount(totalAmount);
        }
    }
}
