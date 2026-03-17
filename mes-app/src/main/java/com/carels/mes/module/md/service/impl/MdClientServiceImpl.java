package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdClient;
import com.carels.mes.module.md.mapper.MdClientMapper;
import com.carels.mes.module.md.service.IMdClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户管理Service实现 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Service
public class MdClientServiceImpl implements IMdClientService {

    @Autowired
    private MdClientMapper mdClientMapper;

    @Override
    public List<MdClient> selectMdClientList(MdClient mdClient) {
        return mdClientMapper.selectMdClientList(mdClient);
    }

    @Override
    public MdClient selectMdClientById(Long clientId) {
        return mdClientMapper.selectMdClientById(clientId);
    }

    @Override
    public MdClient selectMdClientByCode(String clientCode) {
        return mdClientMapper.selectMdClientByCode(clientCode);
    }

    @Override
    public List<MdClient> selectAllEnabledClients() {
        return mdClientMapper.selectAllEnabledClients();
    }

    @Override
    public int insertMdClient(MdClient mdClient) {
        return mdClientMapper.insertMdClient(mdClient);
    }

    @Override
    public int updateMdClient(MdClient mdClient) {
        return mdClientMapper.updateMdClient(mdClient);
    }

    @Override
    public int deleteMdClientById(Long clientId) {
        return mdClientMapper.deleteMdClientById(clientId);
    }

    @Override
    public int deleteMdClientByIds(Long[] clientIds) {
        return mdClientMapper.deleteMdClientByIds(clientIds);
    }

    @Override
    public boolean checkClientCodeUnique(String clientCode) {
        MdClient client = mdClientMapper.selectMdClientByCode(clientCode);
        return client == null;
    }
}
