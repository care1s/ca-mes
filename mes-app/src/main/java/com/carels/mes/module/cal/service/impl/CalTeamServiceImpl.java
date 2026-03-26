package com.carels.mes.module.cal.service.impl;

import com.carels.mes.module.cal.domain.CalTeam;
import com.carels.mes.module.cal.mapper.CalTeamMapper;
import com.carels.mes.module.cal.service.ICalTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班组Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@Service
public class CalTeamServiceImpl implements ICalTeamService {

    @Autowired
    private CalTeamMapper calTeamMapper;

    @Override
    public List<CalTeam> selectCalTeamList(CalTeam calTeam) {
        return calTeamMapper.selectCalTeamList(calTeam);
    }

    @Override
    public List<CalTeam> selectAllActiveTeams() {
        return calTeamMapper.selectAllActiveTeams();
    }

    @Override
    public CalTeam selectCalTeamById(Long teamId) {
        return calTeamMapper.selectCalTeamById(teamId);
    }

    @Override
    public int insertCalTeam(CalTeam calTeam) {
        return calTeamMapper.insertCalTeam(calTeam);
    }

    @Override
    public int updateCalTeam(CalTeam calTeam) {
        return calTeamMapper.updateCalTeam(calTeam);
    }

    @Override
    public int deleteCalTeamById(Long teamId) {
        return calTeamMapper.deleteCalTeamById(teamId);
    }

    @Override
    public int deleteCalTeamByIds(Long[] teamIds) {
        return calTeamMapper.deleteCalTeamByIds(teamIds);
    }

    @Override
    public boolean checkTeamCodeUnique(String teamCode) {
        return calTeamMapper.checkTeamCodeUnique(teamCode) == 0;
    }
}
