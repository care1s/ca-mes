/**
 * API调试工具 - 用于排查登录跳转问题
 * @author carels
 * @version V9.0
 * @date 2026-03-21
 */

import request from './request'

/**
 * 检查token是否有效
 */
export function checkToken() {
  const token = localStorage.getItem('mes-token')
  console.log('========== Token Debug ==========')
  console.log('Token存在:', !!token)
  if (token) {
    console.log('Token长度:', token.length)
    console.log('Token前缀:', token.substring(0, 50) + '...')

    // 解析JWT payload
    try {
      const payload = JSON.parse(atob(token.split('.')[1]))
      console.log('Token payload:', payload)
      console.log('Token过期时间:', new Date(payload.exp * 1000).toLocaleString())
      console.log('Token是否过期:', payload.exp * 1000 < Date.now())
    } catch (e) {
      console.error('Token解析失败:', e)
    }
  }
  console.log('=================================')
  return token
}

/**
 * 测试基础接口（不需要权限）
 */
export async function testPublicApi() {
  console.log('========== 测试公共接口 ==========')
  try {
    // 注意：这里需要根据实际情况调整URL
    const response = await fetch('/auth/info', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' }
    })
    console.log('公共接口响应状态:', response.status)
    return response.status === 200
  } catch (error) {
    console.error('公共接口测试失败:', error)
    return false
  }
}

/**
 * 测试受保护接口
 */
export async function testProtectedApi() {
  console.log('========== 测试受保护接口 ==========')
  try {
    const response = await request({
      url: '/mes/pro/task/list',
      method: 'get',
      params: { pageSize: 1 }
    })
    console.log('受保护接口响应:', response)
    return response.code === 200
  } catch (error) {
    console.error('受保护接口测试失败:', error)
    return false
  }
}

/**
 * 测试新增接口（第三阶段）
 */
export async function testNewApi() {
  console.log('========== 测试新增接口 ==========')
  try {
    const response = await request({
      url: '/mes/plan/scheduling/capacity',
      method: 'get',
      params: { startDate: '2026-03-01', endDate: '2026-03-31' }
    })
    console.log('新增接口响应:', response)
    return response.code === 200
  } catch (error) {
    console.error('新增接口测试失败:', error)
    return false
  }
}

/**
 * 完整的诊断流程
 */
export async function runDiagnostics() {
  console.log('=================================')
  console.log('开始运行API诊断...')
  console.log('=================================')

  // 1. 检查token
  const token = checkToken()
  if (!token) {
    console.error('❌ Token不存在，请先登录')
    return { ok: false, reason: 'NO_TOKEN' }
  }

  // 2. 检查token是否过期
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    if (payload.exp * 1000 < Date.now()) {
      console.error('❌ Token已过期，请重新登录')
      return { ok: false, reason: 'TOKEN_EXPIRED' }
    }
  } catch (e) {
    console.error('❌ Token格式错误')
    return { ok: false, reason: 'TOKEN_INVALID' }
  }

  console.log('✅ Token检查通过')

  // 3. 测试受保护接口
  const protectedOk = await testProtectedApi()
  if (!protectedOk) {
    console.error('❌ 受保护接口测试失败')
    return { ok: false, reason: 'PROTECTED_API_FAILED' }
  }
  console.log('✅ 受保护接口测试通过')

  // 4. 测试新增接口
  const newApiOk = await testNewApi()
  if (!newApiOk) {
    console.error('❌ 新增接口测试失败')
    return { ok: false, reason: 'NEW_API_FAILED' }
  }
  console.log('✅ 新增接口测试通过')

  console.log('=================================')
  console.log('✅ 所有诊断通过！')
  console.log('=================================')
  return { ok: true }
}

// 导出到全局以便在浏览器控制台使用
if (typeof window !== 'undefined') {
  window.apiDebug = {
    checkToken,
    testPublicApi,
    testProtectedApi,
    testNewApi,
    runDiagnostics
  }
  console.log('API调试工具已加载，在控制台使用 window.apiDebug 访问')
}
