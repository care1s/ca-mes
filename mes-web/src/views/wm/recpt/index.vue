<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-download"></i>
        <span class="title">入库管理</span>
        <span class="subtitle">Receipt Management</span>
      </div>
      <div class="action-section">
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="入库单号">
          <el-input v-model="queryParams.recptNo" placeholder="请输入入库单号" clearable />
        </el-form-item>
        <el-form-item label="入库类型">
          <el-select v-model="queryParams.recptType" placeholder="请选择入库类型" clearable>
            <el-option label="采购入库" value="PURCHASE" />
            <el-option label="生产入库" value="PRODUCTION" />
            <el-option label="退货入库" value="RETURN" />
            <el-option label="其他入库" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计信息 - 单行展示 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待处理</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已确认</span>
        <span class="stat-value green">{{ stats.confirmed }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never" v-loading="loading">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-download"></i>
          入库单列表
        </span>
        <el-pagination
          class="pagination"
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      
      <el-table
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="recptNo" label="入库单号" width="150" show-overflow-tooltip />
        <el-table-column prop="recptType" label="入库类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.recptType === 'PURCHASE'" type="primary">采购入库</el-tag>
            <el-tag v-else-if="scope.row.recptType === 'PRODUCTION'" type="success">生产入库</el-tag>
            <el-tag v-else-if="scope.row.recptType === 'RETURN'" type="warning">退货入库</el-tag>
            <el-tag v-else type="info">{{ scope.row.recptType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="vendorName" label="供应商" width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.vendorName">{{ scope.row.vendorName }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="sourceNo" label="来源单号" width="150" show-overflow-tooltip />
        <el-table-column prop="warehouseName" label="仓库" min-width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'PENDING'" type="warning">待处理</el-tag>
            <el-tag v-else-if="scope.row.status === 'CONFIRMED'" type="primary">已确认</el-tag>
            <el-tag v-else-if="scope.row.status === 'COMPLETED'" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 'CANCELLED'" type="danger">已取消</el-tag>
            <el-tag v-else type="info">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="recptDate" label="入库日期" width="160" align="center" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              icon="el-icon-check"
              style="color: #67c23a"
              @click="handleConfirm(scope.row)"
            >确认</el-button>
            <el-button
              type="text"
              icon="el-icon-delete"
              style="color: #f56c6c"
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 查看详情对话框 -->
    <el-dialog title="入库单详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="入库单号">{{ viewData.recptNo }}</el-descriptions-item>
        <el-descriptions-item label="入库类型">
          <el-tag v-if="viewData.recptType === 'PURCHASE'" type="primary">采购入库</el-tag>
          <el-tag v-else-if="viewData.recptType === 'PRODUCTION'" type="success">生产入库</el-tag>
          <el-tag v-else-if="viewData.recptType === 'RETURN'" type="warning">退货入库</el-tag>
          <el-tag v-else type="info">{{ viewData.recptType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="供应商">{{ viewData.vendorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="来源单号">{{ viewData.sourceNo }}</el-descriptions-item>
        <el-descriptions-item label="入库仓库">{{ viewData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewData.status === 'PENDING'" type="warning">待处理</el-tag>
          <el-tag v-else-if="viewData.status === 'CONFIRMED'" type="primary">已确认</el-tag>
          <el-tag v-else-if="viewData.status === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else-if="viewData.status === 'CANCELLED'" type="danger">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="入库日期">{{ viewData.recptDate }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 20px 0 10px 0;">入库明细</h4>
      <el-table :data="viewData.items" border size="small">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="itemCode" label="物料编码" width="120" />
        <el-table-column prop="itemName" label="物料名称" width="150" />
        <el-table-column prop="batchCode" label="批次" width="100" />
        <el-table-column prop="quantity" label="数量" width="100" align="right" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWmRecpt, getWmRecpt, delWmRecpt, confirmWmRecpt, listWmWarehouse } from '@/api/md'
import XLSX from 'xlsx'

/**
 * 入库管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'WmRecpt',
  data() {
    return {
      loading: false,
      total: 0,
      stats: {
        total: 0,
        pending: 0,
        confirmed: 0,
        completed: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        recptNo: '',
        recptType: '',
        warehouseName: '',
        status: ''
      },
      tableData: [],
      warehouseOptions: [],
      viewDialogVisible: false,
      viewData: {
        recptNo: '',
        recptType: '',
        vendorName: '',
        sourceNo: '',
        warehouseName: '',
        status: '',
        recptDate: '',
        remark: '',
        items: []
      }
    }
  },
  mounted() {
    this.fetchData()
    this.fetchWarehouseOptions()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listWmRecpt(this.queryParams)
        if (res.code === 200) {
          this.tableData = res.rows || []
          this.total = res.total || 0
          this.calculateStats()
        } else {
          this.$message.error(res.msg || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    async fetchWarehouseOptions() {
      try {
        const res = await listWmWarehouse({ pageNum: 1, pageSize: 1000 })
        if (res.code === 200) {
          this.warehouseOptions = res.rows || []
        }
      } catch (error) {
        console.error('获取仓库列表失败:', error)
      }
    },
    
    // 导出Excel
    handleExport() {
      if (this.tableData.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      
      const headers = ['入库单号', '入库类型', '来源单号', '仓库', '入库日期', '状态', '备注', '创建时间']
      const data = this.tableData.map(row => [
        row.recptNo,
        this.getRecptTypeText(row.recptType),
        row.sourceNo || '-',
        row.warehouseName,
        row.recptDate,
        this.getStatusText(row.status),
        row.remark || '-',
        row.createTime
      ])
      
      const ws = XLSX.utils.aoa_to_sheet([headers, ...data])
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, ws, '入库单')
      
      const now = new Date()
      const filename = `入库单_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}.xlsx`
      
      XLSX.writeFile(wb, filename)
      this.$message.success('导出成功')
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.pending = this.tableData.filter(item => item.status === 'PENDING').length
      this.stats.confirmed = this.tableData.filter(item => item.status === 'CONFIRMED').length
      this.stats.completed = this.tableData.filter(item => item.status === 'COMPLETED').length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        recptNo: '',
        recptType: '',
        warehouseName: '',
        status: ''
      }
      this.fetchData()
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },
    async handleConfirm(row) {
      try {
        await this.$confirm('确认该入库单吗？', '提示', { type: 'warning' })
        const res = await confirmWmRecpt(row.id)
        if (res.code === 200) {
          this.$message.success('入库确认成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '确认失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('确认失败: ' + error.message)
        }
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除入库单 "${row.recptNo}" 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await delWmRecpt(row.id)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },

    // 辅助方法
    getRecptTypeText(type) {
      const types = {
        'PURCHASE': '采购入库',
        'PRODUCTION': '生产入库',
        'RETURN': '退货入库',
        'OTHER': '其他入库'
      }
      return types[type] || type
    },
    getStatusText(status) {
      const statuses = {
        'PENDING': '待处理',
        'CONFIRMED': '已确认',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statuses[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
}

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 2px solid #EBEEF5;

  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 28px;
      color: #409EFF;
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

// 搜索栏
.search-card {
  margin-bottom: 20px;
  
  .search-form {
    .el-form-item {
      margin-bottom: 0;
      margin-right: 20px;
    }
  }
}

// 统计信息 - 单行展示
.stats-bar {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  background: #fff;
  padding: 12px 20px;
  margin-bottom: 15px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  
  .stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .stat-label {
      font-size: 13px;
      color: #606266;
    }
    
    .stat-value {
      font-size: 16px;
      font-weight: 600;
      
      &.blue { color: #409EFF; }
      &.green { color: #67c23a; }
      &.orange { color: #e6a23c; }
      &.red { color: #f56c6c; }
    }
  }
  
  .stat-divider {
    width: 1px;
    height: 20px;
    background: #ebeef5;
    margin: 0 20px;
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
  }
  
  .el-table {
    margin-top: 15px;
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
}
</style>
