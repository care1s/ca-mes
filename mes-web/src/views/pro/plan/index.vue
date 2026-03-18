<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-s-order"></i>
        <span class="title">生产计划</span>
        <span class="subtitle">Production Plan</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增计划</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">计划总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">执行中</span>
        <span class="stat-value orange">{{ stats.inProgress }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">平均完成率</span>
        <span class="stat-value purple">{{ stats.avgRate }}%</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="计划单号">
          <el-input v-model="queryParams.planNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="计划名称">
          <el-input v-model="queryParams.planName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="计划类型">
          <el-select v-model="queryParams.planType" placeholder="请选择类型" clearable>
            <el-option label="周计划" :value="0" />
            <el-option label="月计划" :value="1" />
            <el-option label="季度计划" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="已发布" :value="1" />
            <el-option label="执行中" :value="2" />
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
        <el-table-column prop="planNo" label="计划单号" width="140" />
        <el-table-column prop="planName" label="计划名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="planType" label="类型" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.planType)" size="small">
              {{ getTypeText(scope.row.planType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="100" />
        <el-table-column prop="endDate" label="结束日期" width="100" />
        <el-table-column prop="planQty" label="计划产量" width="100" align="right" />
        <el-table-column prop="actualQty" label="实际产量" width="100" align="right" />
        <el-table-column prop="completionRate" label="完成率" width="100" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.completionRate" :color="getProgressColor(scope.row.completionRate)" />
          </template>
        </el-table-column>
        <el-table-column prop="managerName" label="负责人" width="100" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" @click="handlePublish(scope.row)">发布</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleStart(scope.row)">开始</el-button>
            <el-button v-if="scope.row.status === 2" type="text" size="small" @click="handleComplete(scope.row)">完成</el-button>
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
            <el-form-item label="计划单号" prop="planNo">
              <el-input v-model="form.planNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划类型" prop="planType">
              <el-select v-model="form.planType" placeholder="请选择类型" style="width: 100%">
                <el-option label="周计划" :value="0" />
                <el-option label="月计划" :value="1" />
                <el-option label="季度计划" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划产量" prop="planQty">
              <el-input-number v-model="form.planQty" :min="0" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="负责人" prop="managerName">
              <el-input v-model="form.managerName" placeholder="请输入负责人" />
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
  name: 'ProPlan',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        planNo: '',
        planName: '',
        planType: null,
        status: null
      },
      stats: {
        total: 0,
        inProgress: 0,
        completed: 0,
        avgRate: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        planNo: '',
        planName: '',
        planType: 0,
        startDate: null,
        endDate: null,
        planQty: 0,
        managerId: null,
        managerName: '',
        remark: ''
      },
      rules: {
        planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
        planType: [{ required: true, message: '请选择计划类型', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
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
            planId: 1,
            planNo: 'PL20240317001',
            planName: '3月第3周生产计划',
            planType: 0,
            startDate: '2024-03-17',
            endDate: '2024-03-23',
            planQty: 1000,
            actualQty: 850,
            completionRate: 85,
            managerName: '张三',
            status: 2,
            createTime: '2024-03-17 10:00:00'
          },
          {
            planId: 2,
            planNo: 'PL20240317002',
            planName: '3月份生产计划',
            planType: 1,
            startDate: '2024-03-01',
            endDate: '2024-03-31',
            planQty: 5000,
            actualQty: 3200,
            completionRate: 64,
            managerName: '李四',
            status: 2,
            createTime: '2024-03-01 09:00:00'
          }
        ]
        this.total = 2
        this.calculateStats()
        this.loading = false
      }, 500)
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.inProgress = this.tableData.filter(item => item.status === 2).length
      this.stats.completed = this.tableData.filter(item => item.status === 3).length
      const completedItems = this.tableData.filter(item => item.completionRate > 0)
      this.stats.avgRate = completedItems.length > 0 
        ? Math.round(completedItems.reduce((sum, item) => sum + item.completionRate, 0) / completedItems.length)
        : 0
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        planNo: '',
        planName: '',
        planType: null,
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
      this.dialogTitle = '新增生产计划'
      this.form = {
        planNo: '',
        planName: '',
        planType: 0,
        startDate: new Date(),
        endDate: null,
        planQty: 0,
        managerId: null,
        managerName: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleEdit(row) {
      this.dialogTitle = '编辑生产计划'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该生产计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handlePublish(row) {
      this.$confirm('确认发布该生产计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('计划发布成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleStart(row) {
      this.$confirm('确认开始执行该计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('计划开始执行')
        this.fetchData()
      }).catch(() => {})
    },
    handleComplete(row) {
      this.$confirm('确认完成该计划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        this.$message.success('计划已完成')
        this.fetchData()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success(this.form.planId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getTypeType(type) {
      const types = { 0: 'primary', 1: 'success', 2: 'warning' }
      return types[type] || 'info'
    },
    getTypeText(type) {
      const texts = { 0: '周计划', 1: '月计划', 2: '季度计划' }
      return texts[type] || '未知'
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'primary', 2: 'warning', 3: 'success', 4: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '已发布', 2: '执行中', 3: '已完成', 4: '已取消' }
      return texts[status] || '未知'
    },
    getProgressColor(rate) {
      if (rate >= 90) return '#67c23a'
      if (rate >= 60) return '#e6a23c'
      return '#f56c6c'
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
      &.purple { color: #9b59b6; }
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
