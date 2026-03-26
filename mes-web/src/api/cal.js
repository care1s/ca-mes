/**
 * 排班管理模块 API - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-23
 * @copyright carels
 */

import request from './request'

// ==================== 日历 API ====================

/**
 * 获取日历列表
 * @param {Object} params 查询参数
 */
export function listCalendar(params) {
  return request({
    url: '/mes/cal/calendar/list',
    method: 'get',
    params
  })
}

/**
 * 根据日期获取日历
 * @param {string} calendarDate 日期
 */
export function getCalendarByDate(calendarDate) {
  return request({
    url: '/mes/cal/calendar/date/' + calendarDate,
    method: 'get'
  })
}

/**
 * 生成年度日历
 * @param {number} year 年份
 */
export function generateYearCalendar(year) {
  return request({
    url: '/mes/cal/calendar/generate/' + year,
    method: 'post'
  })
}

/**
 * 更新日历
 * @param {Object} data 日历数据
 */
export function updateCalendar(data) {
  return request({
    url: '/mes/cal/calendar',
    method: 'put',
    data
  })
}

// ==================== 班次 API ====================

/**
 * 获取班次列表
 * @param {Object} params 查询参数
 */
export function listShift(params) {
  return request({
    url: '/mes/cal/shift/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用班次
 */
export function listAllShift() {
  return request({
    url: '/mes/cal/shift/all',
    method: 'get'
  })
}

/**
 * 根据ID获取班次
 * @param {number} shiftId 班次ID
 */
export function getShift(shiftId) {
  return request({
    url: '/mes/cal/shift/' + shiftId,
    method: 'get'
  })
}

/**
 * 新增班次
 * @param {Object} data 班次数据
 */
export function addShift(data) {
  return request({
    url: '/mes/cal/shift',
    method: 'post',
    data
  })
}

/**
 * 修改班次
 * @param {Object} data 班次数据
 */
export function updateShift(data) {
  return request({
    url: '/mes/cal/shift',
    method: 'put',
    data
  })
}

/**
 * 删除班次
 * @param {number} shiftId 班次ID
 */
export function deleteShift(shiftId) {
  return request({
    url: '/mes/cal/shift/' + shiftId,
    method: 'delete'
  })
}

/**
 * 批量删除班次
 * @param {Array} shiftIds 班次ID数组
 */
export function deleteShiftBatch(shiftIds) {
  return request({
    url: '/mes/cal/shift/batch/' + shiftIds.join(','),
    method: 'delete'
  })
}

// ==================== 班组 API ====================

/**
 * 获取班组列表
 * @param {Object} params 查询参数
 */
export function listTeam(params) {
  return request({
    url: '/mes/cal/team/list',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用班组
 */
export function listAllTeam() {
  return request({
    url: '/mes/cal/team/all',
    method: 'get'
  })
}

/**
 * 根据ID获取班组
 * @param {number} teamId 班组ID
 */
export function getTeam(teamId) {
  return request({
    url: '/mes/cal/team/' + teamId,
    method: 'get'
  })
}

/**
 * 新增班组
 * @param {Object} data 班组数据
 */
export function addTeam(data) {
  return request({
    url: '/mes/cal/team',
    method: 'post',
    data
  })
}

/**
 * 修改班组
 * @param {Object} data 班组数据
 */
export function updateTeam(data) {
  return request({
    url: '/mes/cal/team',
    method: 'put',
    data
  })
}

/**
 * 删除班组
 * @param {number} teamId 班组ID
 */
export function deleteTeam(teamId) {
  return request({
    url: '/mes/cal/team/' + teamId,
    method: 'delete'
  })
}

/**
 * 批量删除班组
 * @param {Array} teamIds 班组ID数组
 */
export function deleteTeamBatch(teamIds) {
  return request({
    url: '/mes/cal/team/batch/' + teamIds.join(','),
    method: 'delete'
  })
}

// ==================== 排班计划 API ====================

/**
 * 获取排班计划列表
 * @param {Object} params 查询参数
 */
export function listPlan(params) {
  return request({
    url: '/mes/cal/plan/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取排班计划
 * @param {number} planId 计划ID
 */
export function getPlan(planId) {
  return request({
    url: '/mes/cal/plan/' + planId,
    method: 'get'
  })
}

/**
 * 新增排班计划
 * @param {Object} data 排班数据
 */
export function addPlan(data) {
  return request({
    url: '/mes/cal/plan',
    method: 'post',
    data
  })
}

/**
 * 修改排班计划
 * @param {Object} data 排班数据
 */
export function updatePlan(data) {
  return request({
    url: '/mes/cal/plan',
    method: 'put',
    data
  })
}

/**
 * 删除排班计划
 * @param {number} planId 计划ID
 */
export function deletePlan(planId) {
  return request({
    url: '/mes/cal/plan/' + planId,
    method: 'delete'
  })
}

/**
 * 批量删除排班计划
 * @param {Array} planIds 计划ID数组
 */
export function deletePlanBatch(planIds) {
  return request({
    url: '/mes/cal/plan/batch/' + planIds.join(','),
    method: 'delete'
  })
}

/**
 * 批量生成排班计划
 * @param {Array} data 排班数据列表
 */
export function batchGeneratePlan(data) {
  return request({
    url: '/mes/cal/plan/batch',
    method: 'post',
    data
  })
}
