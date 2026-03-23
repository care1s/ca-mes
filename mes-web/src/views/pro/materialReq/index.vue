<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-s-goods"></i>
        <span class="title">物料需求</span>
        <span class="subtitle">Material Requirement</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增需求</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">需求总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待审核</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已发料</span>
        <span class="stat-value warning">{{ stats.issued }}</span>
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
        <el-form-item label="需求单号">
          <el-input v-model="queryParams.reqNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="计划单号">
          <el-input v-model="queryParams.planNo" placeholder="请输入计划单号" clearable />
        </el-form-item>
        <el-form-item label="需求类型">
          <el-select v-model="queryParams.reqType" placeholder="请选择类型" clearable>
            <el-option label="计划需求" :value="0" />
            <el-option label="紧急需求" :value="1" />
            <el-option label="补料需求" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="待审核" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="已发料" :value="3" />
            <el-option label="已完成" :value="4" />
            <el-option label="已取消" :value="5" />
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
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="reqNo" label="需求单号" width="140" />
        <el-table-column prop="planNo" label="计划单号" width="140" />
        <el-table-column prop="reqType" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.reqType)" size="small">
              {{ getTypeText(scope.row.reqType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reqDate" label="需求日期" width="100" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="itemCount" label="物料种类" width="90" align="right" />
        <el-table-column prop="totalQty" label="总数量" width="90" align="right" />
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
            <el-button v-if="scope.row.status === 0" type="text" size="small" @click="handleSubmit(scope.row)">提交</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleApprove(scope.row)">审核</el-button>
            <el-button v-if="scope.row.status === 2" type="text" size="small" @click="handleIssue(scope.row)">发料</el-button>
            <el-button v-if="scope.row.status === 3" type="text" size="small" @click="handleComplete(scope.row)">完成</el-button>
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
            <el-form-item label="需求单号" prop="reqNo">
              <el-input v-model="form.reqNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需求类型" prop="reqType">
              <el-select v-model="form.reqType" placeholder="请选择类型" style="width: 100%">
                <el-option label="计划需求" :value="0" />
                <el-option label="紧急需求" :value="1" />
                <el-option label="补料需求" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="关联计划" prop="planNo">
              <el-input v-model="form.planNo" placeholder="请选择关联计划" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="需求日期" prop="reqDate">
              <el-date-picker v-model="form.reqDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请人" prop="applicantName">
              <el-input v-model="form.applicantName" placeholder="请输入申请人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门" />
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
  name: 'ProMaterialReq',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        reqNo: '',
        planNo: '',
        reqType: null,
        status: null
      },
      stats: {
        total: 0,
        pending: 0,
        issued: 0,
        completed: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        reqNo: '',
        planId: null,
        planNo: '',
        reqType: 0,
        reqDate: null,
        applicantId: null,
        applicantName: '',
        deptName: '',
        remark: ''
      },
      rules: {
        reqType: [{ required: true, message: '请选择需求类型', trigger: 'change' }],
        reqDate: [{ required: true, message: '请选择需求日期', trigger: 'change' }],
        applicantName: [{ required: true, message: '请输入申请人', trigger: 'blur' }]
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
            reqId: 1,
            reqNo: 'MR20240317001',
            planNo: 'PL20240317001',
            reqType: 0,
            reqDate: '2024-03-17',
            applicantName: '张三',
            deptName: '生产一部',
            itemCount: 5,
            totalQty: 500,
            status: 3,
            createTime: '2024-03-17 09:00:00'
          },
          {
            reqId: 2,
            reqNo: 'MR20240317002',
            planNo: 'PL20240317002',
            reqType: 1,
            reqDate: '2024-03-17',
            applicantName: '李四',
            deptName: '生产二部',
            itemCount: 3,
            totalQty: 200,
            status: 1,
            createTime: '2024-03-17 10:00:00'
          }
        ]
        this.total = 2
        this.calculateStats()
        this.loading = false
      }, 500)
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.pending = this.tableData.filter(item => item.status === 1).length
      this.stats.issued = this.tableData.filter(item => item.status === 3).length
      this.stats.completed = this.tableData.filter(item => item.status === 4).length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        reqNo: '',
        planNo: '',
        reqType: null,
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
      this.dialogTitle = '新增物料需求'
      this.form = {
        reqNo: '',
        planId: null,
        planNo: '',
        reqType: 0,
        reqDate: new Date(),
        applicantId: null,
        applicantName: '',
        deptName: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleEdit(row) {
      this.dialogTitle = '编辑物料需求'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该物料需求单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleSubmit(row) {
      this.$confirm('确认提交审核吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('提交成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleApprove(row) {
      this.$confirm('确认审核通过吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('审核通过')
        this.fetchData()
      }).catch(() => {})
    },
    handleIssue(row) {
      this.$confirm('确认发料吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('发料完成')
        this.fetchData()
      }).catch(() => {})
    },
    handleComplete(row) {
      this.$confirm('确认完成该需求单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'success'
      }).then(() => {
        this.$message.success('需求完成')
        this.fetchData()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success(this.form.reqId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getTypeType(type) {
      const types = { 0: 'primary', 1: 'danger', 2: 'warning' }
      return types[type] || 'info'
    },
    getTypeText(type) {
      const texts = { 0: '计划需求', 1: '紧急需求', 2: '补料需求' }
      return texts[type] || '未知'
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'primary', 4: 'success', 5: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '待审核', 2: '已审核', 3: '已发料', 4: '已完成', 5: '已取消' }
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
