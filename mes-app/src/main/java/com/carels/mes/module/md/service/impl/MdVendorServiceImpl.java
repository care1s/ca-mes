package com.carels.mes.module.md.service.impl;

import com.carels.mes.module.md.domain.MdVendor;
import com.carels.mes.module.md.mapper.MdVendorMapper;
import com.carels.mes.module.md.service.IMdVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商管理Service实现 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
@Service
public class MdVendorServiceImpl implements IMdVendorService {

    @Autowired
    private MdVendorMapper mdVendorMapper;

    @Override
    public List<MdVendor> selectMdVendorList(MdVendor mdVendor) {
        return mdVendorMapper.selectMdVendorList(mdVendor);
    }

    @Override
    public MdVendor selectMdVendorById(Long vendorId) {
        return mdVendorMapper.selectMdVendorById(vendorId);
    }

    @Override
    public MdVendor selectMdVendorByCode(String vendorCode) {
        return mdVendorMapper.selectMdVendorByCode(vendorCode);
    }

    @Override
    public List<MdVendor> selectAllEnabledVendors() {
        return mdVendorMapper.selectAllEnabledVendors();
    }

    @Override
    public int insertMdVendor(MdVendor mdVendor) {
        return mdVendorMapper.insertMdVendor(mdVendor);
    }

    @Override
    public int updateMdVendor(MdVendor mdVendor) {
        return mdVendorMapper.updateMdVendor(mdVendor);
    }

    @Override
    public int deleteMdVendorById(Long vendorId) {
        return mdVendorMapper.deleteMdVendorById(vendorId);
    }

    @Override
    public int deleteMdVendorByIds(Long[] vendorIds) {
        return mdVendorMapper.deleteMdVendorByIds(vendorIds);
    }

    @Override
    public boolean checkVendorCodeUnique(String vendorCode) {
        MdVendor vendor = mdVendorMapper.selectMdVendorByCode(vendorCode);
        return vendor == null;
    }
}
