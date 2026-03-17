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
    if (!token && store.state.user && store.state.user.token) {
      token = store.state.user.token
    }
    console.log('=== Request Debug ===')
    console.log('URL:', config.url)
    console.log('Token:', token ? token.substring(0, 30) + '...' : 'null')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
      console.log('Authorization header set:', config.headers['Authorization'].substring(0, 40) + '...')
    } else {
      console.warn('No token available!')
    }
    console.log('=== End Request Debug ===')
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
    
    // 如果返回的不是JSON格式（比如HTML错误页面），直接返回
    if (typeof res !== 'object' || res === null) {
      return response.data
    }
    
    // 如果返回的状态码不是 200，说明出错了
    if (res.code !== 200) {
      Message.error(res.msg || '请求失败')
      
      // 401: 未登录或 token 过期
      if (res.code === 401) {
        handleLogout('登录已过期，请重新登录')
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    
    // 返回整个响应对象，包含 code, msg, data/total/rows 等
    return res
  },
  error => {
    console.error('响应错误:', error)
    
    // 获取错误状态码和消息
    const status = error.response && error.response.status
    const msg = (error.response && error.response.data && error.response.data.msg) || error.message || '网络错误'
    
    // HTTP 401: 未授权（token过期）
    if (status === 401) {
      handleLogout('登录已过期，请重新登录')
      return Promise.reject(error)
    }
    
    // HTTP 403: 禁止访问（未登录）
    if (status === 403) {
      handleLogout('请先登录')
      return Promise.reject(error)
    }
    
    // HTTP 500: 服务器错误
    if (status === 500) {
      Message.error('服务器内部错误，请联系管理员')
      return Promise.reject(error)
    }
    
    // 其他错误
    Message.error(msg)
    return Promise.reject(error)
  }
)

/**
 * 处理登出逻辑
 */
function handleLogout(message) {
  Message.error(message)
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
