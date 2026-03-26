package com.carels.mes.module.pro.service.impl;

import com.carels.mes.module.pro.domain.ProAndonType;
import com.carels.mes.module.pro.mapper.ProAndonTypeMapper;
import com.carels.mes.module.pro.service.IProAndonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 安东异常类型Service实现 - carels
 *
 * @author carels
 * @version V9.0
 * @date 2026-03-25
 */
@Service
public class ProAndonTypeServiceImpl implements IProAndonTypeService {

    @Autowired
    private ProAndonTypeMapper andonTypeMapper;

    @Override
    public List<ProAndonType> selectProAndonTypeList(ProAndonType type) {
        return andonTypeMapper.selectProAndonTypeList(type);
    }

    @Override
    public ProAndonType selectProAndonTypeById(Long typeId) {
        return andonTypeMapper.selectProAndonTypeById(typeId);
    }

    @Override
    public int insertProAndonType(ProAndonType type) {
        type.setStatus("0");
        return andonTypeMapper.insertProAndonType(type);
    }

    @Override
    public int updateProAndonType(ProAndonType type) {
        return andonTypeMapper.updateProAndonType(type);
    }

    @Override
    public int deleteProAndonTypeById(Long typeId) {
        return andonTypeMapper.deleteProAndonTypeById(typeId);
    }
}
