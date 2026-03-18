package com.carels.mes.module.dv.service;

import com.carels.mes.module.dv.domain.DvMachinery;
import java.util.List;

public interface IDvMachineryService {
    List<DvMachinery> selectDvMachineryList(DvMachinery machinery);
    DvMachinery selectDvMachineryById(Long id);
    int insertDvMachinery(DvMachinery machinery);
    int updateDvMachinery(DvMachinery machinery);
    int deleteDvMachineryById(Long id);
    void updateStatus(Long id, String status);
}
