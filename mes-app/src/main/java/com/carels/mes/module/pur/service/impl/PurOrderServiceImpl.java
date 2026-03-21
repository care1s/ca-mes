package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurOrder;
import com.carels.mes.module.pur.domain.PurOrderItem;
import com.carels.mes.module.pur.domain.PurRequest;
import com.carels.mes.module.pur.domain.PurRequestItem;
import com.carels.mes.module.pur.mapper.PurOrderMapper;
import com.carels.mes.module.pur.mapper.PurOrderItemMapper;
import com.carels.mes.module.pur.service.IPurOrderService;
import com.carels.mes.module.pur.service.IPurRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PurOrderServiceImpl implements IPurOrderService {

    @Autowired
    private PurOrderMapper purOrderMapper;

    @Autowired
    private PurOrderItemMapper purOrderItemMapper;

    @Autowired
    private IPurRequestService purRequestService;

    @Override
    public List<PurOrder> selectPurOrderList(PurOrder purOrder) {
        return purOrderMapper.selectPurOrderList(purOrder);
    }

    @Override
    public PurOrder selectPurOrderById(Long orderId) {
        return purOrderMapper.selectPurOrderById(orderId);
    }

    @Override
    public int insertPurOrder(PurOrder purOrder) {
        purOrder.setOrderStatus("DRAFT");
        return purOrderMapper.insertPurOrder(purOrder);
    }

    @Override
    public int updatePurOrder(PurOrder purOrder) {
        return purOrderMapper.updatePurOrder(purOrder);
    }

    @Override
    public int deletePurOrderById(Long orderId) {
        return purOrderMapper.deletePurOrderById(orderId);
    }

    @Override
    @Transactional
    public Long createOrderFromRequest(Long requestId) {
        // 1. 获取采购申请信息
        PurRequest request = purRequestService.selectPurRequestById(requestId);
        if (request == null) {
            throw new RuntimeException("采购申请不存在");
        }

        // 2. 检查申请状态
        if (!"APPROVED".equals(request.getStatus())) {
            throw new RuntimeException("只有已审批通过的采购申请才能生成订单");
        }

        // 3. 获取申请明细
        List<PurRequestItem> requestItems = request.getItems();
        if (requestItems == null || requestItems.isEmpty()) {
            throw new RuntimeException("采购申请没有明细，无法生成订单");
        }

        // 4. 创建采购订单
        PurOrder order = new PurOrder();
        order.setRequestId(request.getRequestId());
        order.setRequestNo(request.getRequestCode());
        // 供应商信息需要用户后续填写，这里可以先留空或从申请中获取（如果有的话）
        order.setOrderDate(new Date());
        order.setTotalAmount(request.getTotalAmount());
        order.setCurrency(request.getCurrency());
        order.setOrderStatus("DRAFT");
        order.setAuditStatus("PENDING");
        order.setRemark("由采购申请【" + request.getRequestCode() + "】生成");

        // 5. 插入订单主表
        purOrderMapper.insertPurOrder(order);
        Long orderId = order.getId();

        // 6. 复制明细到订单
        BigDecimal totalQuantity = BigDecimal.ZERO;
        for (PurRequestItem reqItem : requestItems) {
            PurOrderItem orderItem = new PurOrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setItemCode(reqItem.getItemCode());
            orderItem.setItemName(reqItem.getItemName());
            orderItem.setItemSpec(reqItem.getItemSpec());
            orderItem.setUnit(reqItem.getUnit());
            orderItem.setQuantity(reqItem.getQuantity());
            orderItem.setPrice(reqItem.getPrice());
            orderItem.setAmount(reqItem.getAmount());
            orderItem.setRemark(reqItem.getRemark());

            purOrderItemMapper.insertPurOrderItem(orderItem);

            if (reqItem.getQuantity() != null) {
                totalQuantity = totalQuantity.add(reqItem.getQuantity());
            }
        }

        // 7. 更新订单总数量
        order.setTotalQuantity(totalQuantity);
        purOrderMapper.updatePurOrder(order);

        return orderId;
    }
}
