package com.carels.mes.module.pur.service.impl;

import com.carels.mes.module.pur.domain.PurRequest;
import com.carels.mes.module.pur.mapper.PurRequestMapper;
import com.carels.mes.module.pur.service.IPurRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PurRequestServiceImpl implements IPurRequestService {
    
    @Autowired
    private PurRequestMapper purRequestMapper;
    
    @Override
    public List<PurRequest> selectPurRequestList(PurRequest purRequest) {
        return purRequestMapper.selectPurRequestList(purRequest);
    }
    
    @Override
    public PurRequest selectPurRequestById(Long requestId) {
        return purRequestMapper.selectPurRequestById(requestId);
    }
    
    @Override
    public int insertPurRequest(PurRequest purRequest) {
        return purRequestMapper.insertPurRequest(purRequest);
    }
    
    @Override
    public int updatePurRequest(PurRequest purRequest) {
        return purRequestMapper.updatePurRequest(purRequest);
    }
    
    @Override
    public int deletePurRequestById(Long requestId) {
        return purRequestMapper.deletePurRequestById(requestId);
    }
}
