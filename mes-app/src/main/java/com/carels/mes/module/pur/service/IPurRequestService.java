package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurRequest;
import com.carels.mes.module.pur.domain.PurRequestItem;
import java.util.List;

/**
 * 采购申请Service接口
 */
public interface IPurRequestService {
    List<PurRequest> selectPurRequestList(PurRequest purRequest);
    PurRequest selectPurRequestById(Long requestId);
    int insertPurRequest(PurRequest purRequest);
    int updatePurRequest(PurRequest purRequest);
    int deletePurRequestById(Long requestId);

    // 明细相关方法
    List<PurRequestItem> selectPurRequestItems(Long requestId);
    int insertPurRequestItem(PurRequestItem item);
    int deletePurRequestItemsByRequestId(Long requestId);

    // 审批相关方法
    int submitPurRequest(Long requestId);
    int approvePurRequest(Long requestId, String auditOpinion);
    int rejectPurRequest(Long requestId, String auditOpinion);
}
