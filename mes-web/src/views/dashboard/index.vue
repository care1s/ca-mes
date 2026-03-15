<template>
  <div class="app-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">
          <i class="el-icon-s-home"></i>
          欢迎使用 MES 制造执行系统
        </h1>
        <p class="welcome-subtitle">Welcome to Manufacturing Execution System</p>
        <p class="welcome-desc">carels版权所有 V9.0 | 今天是 {{ currentDate }}</p>
      </div>
      <div class="welcome-stats">
        <div class="stat-item">
          <div class="stat-number">{{ stats.onlineUsers }}</div>
          <div class="stat-text">在线用户</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.todayOrders }}</div>
          <div class="stat-text">今日工单</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ stats.completionRate }}%</div>
          <div class="stat-text">完成率</div>
        </div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <el-row :gutter="20" class="quick-access">
      <el-col :span="4" v-for="(item, index) in quickLinks" :key="index">
        <div class="quick-card" @click="$router.push(item.path)">
          <div class="quick-icon" :style="{ background: item.color }">
            <i :class="item.icon"></i>
          </div>
          <div class="quick-title">{{ item.title }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据概览 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :span="6" v-for="(item, index) in overviewData" :key="index">
        <el-card class="overview-card" shadow="hover">
          <div class="overview-icon" :style="{ background: item.bgColor }">
            <i :class="item.icon" :style="{ color: item.iconColor }"></i>
          </div>
          <div class="overview-info">
            <div class="overview-value">{{ item.value }}</div>
            <div class="overview-label">{{ item.label }}</div>
            <div class="overview-trend" :class="item.trend > 0 ? 'up' : 'down'">
              <i :class="item.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(item.trend) }}%
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-data"></i> 生产趋势</span>
            <el-radio-group v-model="chartPeriod" size="small">
              <el-radio-button label="week">本周</el-radio-button>
              <el-radio-button label="month">本月</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-placeholder">
            <div class="mock-chart">
              <div v-for="(bar, idx) in mockChartData" :key="idx" 
                   class="chart-bar" 
                   :style="{ height: bar.height + '%', background: bar.color }">
                <span class="bar-label">{{ bar.label }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-check"></i> 质量统计</span>
            <el-tag type="success">合格率 98.5%</el-tag>
          </div>
          <div class="quality-stats">
            <div class="quality-item" v-for="(item, idx) in qualityData" :key="idx">
              <div class="quality-label">{{ item.label }}</div>
              <el-progress 
                :percentage="item.value" 
                :color="item.color"
                :stroke-width="16"
                :show-text="true"
              />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项和通知 -->
    <el-row :gutter="20" class="bottom-section">
      <el-col :span="12">
        <el-card class="todo-card" shadow="never">
          <div slot="header">
            <span><i class="el-icon-bell"></i> 待办事项</span>
            <el-badge :value="todoList.length" class="item" type="primary" />
          </div>
          <el-timeline>
            <el-timeline-item 
              v-for="(item, index) in todoList" 
              :key="index"
              :type="item.type"
              :timestamp="item.time"
            >
              {{ item.content }}
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="notice-card" shadow="never">
          <div slot="header">
            <span><i class="el-icon-message"></i> 系统公告</span>
          </div>
          <el-table :data="noticeList" style="width: 100%" :show-header="false">
            <el-table-column width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.type" size="small">{{ scope.row.tag }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="title" show-overflow-tooltip />
            <el-table-column width="100" prop="date" align="right" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
/**
 * 首页仪表盘 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'Dashboard',
  data() {
    return {
      currentDate: '',
      stats: {
        onlineUsers: 12,
        todayOrders: 156,
        completionRate: 92
      },
      quickLinks: [
        { title: '物料管理', icon: 'el-icon-goods', path: '/md/item', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { title: '生产工单', icon: 'el-icon-document', path: '/pro/workorder', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { title: '库存查询', icon: 'el-icon-s-data', path: '/wm/stock', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { title: '来料检验', icon: 'el-icon-document-checked', path: '/qc/iqc', color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' },
        { title: '设备台账', icon: 'el-icon-cpu', path: '/dv/machinery', color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)' },
        { title: '生产报表', icon: 'el-icon-pie-chart', path: '/report/chart', color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)' }
      ],
      overviewData: [
        { label: '生产工单', value: '1,234', icon: 'el-icon-s-order', bgColor: '#E3F2FD', iconColor: '#1976D2', trend: 12 },
        { label: '完工数量', value: '8,567', icon: 'el-icon-finished', bgColor: '#E8F5E9', iconColor: '#388E3C', trend: 8 },
        { label: '合格率', value: '98.5%', icon: 'el-icon-s-check', bgColor: '#FFF3E0', iconColor: '#F57C00', trend: 2 },
        { label: '设备运行', value: '45/50', icon: 'el-icon-cpu', bgColor: '#F3E5F5', iconColor: '#7B1FA2', trend: -3 }
      ],
      chartPeriod: 'week',
      mockChartData: [
        { label: '一', height: 60, color: '#409EFF' },
        { label: '二', height: 80, color: '#409EFF' },
        { label: '三', height: 45, color: '#409EFF' },
        { label: '四', height: 90, color: '#409EFF' },
        { label: '五', height: 70, color: '#409EFF' },
        { label: '六', height: 55, color: '#409EFF' },
        { label: '日', height: 40, color: '#409EFF' }
      ],
      qualityData: [
        { label: '来料检验', value: 98, color: '#67C23A' },
        { label: '过程检验', value: 95, color: '#409EFF' },
        { label: '出货检验', value: 99, color: '#E6A23C' },
        { label: '退货检验', value: 92, color: '#F56C6C' }
      ],
      todoList: [
        { content: '审核生产工单 WO20240315001', time: '10:30', type: 'primary' },
        { content: '处理设备异常报警 - 注塑机01', time: '09:15', type: 'warning' },
        { content: '来料检验单审批', time: '昨天', type: 'success' },
        { content: '完成月度生产报表', time: '昨天', type: 'info' }
      ],
      noticeList: [
        { tag: '重要', title: '系统将于今晚22:00进行维护升级', type: 'danger', date: '03-15' },
        { tag: '通知', title: '新增质量检验标准，请及时查看', type: 'warning', date: '03-14' },
        { tag: '公告', title: '清明节放假安排通知', type: 'info', date: '03-13' },
        { tag: '提示', title: '请定期修改登录密码', type: 'success', date: '03-12' }
      ]
    }
  },
  mounted() {
    this.updateDate()
    setInterval(this.updateDate, 60000)
  },
  methods: {
    updateDate() {
      const now = new Date()
      const options = { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric', 
        weekday: 'long',
        hour: '2-digit',
        minute: '2-digit'
      }
      this.currentDate = now.toLocaleDateString('zh-CN', options)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
  background: linear-gradient(180deg, #f5f7fa 0%, #ffffff 100%);
}

// 欢迎区域
.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 25px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
  
  .welcome-content {
    .welcome-title {
      font-size: 28px;
      font-weight: 600;
      margin-bottom: 10px;
      
      i {
        margin-right: 12px;
      }
    }
    
    .welcome-subtitle {
      font-size: 14px;
      opacity: 0.9;
      margin-bottom: 8px;
    }
    
    .welcome-desc {
      font-size: 13px;
      opacity: 0.8;
    }
  }
  
  .welcome-stats {
    display: flex;
    gap: 40px;
    
    .stat-item {
      text-align: center;
      
      .stat-number {
        font-size: 36px;
        font-weight: 700;
        margin-bottom: 5px;
      }
      
      .stat-text {
        font-size: 14px;
        opacity: 0.9;
      }
    }
  }
}

// 快捷入口
.quick-access {
  margin-bottom: 25px;
  
  .quick-card {
    background: #fff;
    border-radius: 12px;
    padding: 20px;
    text-align: center;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 2px 12px rgba(0,0,0,0.06);
    
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 24px rgba(0,0,0,0.12);
    }
    
    .quick-icon {
      width: 56px;
      height: 56px;
      border-radius: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 12px;
      
      i {
        font-size: 26px;
        color: #fff;
      }
    }
    
    .quick-title {
      font-size: 14px;
      color: #303133;
      font-weight: 500;
    }
  }
}

// 数据概览
.data-overview {
  margin-bottom: 25px;
  
  .overview-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 12px;
    
    .overview-icon {
      width: 60px;
      height: 60px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;
      
      i {
        font-size: 28px;
      }
    }
    
    .overview-info {
      flex: 1;
      
      .overview-value {
        font-size: 26px;
        font-weight: 700;
        color: #303133;
        line-height: 1;
        margin-bottom: 6px;
      }
      
      .overview-label {
        font-size: 13px;
        color: #909399;
        margin-bottom: 8px;
      }
      
      .overview-trend {
        font-size: 12px;
        font-weight: 500;
        
        &.up {
          color: #67C23A;
        }
        
        &.down {
          color: #F56C6C;
        }
      }
    }
  }
}

// 图表区域
.chart-section {
  margin-bottom: 25px;
  
  .chart-card {
    border-radius: 12px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
    
    .chart-placeholder {
      height: 250px;
      display: flex;
      align-items: flex-end;
      justify-content: center;
      padding: 20px;
      
      .mock-chart {
        display: flex;
        align-items: flex-end;
        gap: 20px;
        height: 100%;
        width: 100%;
        justify-content: center;
        
        .chart-bar {
          width: 60px;
          border-radius: 8px 8px 0 0;
          position: relative;
          transition: all 0.3s;
          
          &:hover {
            opacity: 0.8;
          }
          
          .bar-label {
            position: absolute;
            bottom: -25px;
            left: 50%;
            transform: translateX(-50%);
            font-size: 12px;
            color: #909399;
          }
        }
      }
    }
    
    .quality-stats {
      padding: 10px;
      
      .quality-item {
        margin-bottom: 20px;
        
        .quality-label {
          font-size: 13px;
          color: #606266;
          margin-bottom: 8px;
        }
      }
    }
  }
}

// 底部区域
.bottom-section {
  .todo-card,
  .notice-card {
    border-radius: 12px;
    min-height: 320px;
    
    .el-card__header {
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
}

// 响应式
@media (max-width: 1200px) {
  .welcome-section {
    flex-direction: column;
    text-align: center;
    
    .welcome-stats {
      margin-top: 20px;
    }
  }
  
  .quick-access .el-col {
    width: 50%;
    margin-bottom: 15px;
  }
}
</style>
