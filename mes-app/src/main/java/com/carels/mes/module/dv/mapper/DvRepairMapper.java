package com.carels.mes.module.dv.mapper;

import com.carels.mes.module.dv.domain.DvRepair;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DvRepairMapper {
    List<DvRepair> selectDvRepairList(DvRepair repair);
    DvRepair selectDvRepairById(Long id);
    int insertDvRepair(DvRepair repair);
    int updateDvRepair(DvRepair repair);
    int deleteDvRepairById(Long id);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
