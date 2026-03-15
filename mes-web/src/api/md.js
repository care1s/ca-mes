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
 * 获取所有车间（用于下拉选择）
 */
export function getWorkshopOptions() {
  return request({
    url: '/mes/md/workshop/options',
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

// ==================== 工作站 API ====================

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
export function delWorkstation(workshopId) {
  return request({
    url: '/mes/md/workstation/' + workshopId,
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

// ==================== 供应商 API ====================

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

// ==================== BOM API ====================

/**
 * 获取BOM列表
 * @param {Object} params 查询参数
 */
export function listBom(params) {
  return request({
    url: '/mes/md/bom/list',
    method: 'get',
    params
  })
}

/**
 * 获取BOM详情
 * @param {number} bomId BOM ID
 */
export function getBom(bomId) {
  return request({
    url: '/mes/md/bom/' + bomId,
    method: 'get'
  })
}

/**
 * 新增BOM
 * @param {Object} data BOM数据
 */
export function addBom(data) {
  return request({
    url: '/mes/md/bom',
    method: 'post',
    data
  })
}

/**
 * 修改BOM
 * @param {Object} data BOM数据
 */
export function updateBom(data) {
  return request({
    url: '/mes/md/bom',
    method: 'put',
    data
  })
}

/**
 * 删除BOM
 * @param {number} bomId BOM ID
 */
export function delBom(bomId) {
  return request({
    url: '/mes/md/bom/' + bomId,
    method: 'delete'
  })
}

