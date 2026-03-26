package com.carels.mes.module.pro.service;

import com.carels.mes.module.pro.domain.ProAndonType;
import java.util.List;

/**
 * 安东异常类型Service接口 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
public interface IProAndonTypeService {

    /**
     * 查询异常类型列表
     */
    List<ProAndonType> selectProAndonTypeList(ProAndonType type);

    /**
     * 根据ID查询异常类型
     */
    ProAndonType selectProAndonTypeById(Long typeId);

    /**
     * 新增异常类型
     */
    int insertProAndonType(ProAndonType type);

    /**
     * 修改异常类型
     */
    int updateProAndonType(ProAndonType type);

    /**
     * 删除异常类型
     */
    int deleteProAndonTypeById(Long typeId);
}
