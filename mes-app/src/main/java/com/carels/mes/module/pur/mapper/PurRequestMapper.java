package com.carels.mes.module.pur.mapper;

import com.carels.mes.module.pur.domain.PurRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购申请Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface PurRequestMapper {

    /**
     * 查询采购申请列表
     */
    List<PurRequest> selectPurRequestList(PurRequest purRequest);

    /**
     * 根据ID查询采购申请
     */
    PurRequest selectPurRequestById(Long requestId);

    /**
     * 根据单号查询采购申请
     */
    PurRequest selectPurRequestByCode(String requestCode);

    /**
     * 查询待审批的采购申请
     */
    List<PurRequest> selectPendingRequests();

    /**
     * 新增采购申请
     */
    int insertPurRequest(PurRequest purRequest);

    /**
     * 修改采购申请
     */
    int updatePurRequest(PurRequest purRequest);

    /**
     * 删除采购申请
     */
    int deletePurRequestById(Long requestId);

    /**
     * 批量删除采购申请
     */
    int deletePurRequestByIds(Long[] requestIds);

    /**
     * 更新申请状态
     */
    int updateRequestStatus(@Param("requestId") Long requestId, @Param("status") String status);

    /**
     * 查询当天最大单号
     */
    String selectMaxRequestCodeByDate(@Param("dateStr") String dateStr);
}
