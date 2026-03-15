/**
 * MES系统 API 服务层 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 * @copyright carels
 */

import axios from 'axios'
import { Message } from 'element-ui'

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
    // 从 localStorage 获取 token
    const token = localStorage.getItem('mes-token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
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
    
    // 如果返回的状态码不是 200，说明出错了
    if (res.code !== 200) {
      Message.error(res.msg || '请求失败')
      
      // 401: 未登录或 token 过期
      if (res.code === 401) {
        localStorage.removeItem('mes-token')
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'))
    }
    
    return res.data
  },
  error => {
    console.error('响应错误:', error)
    Message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service
