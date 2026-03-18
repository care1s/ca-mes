// 权限控制 - carels
import router from './router'
import store from './store'

// 白名单，不需要登录的页面
const whiteList = ['/login', '/404']

router.beforeEach((to, from, next) => {
  // 获取token（先从store获取，如果不存在则从localStorage获取）
  let token = null
  if (store.state.user && store.state.user.token) {
    token = store.state.user.token
  }
  if (!token) {
    token = localStorage.getItem('mes-token')
  }
  
  // 详细调试日志
  console.log('=== 路由守卫详细调试 ===')
  console.log('from:', from.path)
  console.log('to:', to.path)
  console.log('store.user:', store.state.user)
  console.log('store.user.token:', store.state.user ? store.state.user.token : 'store.user不存在')
  console.log('localStorage mes-token:', localStorage.getItem('mes-token'))
  console.log('最终token:', token ? token.substring(0, 30) + '...' : 'null')
  console.log('========================')
  
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
      console.log('无token，跳转到登录页')
      next('/login')
    }
  }
})

router.afterEach(() => {
  // 路由切换后的一些操作
})

