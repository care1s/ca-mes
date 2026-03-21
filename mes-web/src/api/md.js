/**
 * 主数据模块 API - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 * @copyright carels
 */

import request from './request'

// ==================== 物料类型 API ====================

/**
 * 获取物料类型树
 */
export function getItemTypeTree() {
  return request({
    url: '/mes/md/itemType/tree',
    method: 'get'
  })
}

/**
 * 获取物料类型列表
 * @param {Object} params 查询参数
 */
export function listItemType(params) {
  return request({
    url: '/mes/md/itemType/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取物料类型
 * @param {number} typeId 类型ID
 */
export function getItemType(typeId) {
  return request({
    url: '/mes/md/itemType/' + typeId,
    method: 'get'
  })
}

/**
 * 新增物料类型
 * @param {Object} data 类型数据
 */
export function addItemType(data) {
  return request({
    url: '/mes/md/itemType',
    method: 'post',
    data
  })
}

/**
 * 修改物料类型
 * @param {Object} data 类型数据
 */
export function updateItemType(data) {
  return request({
    url: '/mes/md/itemType',
    method: 'put',
    data
  })
}

/**
 * 删除物料类型
 * @param {number} typeId 类型ID
 */
export function delItemType(typeId) {
  return request({
    url: '/mes/md/itemType/' + typeId,
    method: 'delete'
  })
}

// ==================== 物料信息 API ====================

/**
 * 获取物料列表
 * @param {Object} params 查询参数
 */
export function listItem(params) {
  return request({
    url: '/mes/md/item/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取物料
 * @param {number} itemId 物料ID
 */
export function getItem(itemId) {
  return request({
    url: '/mes/md/item/' + itemId,
    method: 'get'
  })
}

/**
 * 新增物料
 * @param {Object} data 物料数据
 */
export function addItem(data) {
  return request({
    url: '/mes/md/item',
    method: 'post',
    data
  })
}

/**
 * 修改物料
 * @param {Object} data 物料数据
 */
export function updateItem(data) {
  return request({
    url: '/mes/md/item',
    method: 'put',
    data
  })
}

/**
 * 删除物料
 * @param {number} itemId 物料ID
 */
export function delItem(itemId) {
  return request({
    url: '/mes/md/item/' + itemId,
    method: 'delete'
  })
}

/**
 * 批量删除物料
 * @param {Array} itemIds 物料ID数组
 */
export function batchDelItem(itemIds) {
  return request({
    url: '/mes/md/item/batch/' + itemIds.join(','),
    method: 'delete'
  })
}

/**
 * 导出物料
 * @param {Object} params 查询参数
 */
export function exportItem(params) {
  return request({
    url: '/mes/md/item/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 车间管理 API ====================

/**
 * 获取车间列表
 * @param {Object} params 查询参数
 */
export function listWorkshop(params) {
  return request({
    url: '/mes/md/workshop/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的车间（用于下拉选择）
 */
export function getWorkshopOptions() {
  return request({
    url: '/mes/md/workshop/all',
    method: 'get'
  })
}

/**
 * 新增车间
 * @param {Object} data 车间数据
 */
export function addWorkshop(data) {
  return request({
    url: '/mes/md/workshop',
    method: 'post',
    data
  })
}

/**
 * 修改车间
 * @param {Object} data 车间数据
 */
export function updateWorkshop(data) {
  return request({
    url: '/mes/md/workshop',
    method: 'put',
    data
  })
}

/**
 * 删除车间
 * @param {number} workshopId 车间ID
 */
export function delWorkshop(workshopId) {
  return request({
    url: '/mes/md/workshop/' + workshopId,
    method: 'delete'
  })
}

// ==================== 生产线管理 API ====================

/**
 * 获取生产线列表
 * @param {Object} params 查询参数
 */
export function listProductionLine(params) {
  return request({
    url: '/mes/md/productionLine/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的生产线（下拉选择用）
 */
export function getAllProductionLines() {
  return request({
    url: '/mes/md/productionLine/all',
    method: 'get'
  })
}

/**
 * 根据车间ID获取生产线列表
 * @param {number} workshopId 车间ID
 */
export function getProductionLinesByWorkshop(workshopId) {
  return request({
    url: '/mes/md/productionLine/workshop/' + workshopId,
    method: 'get'
  })
}

/**
 * 根据ID获取生产线
 * @param {number} lineId 生产线ID
 */
export function getProductionLine(lineId) {
  return request({
    url: '/mes/md/productionLine/' + lineId,
    method: 'get'
  })
}

/**
 * 根据编码获取生产线
 * @param {string} lineCode 生产线编码
 */
export function getProductionLineByCode(lineCode) {
  return request({
    url: '/mes/md/productionLine/code/' + lineCode,
    method: 'get'
  })
}

/**
 * 新增生产线
 * @param {Object} data 生产线数据
 */
export function addProductionLine(data) {
  return request({
    url: '/mes/md/productionLine',
    method: 'post',
    data
  })
}

/**
 * 修改生产线
 * @param {Object} data 生产线数据
 */
export function updateProductionLine(data) {
  return request({
    url: '/mes/md/productionLine',
    method: 'put',
    data
  })
}

/**
 * 删除生产线
 * @param {number} lineId 生产线ID
 */
export function delProductionLine(lineId) {
  return request({
    url: '/mes/md/productionLine/' + lineId,
    method: 'delete'
  })
}

/**
 * 批量删除生产线
 * @param {Array} lineIds 生产线ID数组
 */
export function delProductionLineBatch(lineIds) {
  return request({
    url: '/mes/md/productionLine/batch/' + lineIds.join(','),
    method: 'delete'
  })
}

// ==================== 工作站管理 API ====================

/**
 * 获取工作站列表
 * @param {Object} params 查询参数
 */
export function listWorkstation(params) {
  return request({
    url: '/mes/md/workstation/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的工作站（下拉选择用）
 */
export function getAllWorkstations() {
  return request({
    url: '/mes/md/workstation/all',
    method: 'get'
  })
}

/**
 * 根据车间ID获取工作站列表
 * @param {number} workshopId 车间ID
 */
export function getWorkstationsByWorkshop(workshopId) {
  return request({
    url: '/mes/md/workstation/workshop/' + workshopId,
    method: 'get'
  })
}

/**
 * 根据生产线ID获取工作站列表
 * @param {number} lineId 生产线ID
 */
export function getWorkstationsByLine(lineId) {
  return request({
    url: '/mes/md/workstation/line/' + lineId,
    method: 'get'
  })
}

/**
 * 根据ID获取工作站
 * @param {number} workstationId 工作站ID
 */
export function getWorkstation(workstationId) {
  return request({
    url: '/mes/md/workstation/' + workstationId,
    method: 'get'
  })
}

/**
 * 根据编码获取工作站
 * @param {string} workstationCode 工作站编码
 */
export function getWorkstationByCode(workstationCode) {
  return request({
    url: '/mes/md/workstation/code/' + workstationCode,
    method: 'get'
  })
}

/**
 * 新增工作站
 * @param {Object} data 工作站数据
 */
export function addWorkstation(data) {
  return request({
    url: '/mes/md/workstation',
    method: 'post',
    data
  })
}

/**
 * 修改工作站
 * @param {Object} data 工作站数据
 */
export function updateWorkstation(data) {
  return request({
    url: '/mes/md/workstation',
    method: 'put',
    data
  })
}

/**
 * 删除工作站
 * @param {number} workstationId 工作站ID
 */
export function delWorkstation(workstationId) {
  return request({
    url: '/mes/md/workstation/' + workstationId,
    method: 'delete'
  })
}

/**
 * 批量删除工作站
 * @param {Array} workstationIds 工作站ID数组
 */
export function delWorkstationBatch(workstationIds) {
  return request({
    url: '/mes/md/workstation/batch/' + workstationIds.join(','),
    method: 'delete'
  })
}

// ==================== 客户管理 API ====================

/**
 * 获取客户列表
 * @param {Object} params 查询参数
 */
export function listClient(params) {
  return request({
    url: '/mes/md/client/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的客户（下拉选择用）
 */
export function getAllClients() {
  return request({
    url: '/mes/md/client/all',
    method: 'get'
  })
}

/**
 * 根据ID获取客户
 * @param {number} clientId 客户ID
 */
export function getClient(clientId) {
  return request({
    url: '/mes/md/client/' + clientId,
    method: 'get'
  })
}

/**
 * 新增客户
 * @param {Object} data 客户数据
 */
export function addClient(data) {
  return request({
    url: '/mes/md/client',
    method: 'post',
    data
  })
}

/**
 * 修改客户
 * @param {Object} data 客户数据
 */
export function updateClient(data) {
  return request({
    url: '/mes/md/client',
    method: 'put',
    data
  })
}

/**
 * 删除客户
 * @param {number} clientId 客户ID
 */
export function delClient(clientId) {
  return request({
    url: '/mes/md/client/' + clientId,
    method: 'delete'
  })
}

/**
 * 批量删除客户
 * @param {Array} clientIds 客户ID数组
 */
export function delClientBatch(clientIds) {
  return request({
    url: '/mes/md/client/batch/' + clientIds.join(','),
    method: 'delete'
  })
}

// ==================== 供应商管理 API ====================

/**
 * 获取供应商列表
 * @param {Object} params 查询参数
 */
export function listVendor(params) {
  return request({
    url: '/mes/md/vendor/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的供应商（下拉选择用）
 */
export function getAllVendors() {
  return request({
    url: '/mes/md/vendor/all',
    method: 'get'
  })
}

/**
 * 根据ID获取供应商
 * @param {number} vendorId 供应商ID
 */
export function getVendor(vendorId) {
  return request({
    url: '/mes/md/vendor/' + vendorId,
    method: 'get'
  })
}

/**
 * 新增供应商
 * @param {Object} data 供应商数据
 */
export function addVendor(data) {
  return request({
    url: '/mes/md/vendor',
    method: 'post',
    data
  })
}

/**
 * 修改供应商
 * @param {Object} data 供应商数据
 */
export function updateVendor(data) {
  return request({
    url: '/mes/md/vendor',
    method: 'put',
    data
  })
}

/**
 * 删除供应商
 * @param {number} vendorId 供应商ID
 */
export function delVendor(vendorId) {
  return request({
    url: '/mes/md/vendor/' + vendorId,
    method: 'delete'
  })
}

/**
 * 批量删除供应商
 * @param {Array} vendorIds 供应商ID数组
 */
export function delVendorBatch(vendorIds) {
  return request({
    url: '/mes/md/vendor/batch/' + vendorIds.join(','),
    method: 'delete'
  })
}

// ==================== 采购管理 API ====================

/**
 * 获取采购申请列表
 * @param {Object} params 查询参数
 */
export function listPurRequest(params) {
  return request({
    url: '/mes/pur/request/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取采购申请
 * @param {number} requestId 申请ID
 */
export function getPurRequest(requestId) {
  return request({
    url: '/mes/pur/request/' + requestId,
    method: 'get'
  })
}

/**
 * 新增采购申请
 * @param {Object} data 申请数据
 */
export function addPurRequest(data) {
  return request({
    url: '/mes/pur/request',
    method: 'post',
    data
  })
}

/**
 * 修改采购申请
 * @param {Object} data 申请数据
 */
export function updatePurRequest(data) {
  return request({
    url: '/mes/pur/request',
    method: 'put',
    data
  })
}

/**
 * 删除采购申请
 * @param {number} requestId 申请ID
 */
export function delPurRequest(requestId) {
  return request({
    url: '/mes/pur/request/' + requestId,
    method: 'delete'
  })
}

/**
 * 提交采购申请审批
 * @param {number} requestId 申请ID
 */
export function submitPurRequest(requestId) {
  return request({
    url: '/mes/pur/request/' + requestId + '/submit',
    method: 'put'
  })
}

/**
 * 审批通过采购申请
 * @param {number} requestId 申请ID
 * @param {string} auditOpinion 审批意见
 */
export function approvePurRequest(requestId, auditOpinion) {
  return request({
    url: '/mes/pur/request/' + requestId + '/approve',
    method: 'put',
    params: { auditOpinion }
  })
}

/**
 * 审批拒绝采购申请
 * @param {number} requestId 申请ID
 * @param {string} auditOpinion 审批意见
 */
export function rejectPurRequest(requestId, auditOpinion) {
  return request({
    url: '/mes/pur/request/' + requestId + '/reject',
    method: 'put',
    params: { auditOpinion }
  })
}

// ==================== 采购订单 API ====================

/**
 * 获取采购订单列表
 * @param {Object} params 查询参数
 */
export function listPurOrder(params) {
  return request({
    url: '/mes/pur/order/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取采购订单
 * @param {number} orderId 订单ID
 */
export function getPurOrder(orderId) {
  return request({
    url: '/mes/pur/order/' + orderId,
    method: 'get'
  })
}

/**
 * 新增采购订单
 * @param {Object} data 订单数据
 */
export function addPurOrder(data) {
  return request({
    url: '/mes/pur/order',
    method: 'post',
    data
  })
}

/**
 * 修改采购订单
 * @param {Object} data 订单数据
 */
export function updatePurOrder(data) {
  return request({
    url: '/mes/pur/order',
    method: 'put',
    data
  })
}

/**
 * 删除采购订单
 * @param {number} orderId 订单ID
 */
export function delPurOrder(orderId) {
  return request({
    url: '/mes/pur/order/' + orderId,
    method: 'delete'
  })
}

/**
 * 根据采购申请生成采购订单
 * @param {number} requestId 申请ID
 */
export function createOrderFromRequest(requestId) {
  return request({
    url: '/mes/pur/order/from-request/' + requestId,
    method: 'post'
  })
}

// ==================== 采购入库 API ====================

/**
 * 获取采购入库列表
 * @param {Object} params 查询参数
 */
export function listPurReceipt(params) {
  return request({
    url: '/mes/pur/receipt/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取采购入库
 * @param {number} receiptId 入库ID
 */
export function getPurReceipt(receiptId) {
  return request({
    url: '/mes/pur/receipt/' + receiptId,
    method: 'get'
  })
}

/**
 * 新增采购入库
 * @param {Object} data 入库数据
 */
export function addPurReceipt(data) {
  return request({
    url: '/mes/pur/receipt',
    method: 'post',
    data
  })
}

/**
 * 修改采购入库
 * @param {Object} data 入库数据
 */
export function updatePurReceipt(data) {
  return request({
    url: '/mes/pur/receipt',
    method: 'put',
    data
  })
}

/**
 * 删除采购入库
 * @param {number} receiptId 入库ID
 */
export function delPurReceipt(receiptId) {
  return request({
    url: '/mes/pur/receipt/' + receiptId,
    method: 'delete'
  })
}

/**
 * 确认采购入库
 * @param {number} receiptId 入库ID
 */
export function confirmPurReceipt(receiptId) {
  return request({
    url: '/mes/pur/receipt/' + receiptId + '/confirm',
    method: 'post'
  })
}

// ==================== 采购退货 API ====================

/**
 * 获取采购退货列表
 * @param {Object} params 查询参数
 */
export function listPurReturn(params) {
  return request({
    url: '/mes/pur/return/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取采购退货
 * @param {number} returnId 退货ID
 */
export function getPurReturn(returnId) {
  return request({
    url: '/mes/pur/return/' + returnId,
    method: 'get'
  })
}

/**
 * 新增采购退货
 * @param {Object} data 退货数据
 */
export function addPurReturn(data) {
  return request({
    url: '/mes/pur/return',
    method: 'post',
    data
  })
}

/**
 * 修改采购退货
 * @param {Object} data 退货数据
 */
export function updatePurReturn(data) {
  return request({
    url: '/mes/pur/return',
    method: 'put',
    data
  })
}

/**
 * 删除采购退货
 * @param {number} returnId 退货ID
 */
export function delPurReturn(returnId) {
  return request({
    url: '/mes/pur/return/' + returnId,
    method: 'delete'
  })
}

// ==================== 生产计划 API ====================

/**
 * 获取生产计划列表
 * @param {Object} params 查询参数
 */
export function listProPlan(params) {
  return request({
    url: '/mes/pro/plan/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取生产计划
 * @param {number} planId 计划ID
 */
export function getProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId,
    method: 'get'
  })
}

/**
 * 新增生产计划
 * @param {Object} data 计划数据
 */
export function addProPlan(data) {
  return request({
    url: '/mes/pro/plan',
    method: 'post',
    data
  })
}

/**
 * 修改生产计划
 * @param {Object} data 计划数据
 */
export function updateProPlan(data) {
  return request({
    url: '/mes/pro/plan',
    method: 'put',
    data
  })
}

/**
 * 删除生产计划
 * @param {number} planId 计划ID
 */
export function delProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId,
    method: 'delete'
  })
}

/**
 * 发布生产计划
 * @param {number} planId 计划ID
 */
export function publishProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/publish',
    method: 'put'
  })
}

/**
 * 开始执行生产计划
 * @param {number} planId 计划ID
 */
export function startProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/start',
    method: 'put'
  })
}

/**
 * 完成生产计划
 * @param {number} planId 计划ID
 */
export function completeProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/complete',
    method: 'put'
  })
}

// ==================== 物料需求 API ====================

/**
 * 获取物料需求列表
 * @param {Object} params 查询参数
 */
export function listProMaterialReq(params) {
  return request({
    url: '/mes/pro/materialReq/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取物料需求
 * @param {number} reqId 需求ID
 */
export function getProMaterialReq(reqId) {
  return request({
    url: '/mes/pro/materialReq/' + reqId,
    method: 'get'
  })
}

/**
 * 新增物料需求
 * @param {Object} data 需求数据
 */
export function addProMaterialReq(data) {
  return request({
    url: '/mes/pro/materialReq',
    method: 'post',
    data
  })
}

/**
 * 修改物料需求
 * @param {Object} data 需求数据
 */
export function updateProMaterialReq(data) {
  return request({
    url: '/mes/pro/materialReq',
    method: 'put',
    data
  })
}

/**
 * 删除物料需求
 * @param {number} reqId 需求ID
 */
export function delProMaterialReq(reqId) {
  return request({
    url: '/mes/pro/materialReq/' + reqId,
    method: 'delete'
  })
}

/**
 * 提交物料需求审核
 * @param {number} reqId 需求ID
 */
export function submitProMaterialReq(reqId) {
  return request({
    url: '/mes/pro/materialReq/' + reqId + '/submit',
    method: 'put'
  })
}

/**
 * 审核通过物料需求
 * @param {number} reqId 需求ID
 */
export function approveProMaterialReq(reqId) {
  return request({
    url: '/mes/pro/materialReq/' + reqId + '/approve',
    method: 'put'
  })
}

/**
 * 发料确认
 * @param {number} reqId 需求ID
 */
export function issueProMaterialReq(reqId) {
  return request({
    url: '/mes/pro/materialReq/' + reqId + '/issue',
    method: 'put'
  })
}

// ==================== 系统管理 API ====================

// 用户管理
export function listSysUser(params) {
  return request({
    url: '/mes/system/user/list',
    method: 'get',
    params
  })
}

export function getSysUser(userId) {
  return request({
    url: '/mes/system/user/' + userId,
    method: 'get'
  })
}

export function addSysUser(data) {
  return request({
    url: '/mes/system/user',
    method: 'post',
    data
  })
}

export function updateSysUser(data) {
  return request({
    url: '/mes/system/user',
    method: 'put',
    data
  })
}

export function delSysUser(userId) {
  return request({
    url: '/mes/system/user/' + userId,
    method: 'delete'
  })
}

export function resetSysUserPwd(userId, password) {
  return request({
    url: '/mes/system/user/' + userId + '/resetPwd',
    method: 'put',
    params: { password }
  })
}

// 角色管理
export function listSysRole(params) {
  return request({
    url: '/mes/system/role/list',
    method: 'get',
    params
  })
}

export function getSysRole(roleId) {
  return request({
    url: '/mes/system/role/' + roleId,
    method: 'get'
  })
}

export function addSysRole(data) {
  return request({
    url: '/mes/system/role',
    method: 'post',
    data
  })
}

export function updateSysRole(data) {
  return request({
    url: '/mes/system/role',
    method: 'put',
    data
  })
}

export function delSysRole(roleId) {
  return request({
    url: '/mes/system/role/' + roleId,
    method: 'delete'
  })
}

export function getRolesByUserId(userId) {
  return request({
    url: '/mes/system/role/user/' + userId,
    method: 'get'
  })
}

export function assignRoleMenus(roleId, menuIds) {
  return request({
    url: '/mes/system/role/' + roleId + '/menus',
    method: 'put',
    data: menuIds
  })
}

// 菜单管理
export function listSysMenu(params) {
  return request({
    url: '/mes/system/menu/list',
    method: 'get',
    params
  })
}

export function getSysMenuTree() {
  return request({
    url: '/mes/system/menu/tree',
    method: 'get'
  })
}

export function getSysMenu(menuId) {
  return request({
    url: '/mes/system/menu/' + menuId,
    method: 'get'
  })
}

export function addSysMenu(data) {
  return request({
    url: '/mes/system/menu',
    method: 'post',
    data
  })
}

export function updateSysMenu(data) {
  return request({
    url: '/mes/system/menu',
    method: 'put',
    data
  })
}

export function delSysMenu(menuId) {
  return request({
    url: '/mes/system/menu/' + menuId,
    method: 'delete'
  })
}

export function getMenusByRoleId(roleId) {
  return request({
    url: '/mes/system/menu/role/' + roleId,
    method: 'get'
  })
}

export function changeMenuStatus(menuId, status) {
  return request({
    url: '/mes/system/menu/' + menuId + '/status',
    method: 'put',
    params: { status }
  })
}

// ==================== 部门管理 API ====================

export function listSysDept(params) {
  return request({
    url: '/mes/system/dept/list',
    method: 'get',
    params
  })
}

export function getAllDepts() {
  return request({
    url: '/mes/system/dept/all',
    method: 'get'
  })
}

export function getSysDept(deptId) {
  return request({
    url: '/mes/system/dept/' + deptId,
    method: 'get'
  })
}

/**
 * ==================== 仓库管理 API ====================
 */

// 仓库列表
export function listWmWarehouse(params) {
  return request({
    url: '/mes/wm/warehouse/list',
    method: 'get',
    params
  })
}

// 仓库详情
export function getWmWarehouse(warehouseId) {
  return request({
    url: '/mes/wm/warehouse/' + warehouseId,
    method: 'get'
  })
}

// 新增仓库
export function addWmWarehouse(data) {
  return request({
    url: '/mes/wm/warehouse',
    method: 'post',
    data
  })
}

// 修改仓库
export function updateWmWarehouse(data) {
  return request({
    url: '/mes/wm/warehouse',
    method: 'put',
    data
  })
}

// 删除仓库
export function delWmWarehouse(warehouseId) {
  return request({
    url: '/mes/wm/warehouse/' + warehouseId,
    method: 'delete'
  })
}

// 更新仓库状态
export function updateWmWarehouseStatus(warehouseId, status) {
  return request({
    url: '/mes/wm/warehouse/' + warehouseId + '/status',
    method: 'put',
    params: { status }
  })
}

/**
 * ==================== 库存管理 API ====================
 */

// 库存列表
export function listWmStock(params) {
  return request({
    url: '/mes/wm/stock/list',
    method: 'get',
    params
  })
}

// 库存详情
export function getWmStock(id) {
  return request({
    url: '/mes/wm/stock/' + id,
    method: 'get'
  })
}

// 新增库存
export function addWmStock(data) {
  return request({
    url: '/mes/wm/stock',
    method: 'post',
    data
  })
}

// 修改库存
export function updateWmStock(data) {
  return request({
    url: '/mes/wm/stock',
    method: 'put',
    data
  })
}

// 删除库存
export function delWmStock(id) {
  return request({
    url: '/mes/wm/stock/' + id,
    method: 'delete'
  })
}

/**
 * ==================== 入库管理 API ====================
 */

// 入库单列表
export function listWmRecpt(params) {
  return request({
    url: '/mes/wm/recpt/list',
    method: 'get',
    params
  })
}

// 入库单详情
export function getWmRecpt(recptId) {
  return request({
    url: '/mes/wm/recpt/' + recptId,
    method: 'get'
  })
}

// 新增入库单
export function addWmRecpt(data) {
  return request({
    url: '/mes/wm/recpt',
    method: 'post',
    data
  })
}

// 修改入库单
export function updateWmRecpt(data) {
  return request({
    url: '/mes/wm/recpt',
    method: 'put',
    data
  })
}

// 删除入库单
export function delWmRecpt(recptId) {
  return request({
    url: '/mes/wm/recpt/' + recptId,
    method: 'delete'
  })
}

// 更新入库单状态
export function updateWmRecptStatus(recptId, status) {
  return request({
    url: '/mes/wm/recpt/' + recptId + '/status',
    method: 'put',
    params: { status }
  })
}

// 确认入库
export function confirmWmRecpt(recptId) {
  return request({
    url: '/mes/wm/recpt/' + recptId + '/confirm',
    method: 'post'
  })
}

/**
 * ==================== 出库管理 API ====================
 */

// 出库单列表
export function listWmIssue(params) {
  return request({
    url: '/mes/wm/issue/list',
    method: 'get',
    params
  })
}

// 出库单详情
export function getWmIssue(issueId) {
  return request({
    url: '/mes/wm/issue/' + issueId,
    method: 'get'
  })
}

// 新增出库单
export function addWmIssue(data) {
  return request({
    url: '/mes/wm/issue',
    method: 'post',
    data
  })
}

// 修改出库单
export function updateWmIssue(data) {
  return request({
    url: '/mes/wm/issue',
    method: 'put',
    data
  })
}

// 删除出库单
export function delWmIssue(issueId) {
  return request({
    url: '/mes/wm/issue/' + issueId,
    method: 'delete'
  })
}

// 更新出库单状态
export function updateWmIssueStatus(issueId, status) {
  return request({
    url: '/mes/wm/issue/' + issueId + '/status',
    method: 'put',
    params: { status }
  })
}

// 确认出库
export function confirmWmIssue(issueId) {
  return request({
    url: '/mes/wm/issue/' + issueId + '/confirm',
    method: 'post'
  })
}


/**
 * ==================== 仓区管理 API ====================
 */

// 仓区列表
export function listWmZone(params) {
  return request({
    url: '/mes/wm/zone/list',
    method: 'get',
    params
  })
}

// 仓区详情
export function getWmZone(zoneId) {
  return request({
    url: '/mes/wm/zone/' + zoneId,
    method: 'get'
  })
}

// 根据仓库获取仓区
export function getWmZoneByWarehouse(warehouseId) {
  return request({
    url: '/mes/wm/zone/warehouse/' + warehouseId,
    method: 'get'
  })
}

// 新增仓区
export function addWmZone(data) {
  return request({
    url: '/mes/wm/zone',
    method: 'post',
    data
  })
}

// 修改仓区
export function updateWmZone(data) {
  return request({
    url: '/mes/wm/zone',
    method: 'put',
    data
  })
}

// 删除仓区
export function delWmZone(zoneId) {
  return request({
    url: '/mes/wm/zone/' + zoneId,
    method: 'delete'
  })
}

// 批量删除仓区
export function delWmZoneBatch(zoneIds) {
  return request({
    url: '/mes/wm/zone/batch/' + zoneIds.join(','),
    method: 'delete'
  })
}

/**
 * ==================== 仓位管理 API ====================
 */

// 仓位列表
export function listWmLocation(params) {
  return request({
    url: '/mes/wm/location/list',
    method: 'get',
    params
  })
}

// 仓位详情
export function getWmLocation(locationId) {
  return request({
    url: '/mes/wm/location/' + locationId,
    method: 'get'
  })
}

// 根据仓区获取仓位
export function getWmLocationByZone(zoneId) {
  return request({
    url: '/mes/wm/location/zone/' + zoneId,
    method: 'get'
  })
}

// 根据仓库获取仓位
export function getWmLocationByWarehouse(warehouseId) {
  return request({
    url: '/mes/wm/location/warehouse/' + warehouseId,
    method: 'get'
  })
}

// 新增仓位
export function addWmLocation(data) {
  return request({
    url: '/mes/wm/location',
    method: 'post',
    data
  })
}

// 修改仓位
export function updateWmLocation(data) {
  return request({
    url: '/mes/wm/location',
    method: 'put',
    data
  })
}

// 删除仓位
export function delWmLocation(locationId) {
  return request({
    url: '/mes/wm/location/' + locationId,
    method: 'delete'
  })
}

// 批量删除仓位
export function delWmLocationBatch(locationIds) {
  return request({
    url: '/mes/wm/location/batch/' + locationIds.join(','),
    method: 'delete'
  })
}

/**
 * ==================== 部门管理 API ====================
 */

// 部门列表
export function listDept(params) {
  return request({
    url: '/mes/system/dept/list',
    method: 'get',
    params
  })
}

// 所有部门（下拉选择）
export function getAllDepts() {
  return request({
    url: '/mes/system/dept/all',
    method: 'get'
  })
}

// 部门详情
export function getDept(deptId) {
  return request({
    url: '/mes/system/dept/' + deptId,
    method: 'get'
  })
}

// 新增部门
export function addDept(data) {
  return request({
    url: '/mes/system/dept',
    method: 'post',
    data
  })
}

// 修改部门
export function updateDept(data) {
  return request({
    url: '/mes/system/dept',
    method: 'put',
    data
  })
}

// 删除部门
export function delDept(deptId) {
  return request({
    url: '/mes/system/dept/' + deptId,
    method: 'delete'
  })
}
