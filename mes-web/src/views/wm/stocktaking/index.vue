<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-check"></i>
        <span class="title">库存盘点</span>
        <span class="subtitle">Stocktaking</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增盘点</el-button>
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
        <span class="stat-label">盘点中</span>
        <span class="stat-value orange">{{ stats.inProgress }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">盘盈</span>
        <span class="stat-value red">{{ stats.profit }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">盘亏</span>
        <span class="stat-value gray">{{ stats.loss }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="盘点单号">
          <el-input v-model="queryParams.stocktakingNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="仓库">
          <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库" clearable />
        </el-form-item>
        <el-form-item label="盘点类型">
          <el-select v-model="queryParams.stocktakingType" placeholder="请选择类型" clearable>
            <el-option label="全盘" :value="0" />
            <el-option label="抽盘" :value="1" />
            <el-option label="动碰盘" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="盘点中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
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
        <el-table-column prop="stocktakingNo" label="盘点单号" width="140" />
        <el-table-column prop="warehouseName" label="仓库" min-width="120" />
        <el-table-column prop="stocktakingType" label="盘点类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.stocktakingType)" size="small">
              {{ getTypeText(scope.row.stocktakingType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="stocktakingDate" label="盘点日期" width="100" />
        <el-table-column prop="stocktakerName" label="盘点人" width="100" />
        <el-table-column prop="planCount" label="计划数" width="80" align="right" />
        <el-table-column prop="actualCount" label="实盘数" width="80" align="right" />
        <el-table-column prop="profitCount" label="盘盈" width="70" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.profitCount > 0" style="color: #67c23a">+{{ scope.row.profitCount }}</span>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column prop="lossCount" label="盘亏" width="70" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.lossCount > 0" style="color: #f56c6c">-{{ scope.row.lossCount }}</span>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column prop="diffAmount" label="差异金额" width="100" align="right">
          <template slot-scope="scope">{{ formatMoney(scope.row.diffAmount) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" @click="handleStart(scope.row)">开始</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleComplete(scope.row)">完成</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="盘点单号" prop="stocktakingNo">
              <el-input v-model="form.stocktakingNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="盘点类型" prop="stocktakingType">
              <el-select v-model="form.stocktakingType" placeholder="请选择类型" style="width: 100%">
                <el-option label="全盘" :value="0" />
                <el-option label="抽盘" :value="1" />
                <el-option label="动碰盘" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="仓库" prop="warehouseName">
              <el-input v-model="form.warehouseName" placeholder="请选择仓库" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="盘点日期" prop="stocktakingDate">
              <el-date-picker v-model="form.stocktakingDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="盘点人" prop="stocktakerName">
              <el-input v-model="form.stocktakerName" placeholder="请输入盘点人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划数量" prop="planCount">
              <el-input-number v-model="form.planCount" :min="0" style="width: 100%" />
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
  name: 'WmStocktaking',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        stocktakingNo: '',
        warehouseName: '',
        stocktakingType: null,
        status: null
      },
      stats: {
        total: 0,
        inProgress: 0,
        completed: 0,
        profit: 0,
        loss: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        stocktakingNo: '',
        warehouseId: null,
        warehouseName: '',
        stocktakingType: 0,
        stocktakingDate: null,
        stocktakerId: null,
        stocktakerName: '',
        planCount: 0,
        remark: ''
      },
      rules: {
        warehouseName: [{ required: true, message: '请选择仓库', trigger: 'blur' }],
        stocktakingType: [{ required: true, message: '请选择盘点类型', trigger: 'change' }],
        stocktakingDate: [{ required: true, message: '请选择盘点日期', trigger: 'change' }]
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
            stocktakingId: 1,
            stocktakingNo: 'ST20240317001',
            warehouseName: '原材料仓库',
            stocktakingType: 0,
            stocktakingDate: '2024-03-17',
            stocktakerName: '张三',
            planCount: 100,
            actualCount: 102,
            profitCount: 2,
            lossCount: 0,
            diffAmount: 500.00,
            status: 2,
            createTime: '2024-03-17 10:00:00'
          },
          {
            stocktakingId: 2,
            stocktakingNo: 'ST20240317002',
            warehouseName: '成品仓库',
            stocktakingType: 1,
            stocktakingDate: '2024-03-17',
            stocktakerName: '李四',
            planCount: 50,
            actualCount: 48,
            profitCount: 0,
            lossCount: 2,
            diffAmount: -300.00,
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
      this.stats.inProgress = this.tableData.filter(item => item.status === 1).length
      this.stats.completed = this.tableData.filter(item => item.status === 2).length
      this.stats.profit = this.tableData.reduce((sum, item) => sum + (item.profitCount || 0), 0)
      this.stats.loss = this.tableData.reduce((sum, item) => sum + (item.lossCount || 0), 0)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        stocktakingNo: '',
        warehouseName: '',
        stocktakingType: null,
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
      this.dialogTitle = '新增盘点单'
      this.form = {
        stocktakingNo: '',
        warehouseId: null,
        warehouseName: '',
        stocktakingType: 0,
        stocktakingDate: new Date(),
        stocktakerId: null,
        stocktakerName: '',
        planCount: 0,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleEdit(row) {
      this.dialogTitle = '编辑盘点单'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该盘点单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleStart(row) {
      this.$confirm('确认开始盘点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('盘点已开始')
        this.fetchData()
      }).catch(() => {})
    },
    handleComplete(row) {
      this.$confirm('确认完成盘点吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        this.$message.success('盘点已完成')
        this.fetchData()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success(this.form.stocktakingId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    formatMoney(value) {
      if (!value) return '¥0.00'
      return '¥' + parseFloat(value).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    },
    getTypeType(type) {
      const types = { 0: 'primary', 1: 'warning', 2: 'success' }
      return types[type] || 'info'
    },
    getTypeText(type) {
      const texts = { 0: '全盘', 1: '抽盘', 2: '动碰盘' }
      return texts[type] || '未知'
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '盘点中', 2: '已完成', 3: '已取消' }
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
      &.red { color: #f56c6c; }
      &.gray { color: #909399; }
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
