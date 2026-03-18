package com.carels.mes.module.dv.mapper;

import com.carels.mes.module.dv.domain.DvMachinery;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DvMachineryMapper {
    List<DvMachinery> selectDvMachineryList(DvMachinery machinery);
    DvMachinery selectDvMachineryById(Long id);
    int insertDvMachinery(DvMachinery machinery);
    int updateDvMachinery(DvMachinery machinery);
    int deleteDvMachineryById(Long id);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
