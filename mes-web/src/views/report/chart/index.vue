<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header report">
      <div class="title-section">
        <i class="el-icon-pie-chart"></i>
        <span class="title">生产报表</span>
        <span class="subtitle">Production Report</span>
      </div>
      <div class="action-section">
        <el-button icon="el-icon-refresh" @click="fetchData" :loading="loading">刷新</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6" :xs="12">
        <el-card class="stat-card total" shadow="hover">
          <div class="stat-icon">
            <i class="el-icon-s-order"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ workorderStats.total || 0 }}</div>
            <div class="stat-label">本月工单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" :xs="12">
        <el-card class="stat-card producing" shadow="hover">
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ workorderStats.producing || 0 }}</div>
            <div class="stat-label">生产中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" :xs="12">
        <el-card class="stat-card completed" shadow="hover">
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ (workorderStats.completed || 0) + (workorderStats.closed || 0) }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6" :xs="12">
        <el-card class="stat-card rate" shadow="hover">
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ completionRate }}%</div>
            <div class="stat-label">完成率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-row">
      <!-- 工单状态分布饼图 -->
      <el-col :span="12" :xs="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-pie-chart"></i>
              工单状态分布
            </span>
          </div>
          <div ref="statusChart" class="chart-container"></div>
        </el-card>
      </el-col>

      <!-- 最近7天生产趋势 -->
      <el-col :span="12" :xs="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-s-marketing"></i>
              最近7天生产趋势
            </span>
          </div>
          <div ref="trendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 车间产量排行 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span class="header-title">
              <i class="el-icon-s-flag"></i>
              车间产量排行（最近30天）
            </span>
          </div>
          <div ref="workshopChart" class="chart-container-bar"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from '@/api/request'

/**
 * 生产报表 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'ReportChart',
  data() {
    return {
      loading: false,
      workorderStats: {},
      dailyStats: [],
      workshopRanking: [],
      charts: {}
    }
  },
  computed: {
    completionRate() {
      const total = this.workorderStats.total || 0
      const completed = (this.workorderStats.completed || 0) + (this.workorderStats.closed || 0)
      return total > 0 ? Math.round(completed * 100 / total) : 0
    }
  },
  mounted() {
    this.fetchData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    Object.values(this.charts).forEach(chart => chart && chart.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await request({
          url: '/mes/report/chart',
          method: 'get'
        })
        if (res.code === 200) {
          this.workorderStats = res.data.statusDistribution || {}
          this.dailyStats = res.data.dailyStats || []
          this.workshopRanking = res.data.workshopRanking || []
          this.$nextTick(() => {
            this.initCharts()
          })
        }
      } catch (error) {
        console.error('获取报表数据失败:', error)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initStatusChart()
      this.initTrendChart()
      this.initWorkshopChart()
    },
    initStatusChart() {
      const chart = this.$echarts.init(this.$refs.statusChart)
      this.charts.status = chart

      const data = [
        { value: this.workorderStats.pending || 0, name: '待下达' },
        { value: this.workorderStats.released || 0, name: '已下达' },
        { value: this.workorderStats.producing || 0, name: '生产中' },
        { value: this.workorderStats.completed || 0, name: '已完成' },
        { value: this.workorderStats.closed || 0, name: '已关闭' }
      ].filter(item => item.value > 0)

      chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 20,
          top: 'center'
        },
        color: ['#909399', '#e6a23c', '#409eff', '#67c23a', '#dcdfe6'],
        series: [{
          name: '工单状态',
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: 20,
              fontWeight: 'bold'
            }
          },
          labelLine: { show: false },
          data: data
        }]
      })
    },
    initTrendChart() {
      const chart = this.$echarts.init(this.$refs.trendChart)
      this.charts.trend = chart

      const dates = this.dailyStats.map(item => item.date ? item.date.substring(5) : '')
      const planQty = this.dailyStats.map(item => item.planQuantity || 0)
      const completedQty = this.dailyStats.map(item => item.completedQuantity || 0)

      chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'cross' }
        },
        legend: {
          data: ['计划数量', '完工数量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates,
          axisLabel: { rotate: 30 }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '计划数量',
            type: 'line',
            data: planQty,
            smooth: true,
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.1)'
            },
            itemStyle: { color: '#409eff' }
          },
          {
            name: '完工数量',
            type: 'line',
            data: completedQty,
            smooth: true,
            areaStyle: {
              color: 'rgba(103, 194, 58, 0.1)'
            },
            itemStyle: { color: '#67c23a' }
          }
        ]
      })
    },
    initWorkshopChart() {
      const chart = this.$echarts.init(this.$refs.workshopChart)
      this.charts.workshop = chart

      const names = this.workshopRanking.map(item => item.workshopName || '未知车间')
      const outputs = this.workshopRanking.map(item => item.totalOutput || 0)

      chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: names,
          axisLabel: { rotate: 30 }
        },
        yAxis: {
          type: 'value',
          name: '产量'
        },
        series: [{
          name: '产量',
          type: 'bar',
          data: outputs,
          itemStyle: {
            color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          },
          emphasis: {
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#2378f7' },
                { offset: 0.7, color: '#2378f7' },
                { offset: 1, color: '#83bff6' }
              ])
            }
          }
        }]
      })
    },
    handleResize() {
      Object.values(this.charts).forEach(chart => chart && chart.resize())
    },
    handleExport() {
      const data = [
        ['统计项', '数值'],
        ['工单总数', this.workorderStats.total || 0],
        ['待下达', this.workorderStats.pending || 0],
        ['已下达', this.workorderStats.released || 0],
        ['生产中', this.workorderStats.producing || 0],
        ['已完成', (this.workorderStats.completed || 0) + (this.workorderStats.closed || 0)],
        ['完成率', this.completionRate + '%']
      ]

      import('xlsx').then(XLSX => {
        const ws = XLSX.utils.aoa_to_sheet(data)
        const wb = XLSX.utils.book_new()
        XLSX.utils.book_append_sheet(wb, ws, '生产报表')

        const now = new Date()
        const filename = `生产报表_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}.xlsx`

        XLSX.writeFile(wb, filename)
        this.$message.success('导出成功')
      })
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

    .title-section i {
      color: #909399;
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

  // 不同统计项的颜色
  &.total {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

  &.rate {
    background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    color: #fff;
    .stat-icon { background: rgba(255,255,255,0.2); }
  }
}

// 图表区域
.chart-row {
  margin-bottom: 20px;
}

.chart-card {
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
  }

  .chart-container {
    height: 350px;
    margin-top: 10px;
  }

  .chart-container-bar {
    height: 300px;
    margin-top: 10px;
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

  .chart-row {
    .el-col {
      margin-bottom: 20px;
    }
  }

  .chart-card {
    .chart-container,
    .chart-container-bar {
      height: 250px;
    }
  }
}
</style>
