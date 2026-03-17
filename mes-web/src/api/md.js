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

