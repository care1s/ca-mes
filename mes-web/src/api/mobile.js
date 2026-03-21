/**
 * 移动端 API - 扫码报工、移动检验
 * @author carels
 * @version V9.0
 * @date 2026-03-20
 */

import request from './request'

// ==================== 移动端生产任务 API ====================

/**
 * 扫码查询任务
 * @param {string} barcode 条码
 */
export function scanTask(barcode) {
  return request({
    url: '/mobile/pro/task/scan',
    method: 'get',
    params: { barcode }
  })
}

/**
 * 获取待执行任务列表
 * @param {number} workstationId 工作站ID
 */
export function getPendingTasks(workstationId) {
  return request({
    url: '/mobile/pro/task/pending',
    method: 'get',
    params: { workstationId }
  })
}

/**
 * 获取我的任务列表
 * @param {number} operatorId 操作员ID
 */
export function getMyTasks(operatorId) {
  return request({
    url: '/mobile/pro/task/my',
    method: 'get',
    params: { operatorId }
  })
}

/**
 * 扫码快捷报工
 * @param {Object} data 报工数据 {barcode, quantity, operatorId, operatorName}
 */
export function scanFeedback(data) {
  return request({
    url: '/mobile/pro/task/feedback/scan',
    method: 'post',
    data
  })
}

/**
 * 简单报工（已知任务ID）
 * @param {Object} data 报工数据 {taskId, quantity, operatorId, operatorName}
 */
export function submitFeedback(data) {
  return request({
    url: '/mobile/pro/task/feedback',
    method: 'post',
    data
  })
}

// ==================== 移动端质量检验 API ====================

/**
 * 扫码查询检验任务
 * @param {string} barcode 条码
 * @param {string} qcType 检验类型: IQC/IPQC/OQC
 */
export function scanQcTask(barcode, qcType) {
  return request({
    url: '/mobile/qc/scan',
    method: 'get',
    params: { barcode, qcType }
  })
}

/**
 * 获取待检验列表
 * @param {string} qcType 检验类型
 * @param {number} inspectorId 检验员ID
 */
export function getPendingQcList(qcType, inspectorId) {
  return request({
    url: '/mobile/qc/pending',
    method: 'get',
    params: { qcType, inspectorId }
  })
}

/**
 * 获取我的检验列表
 * @param {number} inspectorId 检验员ID
 */
export function getMyQcList(inspectorId) {
  return request({
    url: '/mobile/qc/my',
    method: 'get',
    params: { inspectorId }
  })
}

/**
 * 扫码快捷报检
 * @param {Object} data 报检数据 {barcode, qcType, quantity, inspectorId, inspectorName}
 */
export function scanInspect(data) {
  return request({
    url: '/mobile/qc/inspect/scan',
    method: 'post',
    data
  })
}

/**
 * 开始检验
 * @param {number} recordId 检验记录ID
 */
export function startInspect(recordId) {
  return request({
    url: '/mobile/qc/start/' + recordId,
    method: 'put'
  })
}

/**
 * 提交检验结果
 * @param {Object} data 检验结果 {recordId, qualifiedQty, defectiveQty, defectDesc, defectReason, handleMethod}
 */
export function submitQcResult(data) {
  return request({
    url: '/mobile/qc/result',
    method: 'post',
    data
  })
}

/**
 * 获取检验记录详情
 * @param {number} recordId 检验记录ID
 */
export function getQcRecord(recordId) {
  return request({
    url: '/mobile/qc/' + recordId,
    method: 'get'
  })
}
