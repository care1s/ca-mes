/**
 * MES系统 API 服务层 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 * @copyright carels
 */

import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'
import store from '@/store'

// 创建 axios 实例
const service = axios.create({
  baseURL: '',  // 使用代理，不需要 baseURL
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 从 localStorage 获取 token，如果没有则从 store 获取
    let token = localStorage.getItem('mes-token')
    console.log('[Request Interceptor] Token from localStorage:', token ? token.substring(0, 30) + '...' : 'null')
    if (!token && store.state.user && store.state.user.token) {
      token = store.state.user.token
      console.log('[Request Interceptor] Token from store:', token ? token.substring(0, 30) + '...' : 'null')
    }
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
      console.log('[Request Interceptor] Added Authorization header:', 'Bearer ' + token.substring(0, 30) + '...')
    } else {
      console.warn('[Request Interceptor] No token found!')
    }
    console.log('[Request Interceptor] Request URL:', config.url)
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    console.log('[Response Interceptor] Response:', response.config.url, res)

    // 如果返回的不是JSON格式（比如HTML错误页面），直接返回
    if (typeof res !== 'object' || res === null) {
      return response.data
    }

    // 如果返回的状态码不是 200，说明出错了
    if (res.code !== 200) {
      Message.error(res.msg || '请求失败')

      // 401: 未登录或 token 过期 - 只有业务层面返回401才跳转
      if (res.code === 401 && isAuthError(res.msg)) {
        console.error('[Response Interceptor] 收到鉴权失败响应，执行登出...', res.msg)
        handleLogout('登录已过期，请重新登录')
      }

      return Promise.reject(new Error(res.msg || '请求失败'))
    }

    // 返回整个响应对象，包含 code, msg, data/total/rows 等
    return res
  },
  error => {
    console.error('[Response Interceptor] 响应错误:', error.message)

    // 获取错误状态码和消息
    const status = error.response && error.response.status
    const resData = error.response && error.response.data
    const msg = (resData && resData.msg) || error.message || '网络错误'

    console.error('[Response Interceptor] Error status:', status, 'msg:', msg)

    // HTTP 401: 未授权（token过期）- 只有明确是鉴权错误才跳转
    if (status === 401 && isAuthError(msg)) {
      console.error('[Response Interceptor] HTTP 401鉴权错误，执行登出...')
      handleLogout('登录已过期，请重新登录')
      return Promise.reject(error)
    }

    // HTTP 403: 禁止访问 - 显示权限不足，不跳转
    if (status === 403) {
      Message.error('权限不足，无法访问')
      return Promise.reject(error)
    }

    // HTTP 500: 服务器错误
    if (status === 500) {
      Message.error(msg || '服务器内部错误，请联系管理员')
      return Promise.reject(error)
    }

    // 其他错误（如 404, 400等）只显示错误信息
    Message.error(msg)
    return Promise.reject(error)
  }
)

/**
 * 判断是否为鉴权错误（需要跳转登录页）
 * @param {string} msg 错误消息
 * @returns {boolean}
 */
function isAuthError(msg) {
  if (!msg) return false
  const authErrorKeywords = [
    '请先登录',
    '登录已过期',
    'token',
    'Token',
    '未登录',
    '无token',
    '未授权',
    '认证失败',
    '鉴权失败'
  ]
  return authErrorKeywords.some(keyword => msg.includes(keyword))
}

/**
 * 处理登出逻辑 - 只有鉴权失败时调用
 */
function handleLogout(message) {
  Message.error(message)
  console.log('鉴权失败，清理token并跳转登录页...')

  // 清除 localStorage token
  localStorage.removeItem('mes-token')

  // 清除 store 中的 token（如果存在）
  if (store.state && store.state.user) {
    store.state.user.token = null
  }

  // 强制跳转到登录页（使用 window.location 避免路由守卫干扰）
  setTimeout(() => {
    window.location.href = '/login'
  }, 1000)
}

export default service
