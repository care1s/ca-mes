// 权限控制 - carels
import router from './router'
import store from './store'

// 白名单，不需要登录的页面
const whiteList = ['/login', '/404']

router.beforeEach((to, from, next) => {
  // 获取token
  const token = localStorage.getItem('mes-token')
  
  if (token) {
    // 有token，已登录
    if (to.path === '/login') {
      // 已登录且要去登录页，重定向到首页
      next('/')
    } else {
      // 已登录，放行
      next()
    }
  } else {
    // 没有token，未登录
    if (whiteList.includes(to.path)) {
      // 在白名单中，放行
      next()
    } else {
      // 不在白名单中，重定向到登录页
      next('/login')
    }
  }
})

router.afterEach(() => {
  // 路由切换后的一些操作
})

