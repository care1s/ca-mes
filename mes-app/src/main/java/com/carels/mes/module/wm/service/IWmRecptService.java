package com.carels.mes.module.wm.service;

import com.carels.mes.module.wm.domain.WmRecpt;

import java.util.List;

/**
 * 入库管理Service接口
 */
public interface IWmRecptService {

    List<WmRecpt> selectWmRecptList(WmRecpt recpt);

    WmRecpt selectWmRecptById(Long recptId);

    int insertWmRecpt(WmRecpt recpt);

    int updateWmRecpt(WmRecpt recpt);

    int deleteWmRecptById(Long recptId);

    void updateStatus(Long recptId, String status);

    void confirmRecpt(Long recptId);
}
