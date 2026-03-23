import request from './request'

// 查询销售订单列表
export function listSaleOrder(query) {
  return request({
    url: '/mes/sale/order/list',
    method: 'get',
    params: query
  })
}

// 查询销售订单详细
export function getSaleOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId,
    method: 'get'
  })
}

// 根据单号查询销售订单
export function getSaleOrderByNo(orderNo) {
  return request({
    url: '/mes/sale/order/byNo/' + orderNo,
    method: 'get'
  })
}

// 新增销售订单
export function addSaleOrder(data) {
  return request({
    url: '/mes/sale/order',
    method: 'post',
    data: data
  })
}

// 修改销售订单
export function updateSaleOrder(data) {
  return request({
    url: '/mes/sale/order',
    method: 'put',
    data: data
  })
}

// 删除销售订单
export function delSaleOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId,
    method: 'delete'
  })
}

// 批量删除销售订单
export function delBatchSaleOrder(orderIds) {
  return request({
    url: '/mes/sale/order/batch/' + orderIds,
    method: 'delete'
  })
}

// 提交订单
export function submitOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId + '/submit',
    method: 'put'
  })
}

// 审核订单
export function auditOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId + '/audit',
    method: 'put'
  })
}

// 取消订单
export function cancelOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId + '/cancel',
    method: 'put'
  })
}

// 完成订单
export function completeOrder(orderId) {
  return request({
    url: '/mes/sale/order/' + orderId + '/complete',
    method: 'put'
  })
}

// 关联生产计划
export function relatePlan(orderId, planId) {
  return request({
    url: '/mes/sale/order/' + orderId + '/relatePlan',
    method: 'put',
    params: { planId }
  })
}

// 查询可出库的订单列表
export function listAvailableForIssue() {
  return request({
    url: '/mes/sale/order/availableForIssue',
    method: 'get'
  })
}
