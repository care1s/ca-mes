package com.carels.mes.module.cal.controller;

import com.carels.mes.common.core.domain.AjaxResult;
import com.carels.mes.common.core.page.TableDataInfo;
import com.carels.mes.common.core.web.controller.BaseController;
import com.carels.mes.module.cal.domain.CalTeam;
import com.carels.mes.module.cal.service.ICalTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 班组管理Controller - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 */
@RestController
@RequestMapping("/mes/cal/team")
public class CalTeamController extends BaseController {

    @Autowired
    private ICalTeamService calTeamService;

    /**
     * 查询班组列表
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "20") Integer pageSize,
                              CalTeam calTeam) {
        startPage(pageNum, pageSize);
        List<CalTeam> list = calTeamService.selectCalTeamList(calTeam);
        return getDataTable(list);
    }

    /**
     * 查询所有启用的班组
     */
    @GetMapping("/all")
    public AjaxResult all() {
        return AjaxResult.success(calTeamService.selectAllActiveTeams());
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{teamId}")
    public AjaxResult getInfo(@PathVariable Long teamId) {
        return AjaxResult.success(calTeamService.selectCalTeamById(teamId));
    }

    /**
     * 新增班组
     */
    @PostMapping
    public AjaxResult add(@RequestBody CalTeam calTeam) {
        if (!calTeamService.checkTeamCodeUnique(calTeam.getTeamCode())) {
            return AjaxResult.error("新增班组'" + calTeam.getTeamCode() + "'失败，班组编码已存在");
        }
        return toAjax(calTeamService.insertCalTeam(calTeam));
    }

    /**
     * 修改班组
     */
    @PutMapping
    public AjaxResult edit(@RequestBody CalTeam calTeam) {
        return toAjax(calTeamService.updateCalTeam(calTeam));
    }

    /**
     * 删除班组
     */
    @DeleteMapping("/{teamId}")
    public AjaxResult remove(@PathVariable Long teamId) {
        return toAjax(calTeamService.deleteCalTeamById(teamId));
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/batch/{teamIds}")
    public AjaxResult removeBatch(@PathVariable Long[] teamIds) {
        return toAjax(calTeamService.deleteCalTeamByIds(teamIds));
    }
}
