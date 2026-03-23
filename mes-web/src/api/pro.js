import request from './request'

// 查询生产计划列表
export function listProPlan(query) {
  return request({
    url: '/mes/pro/plan/list',
    method: 'get',
    params: query
  })
}

// 查询生产计划详细
export function getProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId,
    method: 'get'
  })
}

// 新增生产计划
export function addProPlan(data) {
  return request({
    url: '/mes/pro/plan',
    method: 'post',
    data: data
  })
}

// 修改生产计划
export function updateProPlan(data) {
  return request({
    url: '/mes/pro/plan',
    method: 'put',
    data: data
  })
}

// 删除生产计划
export function delProPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId,
    method: 'delete'
  })
}

// ==================== 工序管理 API ====================

// 查询工序列表
export function listProProcess(query) {
  return request({
    url: '/mes/pro/process/list',
    method: 'get',
    params: query
  })
}

// 查询工序详细
export function getProProcess(processId) {
  return request({
    url: '/mes/pro/process/' + processId,
    method: 'get'
  })
}

// 根据车间查询工序
export function getProcessByWorkshop(workshopId) {
  return request({
    url: '/mes/pro/process/byWorkshop/' + workshopId,
    method: 'get'
  })
}

// 新增工序
export function addProProcess(data) {
  return request({
    url: '/mes/pro/process',
    method: 'post',
    data: data
  })
}

// 修改工序
export function updateProProcess(data) {
  return request({
    url: '/mes/pro/process',
    method: 'put',
    data: data
  })
}

// 删除工序
export function delProProcess(processId) {
  return request({
    url: '/mes/pro/process/' + processId,
    method: 'delete'
  })
}

// 启用工序
export function enableProcess(processId) {
  return request({
    url: '/mes/pro/process/' + processId + '/enable',
    method: 'put'
  })
}

// 停用工序
export function disableProcess(processId) {
  return request({
    url: '/mes/pro/process/' + processId + '/disable',
    method: 'put'
  })
}

// 发布计划
export function publishPlan(planId, workshopId, workshopName) {
  return request({
    url: '/mes/pro/plan/' + planId + '/publish',
    method: 'put',
    params: {
      workshopId,
      workshopName
    }
  })
}

// 开始执行计划
export function startPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/start',
    method: 'put'
  })
}

// 完成计划
export function completePlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/complete',
    method: 'put'
  })
}

// 取消计划
export function cancelPlan(planId) {
  return request({
    url: '/mes/pro/plan/' + planId + '/cancel',
    method: 'put'
  })
}

// ==================== 生产工单 API ====================

// 查询工单列表
export function listProWorkorder(query) {
  return request({
    url: '/mes/pro/workorder/list',
    method: 'get',
    params: query
  })
}

// 查询工单详细
export function getProWorkorder(workorderId) {
  return request({
    url: '/mes/pro/workorder/' + workorderId,
    method: 'get'
  })
}

// 新增工单
export function addProWorkorder(data) {
  return request({
    url: '/mes/pro/workorder',
    method: 'post',
    data: data
  })
}

// 修改工单
export function updateProWorkorder(data) {
  return request({
    url: '/mes/pro/workorder',
    method: 'put',
    data: data
  })
}

// 删除工单
export function delProWorkorder(workorderId) {
  return request({
    url: '/mes/pro/workorder/' + workorderId,
    method: 'delete'
  })
}

// 工单下达
export function releaseWorkorder(workorderId) {
  return request({
    url: '/mes/pro/workorder/release/' + workorderId,
    method: 'put'
  })
}

// 工单关闭
export function closeWorkorder(workorderId) {
  return request({
    url: '/mes/pro/workorder/close/' + workorderId,
    method: 'put'
  })
}

// ==================== 生产任务 API ====================

// 查询任务列表
export function listProTask(query) {
  return request({
    url: '/mes/pro/task/list',
    method: 'get',
    params: query
  })
}

// 查询任务详细
export function getProTask(taskId) {
  return request({
    url: '/mes/pro/task/' + taskId,
    method: 'get'
  })
}

// 新增任务
export function addProTask(data) {
  return request({
    url: '/mes/pro/task',
    method: 'post',
    data: data
  })
}

// 修改任务
export function updateProTask(data) {
  return request({
    url: '/mes/pro/task',
    method: 'put',
    data: data
  })
}

// 删除任务
export function delProTask(taskId) {
  return request({
    url: '/mes/pro/task/' + taskId,
    method: 'delete'
  })
}

// 开始任务
export function startTask(taskId) {
  return request({
    url: '/mes/pro/task/start/' + taskId,
    method: 'put'
  })
}

// 完成任务
export function completeTask(taskId) {
  return request({
    url: '/mes/pro/task/complete/' + taskId,
    method: 'put'
  })
}

// ==================== 工艺路线 API ====================

// 查询工艺路线列表
export function listProRoute(query) {
  return request({
    url: '/mes/pro/route/list',
    method: 'get',
    params: query
  })
}

// 查询工艺路线详细
export function getProRoute(routeId) {
  return request({
    url: '/mes/pro/route/' + routeId,
    method: 'get'
  })
}

// 根据编码查询工艺路线
export function getProRouteByCode(routeCode) {
  return request({
    url: '/mes/pro/route/byCode/' + routeCode,
    method: 'get'
  })
}

// 根据产品ID查询工艺路线
export function getProRouteByItem(itemId) {
  return request({
    url: '/mes/pro/route/byItem/' + itemId,
    method: 'get'
  })
}

// 查询默认工艺路线
export function getDefaultRoute(itemId) {
  return request({
    url: '/mes/pro/route/default/' + itemId,
    method: 'get'
  })
}

// 新增工艺路线
export function addProRoute(data) {
  return request({
    url: '/mes/pro/route',
    method: 'post',
    data: data
  })
}

// 修改工艺路线
export function updateProRoute(data) {
  return request({
    url: '/mes/pro/route',
    method: 'put',
    data: data
  })
}

// 删除工艺路线
export function delProRoute(routeId) {
  return request({
    url: '/mes/pro/route/' + routeId,
    method: 'delete'
  })
}

// 批量删除工艺路线
export function delProRoutes(routeIds) {
  return request({
    url: '/mes/pro/route/batch/' + routeIds,
    method: 'delete'
  })
}

// ==================== 生产报工 API ====================

// 查询报工列表
export function listProFeedback(query) {
  return request({
    url: '/mes/pro/feedback/list',
    method: 'get',
    params: query
  })
}

// 查询报工详细
export function getProFeedback(feedbackId) {
  return request({
    url: '/mes/pro/feedback/' + feedbackId,
    method: 'get'
  })
}

// 新增报工
export function addProFeedback(data) {
  return request({
    url: '/mes/pro/feedback',
    method: 'post',
    data: data
  })
}

// 修改报工
export function updateProFeedback(data) {
  return request({
    url: '/mes/pro/feedback',
    method: 'put',
    data: data
  })
}

// 删除报工
export function delProFeedback(feedbackId) {
  return request({
    url: '/mes/pro/feedback/' + feedbackId,
    method: 'delete'
  })
}

// 批量删除报工
export function delProFeedbacks(feedbackIds) {
  return request({
    url: '/mes/pro/feedback/batch/' + feedbackIds,
    method: 'delete'
  })
}

// 审核报工
export function approveFeedback(feedbackId, status) {
  return request({
    url: '/mes/pro/feedback/approve/' + feedbackId,
    method: 'put',
    params: { status }
  })
}
