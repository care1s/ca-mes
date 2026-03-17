package com.carels.mes.module.md.mapper;

import com.carels.mes.module.md.domain.MdVendor;

import java.util.List;

/**
 * 供应商管理Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface MdVendorMapper {

    /**
     * 查询供应商列表
     */
    List<MdVendor> selectMdVendorList(MdVendor mdVendor);

    /**
     * 根据ID查询供应商
     */
    MdVendor selectMdVendorById(Long vendorId);

    /**
     * 根据编码查询供应商
     */
    MdVendor selectMdVendorByCode(String vendorCode);

    /**
     * 查询所有启用的供应商
     */
    List<MdVendor> selectAllEnabledVendors();

    /**
     * 新增供应商
     */
    int insertMdVendor(MdVendor mdVendor);

    /**
     * 修改供应商
     */
    int updateMdVendor(MdVendor mdVendor);

    /**
     * 删除供应商
     */
    int deleteMdVendorById(Long vendorId);

    /**
     * 批量删除供应商
     */
    int deleteMdVendorByIds(Long[] vendorIds);
}
