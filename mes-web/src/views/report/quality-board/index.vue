<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header qc">
      <div class="title-section">
        <i class="el-icon-data-analysis"></i>
        <span class="title">待检任务看板</span>
        <span class="subtitle">Pending Inspection Dashboard</span>
      </div>
      <div class="action-section">
        <el-button icon="el-icon-refresh" @click="fetchData" :loading="loading">刷新</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </div>
    </div>

    <!-- 统计信息 - 单行展示 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">来料待检</span>
        <span class="stat-value blue">{{ summary.iqc || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">过程待检</span>
        <span class="stat-value blue">{{ summary.ipqc || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">出货物检</span>
        <span class="stat-value blue">{{ summary.oqc || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">退货待检</span>
        <span class="stat-value orange">{{ summary.rqc || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">总待检</span>
        <span class="stat-value blue">{{ summary.total || 0 }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">紧急待检</span>
        <span class="stat-value red">{{ summary.urgent || 0 }}</span>
      </div>
    </div>

    <!-- 检验类型筛选 -->
    <el-card class="filter-card" shadow="never">
      <div class="filter-tags">
        <span class="filter-label">检验类型：</span>
        <el-radio-group v-model="filterType" size="small" @change="handleFilterChange">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="IQC">来料检验</el-radio-button>
          <el-radio-button label="IPQC">过程检验</el-radio-button>
          <el-radio-button label="OQC">出货检验</el-radio-button>
          <el-radio-button label="URGENT">紧急优先</el-radio-button>
        </el-radio-group>
      </div>
    </el-card>

    <!-- 待检任务列表 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-s-claim"></i>
          待检任务列表
        </span>
        <span class="header-subtitle">共 {{ filteredList.length }} 条待检记录</span>
      </div>

      <div v-loading="loading" class="inspect-list">
        <!-- 空数据提示 -->
        <el-empty v-if="filteredList.length === 0" description="暂无待检任务" />

        <!-- 待检任务卡片列表 -->
        <div v-else class="inspect-cards">
          <div
            v-for="(item, index) in filteredList"
            :key="index"
            class="inspect-item"
            :class="[item.inspectType.toLowerCase(), item.urgency.toLowerCase()]"
          >
            <!-- 任务头部 -->
            <div class="item-header">
              <div class="header-left">
                <el-tag :type="getInspectTypeType(item.inspectType)" size="small" effect="dark">
                  {{ item.inspectType }}
                </el-tag>
                <span class="inspect-code">{{ item.inspectCode }}</span>
                <el-tag
                  :type="getUrgencyType(item.urgency)"
                  size="small"
                  effect="plain"
                  class="urgency-tag"
                >
                  <i :class="getUrgencyIcon(item.urgency)"></i>
                  {{ getUrgencyText(item.urgency) }}
                </el-tag>
              </div>
              <div class="header-right">
                <span class="wait-time" :class="{ 'urgent': item.waitHours > 4 }">
                  <i class="el-icon-time"></i>
                  等待 {{ item.waitHours }} 小时
                </span>
              </div>
            </div>

            <!-- 任务内容 -->
            <div class="item-body">
              <div class="material-info">
                <div class="material-name">{{ item.itemName }}</div>
                <div class="material-code">编码: {{ item.itemCode }}</div>
              </div>

              <div class="inspect-details">
                <div class="detail-row">
                  <span class="detail-label">检验类型:</span>
                  <span class="detail-value">{{ item.inspectTypeName }}</span>
                </div>
                <div v-if="item.batchCode" class="detail-row">
                  <span class="detail-label">批次号:</span>
                  <span class="detail-value">{{ item.batchCode }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">送检数量:</span>
                  <span class="detail-value highlight">{{ item.inspectQuantity }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">{{ getSupplierLabel(item.inspectType) }}:</span>
                  <span class="detail-value">{{ item.supplierName || '未知' }}</span>
                </div>
              </div>
            </div>

            <!-- 任务底部 -->
            <div class="item-footer">
              <div class="footer-left">
                <span class="create-time">
                  <i class="el-icon-date"></i>
                  创建时间: {{ formatDateTime(item.createTime) }}
                </span>
              </div>
              <div class="footer-right">
                <el-button
                  type="primary"
                  size="small"
                  icon="el-icon-edit"
                  @click="handleInspect(item)"
                >
                  开始检验
                </el-button>
                <el-button type="text" size="small" @click="handleView(item)">
                  <i class="el-icon-view"></i> 查看
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
 * 待检任务看板 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'ReportQualityBoard',
  data() {
    return {
      loading: false,
      summary: {
        iqc: 0,
        ipqc: 0,
        oqc: 0,
        rqc: 0,
        total: 0,
        urgent: 0
      },
      inspectList: [],
      filterType: ''
    }
  },
  computed: {
    filteredList() {
      if (!this.filterType) {
        return this.inspectList
      }
      if (this.filterType === 'URGENT') {
        return this.inspectList.filter(item => item.urgency === 'HIGH')
      }
      return this.inspectList.filter(item => item.inspectType === this.filterType)
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
          url: '/mes/qc/pending/dashboard',
          method: 'get'
        })
        if (res.code === 200) {
          this.summary = res.data.summary || {}
          this.inspectList = res.data.list || []
        }
      } catch (error) {
        console.error('获取待检任务数据失败:', error)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    handleFilterChange() {
      // 筛选类型改变，不需要重新请求数据
    },
    handleExport() {
      if (this.filteredList.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }

      const headers = ['检验类型', '检验单号', '物料名称', '物料编码', '批次号', '送检数量', '供应商/工单', '紧急程度', '等待时长', '创建时间']
      const data = this.filteredList.map(item => [
        item.inspectType,
        item.inspectCode,
        item.itemName,
        item.itemCode,
        item.batchCode || '',
        item.inspectQuantity,
        item.supplierName || '',
        this.getUrgencyText(item.urgency),
        item.waitHours + '小时',
        this.formatDateTime(item.createTime)
      ])

      import('xlsx').then(XLSX => {
        const ws = XLSX.utils.aoa_to_sheet([headers, ...data])
        const wb = XLSX.utils.book_new()
        XLSX.utils.book_append_sheet(wb, ws, '待检任务')

        const now = new Date()
        const filename = `待检任务_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}.xlsx`

        XLSX.writeFile(wb, filename)
        this.$message.success('导出成功')
      })
    },
    handleInspect(item) {
      this.$message.info(`开始检验: ${item.inspectCode}`)
      // TODO: 跳转到检验页面
      const routeMap = {
        'IQC': '/qc/iqc',
        'IPQC': '/qc/ipqc',
        'OQC': '/qc/oqc'
      }
      const route = routeMap[item.inspectType]
      if (route) {
        this.$router.push(route)
      }
    },
    handleView(item) {
      this.$alert(`
        <div style="line-height: 2;">
          <p><strong>检验类型：</strong>${item.inspectTypeName}</p>
          <p><strong>检验单号：</strong>${item.inspectCode}</p>
          <p><strong>物料名称：</strong>${item.itemName}</p>
          <p><strong>物料编码：</strong>${item.itemCode}</p>
          <p><strong>批次号：</strong>${item.batchCode || '无'}</p>
          <p><strong>送检数量：</strong>${item.inspectQuantity}</p>
          <p><strong>${this.getSupplierLabel(item.inspectType)}：</strong>${item.supplierName || '未知'}</p>
          <p><strong>紧急程度：</strong>${this.getUrgencyText(item.urgency)}</p>
          <p><strong>等待时长：</strong>${item.waitHours} 小时</p>
          <p><strong>创建时间：</strong>${this.formatDateTime(item.createTime)}</p>
        </div>
      `, '检验任务详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        width: '500px'
      })
    },
    getInspectTypeType(type) {
      const map = {
        'IQC': 'warning',
        'IPQC': 'success',
        'OQC': 'primary',
        'RQC': 'danger'
      }
      return map[type] || 'info'
    },
    getUrgencyType(urgency) {
      const map = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return map[urgency] || 'info'
    },
    getUrgencyText(urgency) {
      const map = {
        'HIGH': '紧急',
        'MEDIUM': '一般',
        'LOW': '普通'
      }
      return map[urgency] || urgency
    },
    getUrgencyIcon(urgency) {
      const map = {
        'HIGH': 'el-icon-warning',
        'MEDIUM': 'el-icon-time',
        'LOW': 'el-icon-check'
      }
      return map[urgency] || ''
    },
    getSupplierLabel(type) {
      const map = {
        'IQC': '供应商',
        'IPQC': '工单号',
        'OQC': '客户',
        'RQC': '退货来源'
      }
      return map[type] || '来源'
    },
    formatDateTime(date) {
      if (!date) return '-'
      const d = new Date(date)
      return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
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

  &.qc {
    border-bottom-color: #67c23a;

    .title-section i {
      color: #67c23a;
    }
  }

  .title-section {
    display: flex;
    align-items: center;

    i {
      font-size: 28px;
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

  // 不同检验类型的颜色
  &.iqc {
    background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.ipqc {
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.oqc {
    background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.rqc {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.total {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }

  &.urgent {
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
        color: #67c23a;
      }
    }

    .header-subtitle {
      font-size: 13px;
      color: #909399;
    }
  }
}

// 检验任务列表
.inspect-list {
  padding: 10px 0;
}

.inspect-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
  gap: 20px;
}

.inspect-item {
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

  // 不同检验类型左边框颜色
  &.iqc { border-left-color: #f5576c; }
  &.ipqc { border-left-color: #00f2fe; }
  &.oqc { border-left-color: #43e97b; }
  &.rqc { border-left-color: #fee140; }

  // 紧急程度样式
  &.high {
    background: linear-gradient(to right, #fff5f5, #fff);
  }

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
      flex-wrap: wrap;

      .inspect-code {
        font-size: 15px;
        font-weight: 600;
        color: #303133;
      }

      .urgency-tag {
        i {
          margin-right: 4px;
        }
      }
    }

    .header-right {
      .wait-time {
        font-size: 13px;
        color: #909399;
        background: #f5f7fa;
        padding: 4px 10px;
        border-radius: 4px;

        i {
          margin-right: 4px;
        }

        &.urgent {
          background: #fef0f0;
          color: #f56c6c;
        }
      }
    }
  }

  .item-body {
    .material-info {
      margin-bottom: 15px;

      .material-name {
        font-size: 15px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 5px;
      }

      .material-code {
        font-size: 13px;
        color: #909399;
      }
    }

    .inspect-details {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 15px;

      .detail-row {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .detail-label {
          font-size: 13px;
          color: #909399;
        }

        .detail-value {
          font-size: 13px;
          color: #606266;

          &.highlight {
            font-weight: 600;
            color: #409eff;
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
      .create-time {
        font-size: 12px;
        color: #909399;

        i {
          margin-right: 5px;
        }
      }
    }

    .footer-right {
      display: flex;
      align-items: center;
      gap: 10px;
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

  .inspect-cards {
    grid-template-columns: 1fr;
  }

  .inspect-item {
    .item-header {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      .header-right {
        width: 100%;
      }
    }

    .item-footer {
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      .footer-right {
        width: 100%;
        justify-content: flex-end;
      }
    }
  }
}
</style>
