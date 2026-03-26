import request from './request'

// ==================== 设备点检 API ====================

// 查询点检列表
export function listDvCheck(query) {
  return request({
    url: '/mes/dv/check/list',
    method: 'get',
    params: query
  })
}

// 查询点检详细
export function getDvCheck(checkId) {
  return request({
    url: '/mes/dv/check/' + checkId,
    method: 'get'
  })
}

// 新增点检
export function addDvCheck(data) {
  return request({
    url: '/mes/dv/check',
    method: 'post',
    data: data
  })
}

// 修改点检
export function updateDvCheck(data) {
  return request({
    url: '/mes/dv/check',
    method: 'put',
    data: data
  })
}

// 删除点检
export function delDvCheck(checkId) {
  return request({
    url: '/mes/dv/check/' + checkId,
    method: 'delete'
  })
}

// 更新点检状态
export function updateCheckStatus(checkId, status) {
  return request({
    url: '/mes/dv/check/' + checkId + '/status',
    method: 'put',
    params: { status }
  })
}

// ==================== 设备维修 API ====================

// 查询维修列表
export function listDvRepair(query) {
  return request({
    url: '/mes/dv/repair/list',
    method: 'get',
    params: query
  })
}

// 查询维修详细
export function getDvRepair(repairId) {
  return request({
    url: '/mes/dv/repair/' + repairId,
    method: 'get'
  })
}

// 新增维修
export function addDvRepair(data) {
  return request({
    url: '/mes/dv/repair',
    method: 'post',
    data: data
  })
}

// 修改维修
export function updateDvRepair(data) {
  return request({
    url: '/mes/dv/repair',
    method: 'put',
    data: data
  })
}

// 删除维修
export function delDvRepair(repairId) {
  return request({
    url: '/mes/dv/repair/' + repairId,
    method: 'delete'
  })
}

// 派工
export function assignRepair(repairId, repairUserId, repairUserName) {
  return request({
    url: '/mes/dv/repair/' + repairId + '/assign',
    method: 'put',
    params: { repairUserId, repairUserName }
  })
}

// 开始维修
export function startRepair(repairId) {
  return request({
    url: '/mes/dv/repair/' + repairId + '/start',
    method: 'put'
  })
}

// 完成维修
export function completeRepair(repairId, repairResult, repairCost) {
  return request({
    url: '/mes/dv/repair/' + repairId + '/complete',
    method: 'put',
    params: { repairResult, repairCost }
  })
}
