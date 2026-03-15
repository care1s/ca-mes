// MES系统状态管理 - carels
import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)

// 创建 axios 实例用于登录（不经过拦截器）
const loginRequest = axios.create({
  baseURL: '',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

export default new Vuex.Store({
  modules: {
    app: {
      state: {
        sidebar: {
          opened: true,
          withoutAnimation: false
        },
        device: 'desktop'
      },
      mutations: {
        TOGGLE_SIDEBAR: state => {
          state.sidebar.opened = !state.sidebar.opened
          state.sidebar.withoutAnimation = false
        }
      },
      actions: {
        toggleSideBar({ commit }) {
          commit('TOGGLE_SIDEBAR')
        },
        closeSideBar({ commit }, { withoutAnimation }) {
          commit('CLOSE_SIDEBAR', withoutAnimation)
        }
      }
    },
    user: {
      namespaced: true,
      state: {
        token: localStorage.getItem('mes-token') || '',
        name: localStorage.getItem('mes-user-name') || '',
        avatar: ''
      },
      mutations: {
        SET_TOKEN: (state, token) => {
          state.token = token
          localStorage.setItem('mes-token', token)
        },
        SET_NAME: (state, name) => {
          state.name = name
          localStorage.setItem('mes-user-name', name)
        },
        LOGOUT: (state) => {
          state.token = ''
          state.name = ''
          localStorage.removeItem('mes-token')
          localStorage.removeItem('mes-user-name')
        }
      },
      actions: {
        login({ commit }, userInfo) {
          const { username, password, terminalType, captcha, captchaKey } = userInfo
          console.log('开始登录请求:', { username, terminalType, captchaKey })
          return new Promise((resolve, reject) => {
            // 调用后端登录API
            loginRequest({
              url: '/auth/login',
              method: 'post',
              data: {
                username,
                password,
                terminalType,
                captcha,
                captchaKey
              }
            }).then(response => {
              console.log('登录响应:', response)
              const res = response.data
              if (res.code === 200) {
                const { token, userName } = res.data
                commit('SET_TOKEN', token)
                commit('SET_NAME', userName || username)
                resolve(res.data)
              } else {
                reject(new Error(res.msg || '登录失败'))
              }
            }).catch(error => {
              console.error('登录错误:', error)
              const msg = (error.response && error.response.data && error.response.data.msg) || error.message || '网络错误'
              reject(new Error(msg))
            })
          })
        },
        
        logout({ commit }) {
          return new Promise((resolve) => {
            commit('LOGOUT')
            resolve()
          })
        }
      }
    },
    settings: {
      state: {
        fixedHeader: false
      }
    }
  }
})
