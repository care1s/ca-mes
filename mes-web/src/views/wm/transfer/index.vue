<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-promotion"></i>
        <span class="title">库存调拨</span>
        <span class="subtitle">Stock Transfer</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增调拨</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待出库</span>
        <span class="stat-value orange">{{ stats.pendingOut }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已出库</span>
        <span class="stat-value warning">{{ stats.out }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="调拨单号">
          <el-input v-model="queryParams.transferNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="调出仓库">
          <el-input v-model="queryParams.fromWarehouseName" placeholder="请输入调出仓库" clearable />
        </el-form-item>
        <el-form-item label="调入仓库">
          <el-input v-model="queryParams.toWarehouseName" placeholder="请输入调入仓库" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="待出库" :value="1" />
            <el-option label="已出库" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table  :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="transferNo" label="调拨单号" width="140" />
        <el-table-column prop="transferType" label="类型" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.transferType === 0 ? 'primary' : 'danger'" size="small">
              {{ scope.row.transferType === 0 ? '普通' : '紧急' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fromWarehouseName" label="调出仓库" min-width="120" />
        <el-table-column prop="toWarehouseName" label="调入仓库" min-width="120" />
        <el-table-column prop="transferDate" label="调拨日期" width="100" />
        <el-table-column prop="totalQty" label="总数量" width="90" align="right" />
        <el-table-column prop="totalCount" label="总件数" width="80" align="right" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" @click="handleSubmit(scope.row)">提交</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleOut(scope.row)">出库</el-button>
            <el-button v-if="scope.row.status === 2" type="text" size="small" @click="handleIn(scope.row)">入库</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调拨单号" prop="transferNo">
              <el-input v-model="form.transferNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调拨类型" prop="transferType">
              <el-select v-model="form.transferType" placeholder="请选择类型" style="width: 100%">
                <el-option label="普通调拨" :value="0" />
                <el-option label="紧急调拨" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调出仓库" prop="fromWarehouseName">
              <el-input v-model="form.fromWarehouseName" placeholder="请选择调出仓库" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调入仓库" prop="toWarehouseName">
              <el-input v-model="form.toWarehouseName" placeholder="请选择调入仓库" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="调拨日期" prop="transferDate">
              <el-date-picker v-model="form.transferDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicantName">
              <el-input v-model="form.applicantName" placeholder="请输入申请人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'WmTransfer',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        transferNo: '',
        fromWarehouseName: '',
        toWarehouseName: '',
        status: null
      },
      stats: {
        total: 0,
        pendingOut: 0,
        out: 0,
        completed: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        transferNo: '',
        fromWarehouseId: null,
        fromWarehouseName: '',
        toWarehouseId: null,
        toWarehouseName: '',
        transferDate: null,
        transferType: 0,
        totalQty: 0,
        totalCount: 0,
        applicantId: null,
        applicantName: '',
        remark: ''
      },
      rules: {
        fromWarehouseName: [{ required: true, message: '请选择调出仓库', trigger: 'blur' }],
        toWarehouseName: [{ required: true, message: '请选择调入仓库', trigger: 'blur' }],
        transferDate: [{ required: true, message: '请选择调拨日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            transferId: 1,
            transferNo: 'TR20240317001',
            transferType: 0,
            fromWarehouseName: '原材料仓库',
            toWarehouseName: '生产线仓库',
            transferDate: '2024-03-17',
            totalQty: 500,
            totalCount: 10,
            applicantName: '张三',
            status: 3,
            createTime: '2024-03-17 10:00:00'
          },
          {
            transferId: 2,
            transferNo: 'TR20240317002',
            transferType: 1,
            fromWarehouseName: '成品仓库',
            toWarehouseName: '发货仓库',
            transferDate: '2024-03-17',
            totalQty: 200,
            totalCount: 5,
            applicantName: '李四',
            status: 1,
            createTime: '2024-03-17 11:00:00'
          }
        ]
        this.total = 2
        this.calculateStats()
        this.loading = false
      }, 500)
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.pendingOut = this.tableData.filter(item => item.status === 1).length
      this.stats.out = this.tableData.filter(item => item.status === 2).length
      this.stats.completed = this.tableData.filter(item => item.status === 3).length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        transferNo: '',
        fromWarehouseName: '',
        toWarehouseName: '',
        status: null
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增调拨单'
      this.form = {
        transferNo: '',
        fromWarehouseId: null,
        fromWarehouseName: '',
        toWarehouseId: null,
        toWarehouseName: '',
        transferDate: new Date(),
        transferType: 0,
        totalQty: 0,
        totalCount: 0,
        applicantId: null,
        applicantName: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleEdit(row) {
      this.dialogTitle = '编辑调拨单'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该调拨单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleSubmit(row) {
      this.$confirm('确认提交该调拨单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('提交成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleOut(row) {
      this.$confirm('确认出库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('出库确认成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleIn(row) {
      this.$confirm('确认入库吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        this.$message.success('入库确认成功')
        this.fetchData()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success(this.form.transferId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'warning', 3: 'success', 4: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '待出库', 2: '已出库', 3: '已完成', 4: '已取消' }
      return texts[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 1px solid #ebeef5;

  .title-section {
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      font-size: 24px;
      color: #409eff;
    }

    .title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .subtitle {
      font-size: 14px;
      color: #909399;
      font-weight: normal;
    }
  }
}

.stats-bar {
  display: flex;
  align-items: center;
  gap: 0;
  margin-bottom: 20px;
  padding: 15px 20px;
  background: #f5f7fa;
  border-radius: 4px;

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0 25px;

    .stat-label {
      font-size: 13px;
      color: #606266;
      margin-bottom: 5px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: 600;

      &.blue { color: #409eff; }
      &.green { color: #67c23a; }
      &.orange { color: #e6a23c; }
      &.warning { color: #ffba00; }
    }
  }

  .stat-divider {
    width: 1px;
    height: 40px;
    background: #dcdfe6;
  }
}

.search-card {
  margin-bottom: 20px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}

.table-card {
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 确保按钮可点击，移除任何可能的遮挡 */
.el-button {
  pointer-events: auto !important;
  z-index: 1;
}

/* 移除表格加载遮罩 */
.el-table {
  pointer-events: auto;
}

.el-table .el-table__body-wrapper {
  pointer-events: auto;
}

/* 确保操作列按钮可点击 */
.el-table .cell {
  pointer-events: auto;
}

/* 移除任何可能的遮罩层 */
::v-deep .el-loading-mask {
  display: none !important;
}

::v-deep .el-loading-spinner {
  display: none !important;
}

/* 确保卡片内容可点击 */
.el-card {
  pointer-events: auto;
}

.el-card__body {
  pointer-events: auto;
}
</style>
