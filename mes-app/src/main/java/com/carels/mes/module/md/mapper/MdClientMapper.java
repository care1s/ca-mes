package com.carels.mes.module.md.mapper;

import com.carels.mes.module.md.domain.MdClient;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户管理Mapper接口 - carels
 *
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
public interface MdClientMapper {

    /**
     * 查询客户列表
     */
    List<MdClient> selectMdClientList(MdClient mdClient);

    /**
     * 根据ID查询客户
     */
    MdClient selectMdClientById(Long clientId);

    /**
     * 根据编码查询客户
     */
    MdClient selectMdClientByCode(String clientCode);

    /**
     * 查询所有启用的客户
     */
    List<MdClient> selectAllEnabledClients();

    /**
     * 新增客户
     */
    int insertMdClient(MdClient mdClient);

    /**
     * 修改客户
     */
    int updateMdClient(MdClient mdClient);

    /**
     * 删除客户
     */
    int deleteMdClientById(Long clientId);

    /**
     * 批量删除客户
     */
    int deleteMdClientByIds(Long[] clientIds);
}
