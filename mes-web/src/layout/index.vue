<template>
  <div :class="themeClass" class="app-wrapper">
    <!-- 顶部导航栏 - 圆角现代风格 -->
    <div class="top-header">
      <div class="logo" @click="goHome">
        <i class="el-icon-s-home"></i>
        <span>MES系统</span>
        <span class="version">V1.0</span>
      </div>
      
      <div class="top-menu" ref="menuContainer">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          :background-color="menuBg"
          :text-color="menuText"
          :active-text-color="menuActiveText"
          router
        >
          <!-- 主数据 - 无图标 -->
          <el-submenu index="/md" v-if="!hiddenMenus.includes('/md')">
            <template slot="title">
              <span>主数据</span>
            </template>
            <el-menu-item index="/md/item">物料信息</el-menu-item>
            <el-menu-item index="/md/workshop">车间管理</el-menu-item>
            <el-menu-item index="/md/productionLine">生产线管理</el-menu-item>
            <el-menu-item index="/md/workstation">工作站</el-menu-item>
            <el-menu-item index="/md/client">客户管理</el-menu-item>
            <el-menu-item index="/md/vendor">供应商</el-menu-item>
          </el-submenu>
          
          <!-- 生产执行 - 无图标 -->
          <el-submenu index="/pro" v-if="!hiddenMenus.includes('/pro')">
            <template slot="title">
              <span>生产执行</span>
            </template>
            <el-menu-item index="/pro/workorder">生产工单</el-menu-item>
            <el-menu-item index="/pro/task">生产任务</el-menu-item>
            <el-menu-item index="/pro/feedback">生产报工</el-menu-item>
            <el-menu-item index="/pro/route">工艺路线</el-menu-item>
            <el-menu-item index="/pro/andon">异常管理</el-menu-item>
          </el-submenu>
          
          <!-- 仓储物流 - 无图标 -->
          <el-submenu index="/wm" v-if="!hiddenMenus.includes('/wm')">
            <template slot="title">
              <span>仓储物流</span>
            </template>
            <el-menu-item index="/wm/warehouse">仓库管理</el-menu-item>
            <el-menu-item index="/wm/stock">库存查询</el-menu-item>
            <el-menu-item index="/wm/recpt">入库管理</el-menu-item>
            <el-menu-item index="/wm/issue">出库管理</el-menu-item>
            <el-menu-item index="/wm/batch">批次管理</el-menu-item>
          </el-submenu>
          
          <!-- 质量管理 - 无图标 -->
          <el-submenu index="/qc" v-if="!hiddenMenus.includes('/qc')">
            <template slot="title">
              <span>质量管理</span>
            </template>
            <el-menu-item index="/qc/iqc">来料检验</el-menu-item>
            <el-menu-item index="/qc/ipqc">过程检验</el-menu-item>
            <el-menu-item index="/qc/oqc">出货检验</el-menu-item>
            <el-menu-item index="/qc/template">检验模板</el-menu-item>
            <el-menu-item index="/qc/defect">缺陷记录</el-menu-item>
          </el-submenu>
          
          <!-- 设备运维 - 无图标 -->
          <el-submenu index="/dv" v-if="!hiddenMenus.includes('/dv')">
            <template slot="title">
              <span>设备运维</span>
            </template>
            <el-menu-item index="/dv/machinery">设备台账</el-menu-item>
            <el-menu-item index="/dv/check">点检管理</el-menu-item>
            <el-menu-item index="/dv/repair">维修管理</el-menu-item>
          </el-submenu>
          
          <!-- 报表分析 - 无图标 -->
          <el-submenu index="/report" v-if="!hiddenMenus.includes('/report')">
            <template slot="title">
              <span>报表分析</span>
            </template>
            <el-menu-item index="/report/chart">生产报表</el-menu-item>
            <el-menu-item index="/report/workorder-board">工单看板</el-menu-item>
            <el-menu-item index="/report/quality-board">质量看板</el-menu-item>
          </el-submenu>
          
          <!-- 更多菜单 - 隐藏的菜单项 -->
          <el-submenu index="/more" v-if="hiddenMenus.length > 0" class="more-menu">
            <template slot="title">
              <span>更多</span>
              <el-badge :value="hiddenMenus.length" class="more-badge" type="primary"></el-badge>
              <i class="el-icon-arrow-down more-arrow"></i>
            </template>
            
            <!-- 主数据 -->
            <div v-if="hiddenMenus.includes('/md')" class="menu-group">
              <div class="menu-group-title">主数据</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/md/item">物料信息</el-menu-item>
                <el-menu-item index="/md/workshop">车间管理</el-menu-item>
                <el-menu-item index="/md/productionLine">生产线管理</el-menu-item>
                <el-menu-item index="/md/workstation">工作站</el-menu-item>
                <el-menu-item index="/md/client">客户管理</el-menu-item>
                <el-menu-item index="/md/vendor">供应商</el-menu-item>
              </div>
            </div>
            
            <!-- 生产执行 -->
            <div v-if="hiddenMenus.includes('/pro')" class="menu-group">
              <div class="menu-group-title">生产执行</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/pro/workorder">生产工单</el-menu-item>
                <el-menu-item index="/pro/task">生产任务</el-menu-item>
                <el-menu-item index="/pro/feedback">生产报工</el-menu-item>
                <el-menu-item index="/pro/route">工艺路线</el-menu-item>
                <el-menu-item index="/pro/andon">异常管理</el-menu-item>
              </div>
            </div>
            
            <!-- 仓储物流 -->
            <div v-if="hiddenMenus.includes('/wm')" class="menu-group">
              <div class="menu-group-title">仓储物流</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/wm/warehouse">仓库管理</el-menu-item>
                <el-menu-item index="/wm/stock">库存查询</el-menu-item>
                <el-menu-item index="/wm/recpt">入库管理</el-menu-item>
                <el-menu-item index="/wm/issue">出库管理</el-menu-item>
                <el-menu-item index="/wm/batch">批次管理</el-menu-item>
              </div>
            </div>
            
            <!-- 质量管理 -->
            <div v-if="hiddenMenus.includes('/qc')" class="menu-group">
              <div class="menu-group-title">质量管理</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/qc/iqc">来料检验</el-menu-item>
                <el-menu-item index="/qc/ipqc">过程检验</el-menu-item>
                <el-menu-item index="/qc/oqc">出货检验</el-menu-item>
                <el-menu-item index="/qc/template">检验模板</el-menu-item>
                <el-menu-item index="/qc/defect">缺陷记录</el-menu-item>
              </div>
            </div>
            
            <!-- 设备运维 -->
            <div v-if="hiddenMenus.includes('/dv')" class="menu-group">
              <div class="menu-group-title">设备运维</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/dv/machinery">设备台账</el-menu-item>
                <el-menu-item index="/dv/check">点检管理</el-menu-item>
                <el-menu-item index="/dv/repair">维修管理</el-menu-item>
              </div>
            </div>
            
            <!-- 报表分析 -->
            <div v-if="hiddenMenus.includes('/report')" class="menu-group">
              <div class="menu-group-title">报表分析</div>
              <div class="menu-group-divider"></div>
              <div class="menu-group-content">
                <el-menu-item index="/report/chart">生产报表</el-menu-item>
                <el-menu-item index="/report/workorder-board">工单看板</el-menu-item>
                <el-menu-item index="/report/quality-board">质量看板</el-menu-item>
              </div>
            </div>
          </el-submenu>
        </el-menu>
      </div>
      
      <div class="right-tools">
        <!-- 主题切换按钮 -->
        <el-tooltip :content="isDark ? '切换亮色模式' : '切换暗色模式'" placement="bottom">
          <div class="theme-switch" @click="toggleTheme">
            <i :class="isDark ? 'el-icon-sunny' : 'el-icon-moon'"></i>
          </div>
        </el-tooltip>
        
        <!-- 系统管理齿轮图标 -->
        <el-dropdown trigger="click" @command="handleSystemCommand">
          <div class="system-menu">
            <i class="el-icon-s-tools"></i>
          </div>
          <el-dropdown-menu slot="dropdown" class="system-dropdown">
            <el-dropdown-item command="/system/user">用户管理</el-dropdown-item>
            <el-dropdown-item command="/system/role">角色管理</el-dropdown-item>
            <el-dropdown-item command="/system/menu">菜单管理</el-dropdown-item>
            <el-dropdown-item command="/system/dept">部门管理</el-dropdown-item>
            <el-dropdown-item command="/system/dict">字典管理</el-dropdown-item>
            <el-dropdown-item command="/system/config">参数设置</el-dropdown-item>
            <el-dropdown-item command="/system/log">操作日志</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        
        <!-- 用户信息 -->
        <el-dropdown trigger="click">
          <span class="user-info">
            <i class="el-icon-user-solid"></i>
            <span>管理员</span>
            <i class="el-icon-arrow-down"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人中心</el-dropdown-item>
            <el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    
    <!-- 主内容区 -->
    <div class="main-content">
      <app-main />
    </div>
    
    <!-- 页脚 -->
    <div class="footer">
      <span>© 2026 MES制造执行系统 - carels版权所有 V1.0</span>
    </div>
  </div>
</template>

<script>
import AppMain from './components/AppMain'

export default {
  name: 'Layout',
  components: {
    AppMain
  },
  data() {
    return {
      isDark: false,
      theme: 'light-purple',
      menuItems: [
        { path: '/md', title: '主数据', priority: 1 },
        { path: '/pro', title: '生产执行', priority: 2 },
        { path: '/wm', title: '仓储物流', priority: 3 },
        { path: '/qc', title: '质量管理', priority: 4 },
        { path: '/dv', title: '设备运维', priority: 5 },
        { path: '/report', title: '报表分析', priority: 6 }
      ],
      visibleMenus: [],
      hiddenMenus: []
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    themeClass() {
      return {
        'theme-light': !this.isDark,
        'theme-dark': this.isDark
      }
    },
    menuBg() {
      return this.isDark ? '#1A1A2E' : '#FFFFFF'
    },
    menuText() {
      return this.isDark ? '#EAEAEA' : '#4A5568'
    },
    menuActiveText() {
      return this.isDark ? '#A78BFA' : '#7C3AED'
    }
  },
  mounted() {
    const savedTheme = localStorage.getItem('mes-theme')
    if (savedTheme) this.theme = savedTheme
    const savedDark = localStorage.getItem('mes-dark')
    if (savedDark) this.isDark = savedDark === 'true'
    
    // 计算可见菜单
    this.calculateVisibleMenus()
    
    // 监听窗口大小变化
    window.addEventListener('resize', this.calculateVisibleMenus)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.calculateVisibleMenus)
  },
  methods: {
    goHome() {
      this.$router.push('/dashboard')
    },
    toggleTheme() {
      this.isDark = !this.isDark
      localStorage.setItem('mes-dark', this.isDark)
      this.$message.success(this.isDark ? '已切换到暗色模式' : '已切换到亮色模式')
    },
    handleSystemCommand(command) {
      if (command === 'logout') {
        this.logout()
      } else {
        this.$router.push(command)
      }
    },
    logout() {
      this.$store.dispatch('user/logout').then(() => {
        this.$message.success('退出登录成功')
        this.$router.push('/login')
      })
    },
    calculateVisibleMenus() {
      // 根据屏幕宽度决定显示多少个菜单
      const width = window.innerWidth
      let visibleCount = 6
      
      if (width < 1200) {
        visibleCount = 3
      } else if (width < 1400) {
        visibleCount = 4
      } else if (width < 1600) {
        visibleCount = 5
      }
      
      // 按优先级排序
      const sortedMenus = [...this.menuItems].sort((a, b) => a.priority - b.priority)
      
      this.visibleMenus = sortedMenus.slice(0, visibleCount)
      this.hiddenMenus = sortedMenus.slice(visibleCount).map(item => item.path)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

// 顶部导航栏 - 圆角现代风格
.top-header {
  height: 70px;
  display: flex;
  align-items: center;
  padding: 0 30px;
  transition: all 0.3s ease;
  border-radius: 0 0 20px 20px;
  margin: 0 10px;
  
  .logo {
    width: 200px;
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;
    padding: 10px 15px;
    border-radius: 12px;
    
    i {
      font-size: 26px;
      margin-right: 10px;
    }
    
    span {
      font-size: 20px;
      font-weight: 700;
    }
    
    .version {
      font-size: 11px;
      margin-left: 8px;
      padding: 3px 8px;
      border-radius: 20px;
      font-weight: 500;
    }
    
    &:hover {
      transform: scale(1.02);
    }
  }
  
  .top-menu {
    flex: 1;
    margin: 0 20px;
    
    .el-menu {
      border-bottom: none;
      background: transparent !important;
      border-radius: 16px;
      padding: 5px 10px;
      display: flex;
      align-items: center;
      justify-content: center;
      
      .el-menu-item, .el-submenu {
        flex: 1;
        max-width: 140px;
        min-width: 100px;
        text-align: center;
      }
      
      .el-menu-item, .el-submenu__title {
        height: 48px;
        line-height: 48px;
        font-size: 15px;
        font-weight: 500;
        border-radius: 25px;
        margin: 0 4px;
        padding: 0 !important;
        letter-spacing: 0.3px;
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        
        i {
          margin-right: 6px;
          font-size: 16px;
        }
        
        &:hover {
          border-radius: 25px;
        }
      }
      
      .el-submenu.is-active .el-submenu__title {
        border-radius: 25px;
      }
    }
  }
  
  .right-tools {
    display: flex;
    align-items: center;
    gap: 15px;
    
    .theme-switch,
    .system-menu {
      width: 42px;
      height: 42px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      cursor: pointer;
      border-radius: 50%;
      transition: all 0.3s ease;
      
      &:hover {
        transform: scale(1.1) rotate(15deg);
      }
    }
    
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 10px 18px;
      border-radius: 25px;
      transition: all 0.3s ease;
      font-weight: 500;
      
      i:first-child {
        margin-right: 8px;
        font-size: 18px;
      }
      
      i:last-child {
        margin-left: 8px;
        font-size: 12px;
      }
      
      &:hover {
        transform: translateY(-2px);
      }
    }
  }
}

// 主内容区
.main-content {
  flex: 1;
  overflow: auto;
  padding: 20px;
  margin: 0 15px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

// 页脚
.footer {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  margin: 10px 15px;
  border-radius: 15px;
  transition: all 0.3s ease;
}

// ========== 亮色主题 - 紫色渐变 ==========
.theme-light {
  background: linear-gradient(135deg, #F5F3FF 0%, #E0E7FF 100%);
  
  .top-header {
    background: linear-gradient(135deg, #FFFFFF 0%, #FAFAFA 100%);
    box-shadow: 0 4px 20px rgba(124, 58, 237, 0.08);
    
    .logo {
      color: #7C3AED;
      background: rgba(124, 58, 237, 0.08);
      
      .version {
        background: linear-gradient(135deg, #7C3AED 0%, #A78BFA 100%);
        color: #fff;
      }
      
      &:hover {
        background: rgba(124, 58, 237, 0.15);
      }
    }
  }
  
  .top-menu {
    .el-menu {
      background: rgba(255, 255, 255, 0.6) !important;
      backdrop-filter: blur(10px);
      
      .el-menu-item, .el-submenu__title {
        color: #4A5568 !important;
        
        &:hover {
          color: #7C3AED !important;
          background: rgba(124, 58, 237, 0.1) !important;
        }
      }
      
      .el-menu-item.is-active, .el-submenu.is-active .el-submenu__title {
        color: #7C3AED !important;
        background: linear-gradient(135deg, rgba(124, 58, 237, 0.15) 0%, rgba(167, 139, 250, 0.1) 100%) !important;
        font-weight: 600;
        box-shadow: 0 2px 8px rgba(124, 58, 237, 0.15);
      }
    }
  }
  
  .right-tools {
    .theme-switch {
      color: #7C3AED;
      background: rgba(124, 58, 237, 0.1);
      
      &:hover {
        background: rgba(124, 58, 237, 0.2);
        box-shadow: 0 4px 12px rgba(124, 58, 237, 0.25);
      }
    }
    
    .system-menu {
      color: #4A5568;
      background: rgba(74, 85, 104, 0.08);
      
      &:hover {
        color: #7C3AED;
        background: rgba(124, 58, 237, 0.15);
      }
    }
    
    .user-info {
      color: #4A5568;
      background: rgba(74, 85, 104, 0.06);
      border: 1px solid rgba(124, 58, 237, 0.1);
      
      &:hover {
        color: #7C3AED;
        background: rgba(124, 58, 237, 0.1);
        border-color: rgba(124, 58, 237, 0.3);
        box-shadow: 0 4px 12px rgba(124, 58, 237, 0.12);
      }
    }
  }
  
  .main-content {
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(20px);
    box-shadow: 0 8px 32px rgba(124, 58, 237, 0.06);
  }
  
  .footer {
    background: rgba(255, 255, 255, 0.6);
    color: #7C3AED;
    border: 1px solid rgba(124, 58, 237, 0.1);
  }
}

// ========== 暗色主题 - 深蓝紫 ==========
.theme-dark {
  background: linear-gradient(135deg, #0F0F23 0%, #1A1A3E 100%);
  
  .top-header {
    background: linear-gradient(135deg, #1A1A2E 0%, #16213E 100%);
    box-shadow: 0 4px 20px rgba(167, 139, 250, 0.1);
    
    .logo {
      color: #A78BFA;
      background: rgba(167, 139, 250, 0.1);
      
      .version {
        background: linear-gradient(135deg, #A78BFA 0%, #C4B5FD 100%);
        color: #1A1A2E;
      }
      
      &:hover {
        background: rgba(167, 139, 250, 0.2);
      }
    }
  }
  
  .top-menu {
    .el-menu {
      background: rgba(26, 26, 46, 0.6) !important;
      backdrop-filter: blur(10px);
      
      .el-menu-item, .el-submenu__title {
        color: #EAEAEA !important;
        
        &:hover {
          color: #A78BFA !important;
          background: rgba(167, 139, 250, 0.15) !important;
        }
      }
      
      .el-menu-item.is-active, .el-submenu.is-active .el-submenu__title {
        color: #A78BFA !important;
        background: linear-gradient(135deg, rgba(167, 139, 250, 0.2) 0%, rgba(196, 181, 253, 0.1) 100%) !important;
        font-weight: 600;
        box-shadow: 0 2px 8px rgba(167, 139, 250, 0.2);
      }
    }
  }
  
  .right-tools {
    .theme-switch {
      color: #FCD34D;
      background: rgba(252, 211, 77, 0.15);
      
      &:hover {
        background: rgba(252, 211, 77, 0.25);
        box-shadow: 0 4px 12px rgba(252, 211, 77, 0.25);
      }
    }
    
    .system-menu {
      color: #EAEAEA;
      background: rgba(234, 234, 234, 0.08);
      
      &:hover {
        color: #A78BFA;
        background: rgba(167, 139, 250, 0.2);
      }
    }
    
    .user-info {
      color: #EAEAEA;
      background: rgba(234, 234, 234, 0.06);
      border: 1px solid rgba(167, 139, 250, 0.2);
      
      &:hover {
        color: #A78BFA;
        background: rgba(167, 139, 250, 0.15);
        border-color: rgba(167, 139, 250, 0.4);
        box-shadow: 0 4px 12px rgba(167, 139, 250, 0.18);
      }
    }
  }
  
  .main-content {
    background: rgba(26, 26, 46, 0.7);
    backdrop-filter: blur(20px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.25);
  }
  
  .footer {
    background: rgba(26, 26, 46, 0.6);
    color: #A78BFA;
    border: 1px solid rgba(167, 139, 250, 0.2);
  }
}

// 下拉菜单圆角
::v-deep {
  .el-menu--horizontal {
    .el-menu--popup {
      border-radius: 12px;
      padding: 8px 0;
      
      .el-menu-item {
        border-radius: 8px;
        margin: 2px 8px;
        height: 40px;
        line-height: 40px;
      }
    }
  }
  
  .el-dropdown-menu {
    border-radius: 12px;
    padding: 8px 0;
    
    .el-dropdown-menu__item {
      border-radius: 8px;
      margin: 2px 8px;
      padding: 8px 16px;
    }
  }
}

// 更多菜单特殊样式
::v-deep .more-menu {
  .el-menu--popup {
    min-width: 400px !important;
    width: auto !important;
    max-height: 600px;
    overflow-y: auto;
    padding: 12px 0 !important;
    
    // 自定义滚动条
    &::-webkit-scrollbar {
      width: 6px;
    }
    
    &::-webkit-scrollbar-track {
      background: transparent;
    }
    
    &::-webkit-scrollbar-thumb {
      background: rgba(124, 58, 237, 0.3);
      border-radius: 3px;
    }
    
    .menu-group {
      padding: 12px 16px;
      
      &:not(:first-child) {
        border-top: 1px solid rgba(0, 0, 0, 0.08);
        margin-top: 8px;
        padding-top: 16px;
      }
      
      // 一级菜单标题 - 独占一行
      .menu-group-title {
        padding: 8px 0;
        font-size: 14px;
        font-weight: 600;
        color: #7C3AED;
        display: flex;
        align-items: center;
        margin-bottom: 8px;
        
        &::before {
          content: '';
          width: 4px;
          height: 16px;
          border-radius: 2px;
          background: linear-gradient(180deg, #7C3AED 0%, #A78BFA 100%);
          margin-right: 10px;
        }
      }
      
      // 分割线
      .menu-group-divider {
        height: 1px;
        background: linear-gradient(90deg, transparent, rgba(124, 58, 237, 0.3), transparent);
        margin: 8px 0 12px 0;
      }
      
      // 子菜单容器 - 网格布局，每行3个
      .menu-group-content {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 8px;
        padding: 0 4px;
      }
      
      // 子菜单项
      .el-menu-item {
        height: 36px !important;
        line-height: 36px !important;
        padding: 0 12px !important;
        font-size: 13px;
        border-radius: 8px;
        margin: 0 !important;
        text-align: center !important;
        display: flex !important;
        align-items: center !important;
        justify-content: center !important;
        min-width: 0 !important;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        
        &:hover {
          background: rgba(124, 58, 237, 0.1) !important;
        }
        
        &.is-active {
          background: rgba(124, 58, 237, 0.15) !important;
          color: #7C3AED !important;
          font-weight: 500;
        }
      }
    }
  }
}

// 暗色主题更多菜单
.theme-dark ::v-deep .more-menu {
  .el-menu--popup {
    .menu-group {
      &:not(:first-child) {
        border-top-color: rgba(255, 255, 255, 0.1);
      }
      
      .menu-group-title {
        color: #A78BFA;
        
        &::before {
          background: linear-gradient(180deg, #A78BFA 0%, #C4B5FD 100%);
        }
      }
      
      .menu-group-divider {
        background: linear-gradient(90deg, transparent, rgba(167, 139, 250, 0.4), transparent);
      }
      
      .el-menu-item {
        &:hover {
          background: rgba(167, 139, 250, 0.15) !important;
        }
        
        &.is-active {
          background: rgba(167, 139, 250, 0.2) !important;
          color: #A78BFA !important;
        }
      }
    }
    
    &::-webkit-scrollbar-thumb {
      background: rgba(167, 139, 250, 0.4);
    }
  }
}
</style>
