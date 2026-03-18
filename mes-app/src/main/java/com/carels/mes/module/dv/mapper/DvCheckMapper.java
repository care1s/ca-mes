package com.carels.mes.module.dv.mapper;

import com.carels.mes.module.dv.domain.DvCheck;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface DvCheckMapper {
    List<DvCheck> selectDvCheckList(DvCheck check);
    DvCheck selectDvCheckById(Long id);
    int insertDvCheck(DvCheck check);
    int updateDvCheck(DvCheck check);
    int deleteDvCheckById(Long id);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
}
