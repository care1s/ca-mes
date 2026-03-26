package com.carels.mes.module.cal.service;

import com.carels.mes.module.cal.domain.CalTeam;

import java.util.List;

/**
 * 班组Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
public interface ICalTeamService {

    /**
     * 查询班组列表
     */
    List<CalTeam> selectCalTeamList(CalTeam calTeam);

    /**
     * 查询所有启用的班组
     */
    List<CalTeam> selectAllActiveTeams();

    /**
     * 根据ID查询
     */
    CalTeam selectCalTeamById(Long teamId);

    /**
     * 新增班组
     */
    int insertCalTeam(CalTeam calTeam);

    /**
     * 修改班组
     */
    int updateCalTeam(CalTeam calTeam);

    /**
     * 删除班组
     */
    int deleteCalTeamById(Long teamId);

    /**
     * 批量删除
     */
    int deleteCalTeamByIds(Long[] teamIds);

    /**
     * 检查编码是否唯一
     */
    boolean checkTeamCodeUnique(String teamCode);
}
