/**
 * MES制造执行系统前端主入口
 * 作者: carels
 * 版本: V9.0
 * 日期: 2026-03-15
 * 版权所有: carels
 */

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// Element UI
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

// 全局样式
import '@/styles/index.scss'

// 权限控制
import './permission'

Vue.use(ElementUI, { size: 'small', zIndex: 3000 })

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
