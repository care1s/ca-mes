package com.carels.mes.module.qc.service;

import com.carels.mes.module.qc.domain.QcDefect;

import java.util.List;

public interface IQcDefectService {
    List<QcDefect> selectQcDefectList(QcDefect defect);
    QcDefect selectQcDefectById(Long defectId);
    int insertQcDefect(QcDefect defect);
    int updateQcDefect(QcDefect defect);
    int deleteQcDefectById(Long defectId);
    void updateStatus(Long defectId, Integer status);
}
