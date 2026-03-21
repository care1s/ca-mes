<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header report">
      <div class="title-section">
        <i class="el-icon-data-board"></i>
        <span class="title">工单进度看板</span>
        <span class="subtitle">Workorder Dashboard</span>
      </div>
      <div class="action-section">
        <el-button icon="el-icon-refresh" @click="fetchData" :loading="loading">刷新</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </div>
    </div>

    <!-- 统计信息 - 单行展示 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">待下达</span>
        <span class="stat-value orange">{{ summary.pending || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已下达</span>
        <span class="stat-value blue">{{ summary.released || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">生产中</span>
        <span class="stat-value blue">{{ summary.producing || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ summary.completed || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">总工单</span>
        <span class="stat-value blue">{{ summary.total || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">延期预警</span>
        <span class="stat-value red">{{ summary.delayed || 0 }}</span>
      </div>
    </div>

    <!-- 状态筛选标签 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-tags">
        <span class="filter-label">状态筛选：</span>
        <el-radio-group v-model="filterStatus" size="small" @change="handleFilterChange">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="PENDING">待下达</el-radio-button>
          <el-radio-button label="RELEASED">已下达</el-radio-button>
          <el-radio-button label="PRODUCING">生产中</el-radio-button>
          <el-radio-button label="COMPLETED">已完成</el-radio-button>
          <el-radio-button label="DELAYED">延期预警</el-radio-button>
        </el-radio-group>
      </div>
    </el-card>

    <!-- 工单列表 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-s-order"></i>
          工单进度列表
        </span>
        <span class="header-subtitle">共 {{ filteredList.length }} 条记录</span>
      </div>

      <div v-loading="loading" class="workorder-list">
        <!-- 空数据提示 -->
        <el-empty v-if="filteredList.length === 0" description="暂无工单数据" />

        <!-- 工单卡片列表 -->
        <div v-else class="workorder-cards">
          <div
            v-for="(item, index) in filteredList"
            :key="index"
            class="workorder-item"
            :class="getStatusClass(item.status)"
          >
            <!-- 工单头部 -->
            <div class="item-header">
              <div class="header-left">
                <span class="workorder-code">{{ item.workorderCode }}</span>
                <el-tag :type="getStatusType(item.status)" size="small" effect="dark">
                  {{ getStatusText(item.status) }}
                </el-tag>
                <el-tag v-if="item.priority === 'HIGH'" type="danger" size="small" effect="plain">
                  紧急
                </el-tag>
              </div>
              <div class="header-right">
                <span v-if="item.delayDays > 0" class="delay-badge">
                  <i class="el-icon-warning"></i>
                  延期 {{ item.delayDays }} 天
                </span>
                <span class="workshop-name">
                  <i class="el-icon-office-building"></i>
                  {{ item.workshopName || '未分配车间' }}
                </span>
              </div>
            </div>

            <!-- 工单内容 -->
            <div class="item-body">
              <div class="product-info">
                <div class="product-name">{{ item.itemName }}</div>
                <div class="product-spec">{{ item.specification || '暂无规格' }}</div>
              </div>

              <!-- 进度条 -->
              <div class="progress-section">
                <div class="progress-header">
                  <span class="progress-text">生产进度</span>
                  <span class="progress-value" :class="getProgressClass(item.progress)">
                    {{ item.progress }}%
                  </span>
                </div>
                <el-progress
                  :percentage="parseFloat(item.progress) || 0"
                  :color="getProgressColor(item.progress, item.status)"
                  :stroke-width="12"
                  :show-text="false"
                />
                <div class="quantity-info">
                  <span>计划: {{ item.planQuantity || 0 }}</span>
                  <span>完工: {{ item.completedQuantity || 0 }}</span>
                  <span>合格: {{ item.qualifiedQuantity || 0 }}</span>
                  <span v-if="item.defectiveQuantity > 0" class="defective">
                    不良: {{ item.defectiveQuantity }}
                  </span>
                </div>
              </div>

              <!-- 时间信息 -->
              <div class="time-info">
                <div class="time-item">
                  <span class="time-label">计划开始:</span>
                  <span class="time-value">{{ formatDate(item.planStartTime) }}</span>
                </div>
                <div class="time-item">
                  <span class="time-label">计划完成:</span>
                  <span class="time-value" :class="{ 'delayed': item.delayDays > 0 }">
                    {{ formatDate(item.planEndTime) }}
                  </span>
                </div>
                <div v-if="item.actualStartTime" class="time-item">
                  <span class="time-label">实际开始:</span>
                  <span class="time-value">{{ formatDate(item.actualStartTime) }}</span>
                </div>
              </div>
            </div>

            <!-- 工单底部 -->
            <div class="item-footer">
              <div class="footer-left">
                <span class="team-name">
                  <i class="el-icon-user"></i>
                  {{ item.teamName || '未分配班组' }}
                </span>
              </div>
              <div class="footer-right">
                <el-button type="text" size="small" @click="handleView(item)">
                  <i class="el-icon-view"></i> 查看详情
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from '@/api/request'

/**
 * 工单进度看板 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'ReportWorkorderBoard',
  data() {
    return {
      loading: false,
      summary: {
        pending: 0,
        released: 0,
        producing: 0,
        completed: 0,
        closed: 0,
        total: 0,
        delayed: 0
      },
      workorderList: [],
      filterStatus: ''
    }
  },
  computed: {
    filteredList() {
      if (!this.filterStatus) {
        return this.workorderList
      }
      if (this.filterStatus === 'DELAYED') {
        return this.workorderList.filter(item => item.delayDays > 0)
      }
      return this.workorderList.filter(item => item.status === this.filterStatus)
    }
  },
  mounted() {
    this.fetchData()
    // 自动刷新，每30秒更新一次
    this.refreshTimer = setInterval(() => {
      this.fetchData()
    }, 30000)
  },
  beforeDestroy() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
    }
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await request({
          url: '/mes/pro/workorder/dashboard',
          method: 'get'
        })
        if (res.code === 200) {
          this.summary = res.data.summary || {}
          this.workorderList = res.data.list || []
        }
      } catch (error) {
        console.error('获取工单看板数据失败:', error)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    handleFilterChange() {
      // 筛选状态改变，不需要重新请求数据
    },
    handleExport() {
      if (this.filteredList.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }

      const headers = ['工单编码', '产品名称', '规格型号', '状态', '计划数量', '完工数量', '进度', '车间', '班组', '计划完成时间']
      const data = this.filteredList.map(item => [
        item.workorderCode,
        item.itemName,
        item.specification || '',
        this.getStatusText(item.status),
        item.planQuantity,
        item.completedQuantity,
        item.progress + '%',
        item.workshopName || '',
        item.teamName || '',
        this.formatDate(item.planEndTime)
      ])

      import('xlsx').then(XLSX => {
        const ws = XLSX.utils.aoa_to_sheet([headers, ...data])
        const wb = XLSX.utils.book_new()
        XLSX.utils.book_append_sheet(wb, ws, '工单看板')

        const now = new Date()
        const filename = `工单看板_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}.xlsx`

        XLSX.writeFile(wb, filename)
        this.$message.success('导出成功')
      })
    },
    handleView(row) {
      this.$alert(`
        <div style="line-height: 2;">
          <p><strong>工单编码：</strong>${row.workorderCode}</p>
          <p><strong>产品名称：</strong>${row.itemName}</p>
          <p><strong>规格型号：</strong>${row.specification || '暂无'}</p>
          <p><strong>工单状态：</strong>${this.getStatusText(row.status)}</p>
          <p><strong>计划数量：</strong>${row.planQuantity}</p>
          <p><strong>完工数量：</strong>${row.completedQuantity || 0}</p>
          <p><strong>合格数量：</strong>${row.qualifiedQuantity || 0}</p>
          <p><strong>不良数量：</strong>${row.defectiveQuantity || 0}</p>
          <p><strong>生产进度：</strong>${row.progress}%</p>
          <p><strong>生产车间：</strong>${row.workshopName || '未分配'}</p>
          <p><strong>生产班组：</strong>${row.teamName || '未分配'}</p>
          <p><strong>计划开始：</strong>${this.formatDate(row.planStartTime)}</p>
          <p><strong>计划完成：</strong>${this.formatDate(row.planEndTime)}</p>
        </div>
      `, '工单详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        width: '500px'
      })
    },
    getStatusType(status) {
      const map = {
        'PENDING': 'info',
        'RELEASED': 'warning',
        'PRODUCING': 'primary',
        'COMPLETED': 'success',
        'CLOSED': ''
      }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = {
        'PENDING': '待下达',
        'RELEASED': '已下达',
        'PRODUCING': '生产中',
        'COMPLETED': '已完成',
        'CLOSED': '已关闭'
      }
      return map[status] || status
    },
    getStatusClass(status) {
      return status ? status.toLowerCase() : ''
    },
    getProgressClass(progress) {
      const p = parseFloat(progress) || 0
      if (p >= 100) return 'success'
      if (p >= 60) return 'warning'
      return 'normal'
    },
    getProgressColor(progress, status) {
      const p = parseFloat(progress) || 0
      if (p >= 100) return '#67C23A'
      if (p >= 80) return '#409EFF'
      if (p >= 50) return '#E6A23C'
      return '#F56C6C'
    },
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
  background: #f5f7fa;
}

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 2px solid #EBEEF5;

  &.report {
    border-bottom-color: #909399;
  }

  .title-section {
    display: flex;
    align-items: center;

    i {
      font-size: 28px;
      color: #909399;
      margin-right: 12px;
    }

    .title {
      font-size: 22px;
      font-weight: 600;
      color: #303133;
      margin-right: 10px;
    }

    .subtitle {
      font-size: 13px;
      color: #909399;
      font-weight: normal;
    }
  }
}

// 统计卡片
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 16px rgba(0,0,0,0.1);
  }

  .stat-icon {
    width: 56px;
    height: 56px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 15px;

    i {
      font-size: 24px;
      color: #fff;
    }
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 26px;
      font-weight: 700;
      line-height: 1;
      margin-bottom: 8px;
    }

    .stat-label {
      font-size: 13px;
      opacity: 0.8;
    }
  }

  // 不同状态的颜色
  &.pending {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.released {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.producing {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.completed {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.total {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.delayed {
    background: linear-gradient(135deg, #ff0844 0%, #ffb199 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }
}

// 筛选标签
.filter-card {
  margin-bottom: 20px;

  .filter-tags {
    display: flex;
    align-items: center;

    .filter-label {
      font-size: 14px;
      color: #606266;
      margin-right: 15px;
    }
  }
}

// 表格卡片
.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }

    .header-subtitle {
      font-size: 13px;
      color: #909399;
    }
  }
}

// 工单列表
.workorder-list {
  padding: 10px 0;
}

.workorder-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
}

.workorder-item {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: all 0.3s;
  border-left: 4px solid #dcdfe6;

  &:hover {
    box-shadow: 0 8px 24px rgba(0,0,0,0.1);
    transform: translateY(-2px);
  }

  // 不同状态左边框颜色
  &.pending { border-left-color: #909399; }
  &.released { border-left-color: #e6a23c; }
  &.producing { border-left-color: #409eff; }
  &.completed { border-left-color: #67c23a; }
  &.closed { border-left-color: #dcdfe6; }

  .item-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      .workorder-code {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      gap: 10px;

      .delay-badge {
        background: #fef0f0;
        color: #f56c6c;
        padding: 4px 10px;
        border-radius: 4px;
        font-size: 12px;
        font-weight: 500;

        i {
          margin-right: 4px;
        }
      }

      .workshop-name {
        font-size: 13px;
        color: #909399;

        i {
          margin-right: 4px;
        }
      }
    }
  }

  .item-body {
    .product-info {
      margin-bottom: 15px;

      .product-name {
        font-size: 15px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 5px;
      }

      .product-spec {
        font-size: 13px;
        color: #909399;
      }
    }

    .progress-section {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 15px;
      margin-bottom: 15px;

      .progress-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;

        .progress-text {
          font-size: 13px;
          color: #606266;
        }

        .progress-value {
          font-size: 18px;
          font-weight: 600;

          &.success { color: #67c23a; }
          &.warning { color: #e6a23c; }
          &.normal { color: #409eff; }
        }
      }

      .quantity-info {
        display: flex;
        gap: 20px;
        margin-top: 10px;
        font-size: 12px;
        color: #606266;

        span {
          display: flex;
          align-items: center;
        }

        .defective {
          color: #f56c6c;
        }
      }
    }

    .time-info {
      display: flex;
      flex-wrap: wrap;
      gap: 15px;

      .time-item {
        font-size: 12px;

        .time-label {
          color: #909399;
          margin-right: 5px;
        }

        .time-value {
          color: #606266;

          &.delayed {
            color: #f56c6c;
            font-weight: 500;
          }
        }
      }
    }
  }

  .item-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;

    .footer-left {
      .team-name {
        font-size: 13px;
        color: #606266;

        i {
          margin-right: 5px;
          color: #409eff;
        }
      }
    }

    .footer-right {
      .el-button {
        color: #409eff;
      }
    }
  }
}

// 响应式调整
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;

    .action-section {
      margin-top: 10px;
    }
  }

  .stat-row {
    .el-col {
      margin-bottom: 15px;
    }
  }

  .workorder-cards {
    grid-template-columns: 1fr;
  }

  .workorder-item {
    .item-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      .header-right {
        width: 100%;
        justify-content: flex-start;
      }
    }
  }
}
</style>
