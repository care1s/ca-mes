package com.carels.mes.module.pur.service;

import com.carels.mes.module.pur.domain.PurRequest;
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
}
