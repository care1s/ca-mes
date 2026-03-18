package com.carels.mes.module.qc.service;

import com.carels.mes.module.qc.domain.QcOqc;

import java.util.List;

public interface IQcOqcService {
    List<QcOqc> selectQcOqcList(QcOqc oqc);
    QcOqc selectQcOqcById(Long id);
    int insertQcOqc(QcOqc oqc);
    int updateQcOqc(QcOqc oqc);
    int deleteQcOqcById(Long id);
    void updateStatus(Long id, String status);
}
