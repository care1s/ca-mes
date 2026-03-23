<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-check"></i>
        <span class="title">生产报工</span>
        <span class="subtitle">Production Feedback</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增报工</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="报工单号">
          <el-input v-model="queryParams.feedbackCode" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="工单编码">
          <el-input v-model="queryParams.workorderCode" placeholder="请输入工单编码" clearable />
        </el-form-item>
        <el-form-item label="工序名称">
          <el-input v-model="queryParams.processName" placeholder="请输入工序名称" clearable />
        </el-form-item>
        <el-form-item label="操作员">
          <el-input v-model="queryParams.operatorName" placeholder="请输入操作员" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="已提交" value="SUBMITTED" />
            <el-option label="已审核" value="APPROVED" />
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
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="feedbackCode" label="报工单号" width="150" show-overflow-tooltip />
        <el-table-column prop="workorderCode" label="工单编码" width="140" />
        <el-table-column prop="taskCode" label="任务编码" width="140" />
        <el-table-column prop="processName" label="工序名称" min-width="120" />
        <el-table-column prop="workstationName" label="工作站" width="120" />
        <el-table-column prop="quantity" label="报工数量" width="100" align="right" />
        <el-table-column prop="qualifiedQuantity" label="合格数" width="90" align="right">
          <template slot-scope="scope">
            <span style="color: #67c23a">{{ scope.row.qualifiedQuantity || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="defectiveQuantity" label="不良数" width="90" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c">{{ scope.row.defectiveQuantity || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作员" width="100" />
        <el-table-column prop="feedbackTime" label="报工时间" width="160" />
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 'APPROVED' ? 'success' : 'info'" size="small">
              {{ scope.row.status === 'APPROVED' ? '已审核' : '已提交' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status !== 'APPROVED'" type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status !== 'APPROVED'" type="text" size="small" style="color: #67c23a" @click="handleApprove(scope.row)">审核</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false" :modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报工单号" prop="feedbackCode">
              <el-input v-model="form.feedbackCode" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报工时间" prop="feedbackTime">
              <el-date-picker v-model="form.feedbackTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产任务" prop="taskId">
              <el-select v-model="form.taskId" placeholder="选择任务" style="width: 100%" filterable @change="handleTaskChange">
                <el-option v-for="item in taskList" :key="item.taskId" :label="item.taskCode + ' - ' + item.processName" :value="item.taskId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工单编码">
              <el-input v-model="form.workorderCode" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工序">
              <el-input v-model="form.processName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工作站">
              <el-input v-model="form.workstationName" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="报工数量" prop="quantity">
              <el-input-number v-model="form.quantity" :min="0.01" :precision="2" style="width: 100%" @change="calcTotal" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合格数量" prop="qualifiedQuantity">
              <el-input-number v-model="form.qualifiedQuantity" :min="0" :precision="2" style="width: 100%" @change="calcDefective" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="不良数量">
              <el-input-number v-model="form.defectiveQuantity" :min="0" :precision="2" style="width: 100%" disabled />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="操作员" prop="operatorName">
              <el-input v-model="form.operatorName" placeholder="请输入操作员姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报废数量">
              <el-input-number v-model="form.scrapQuantity" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="报工详情" :visible.sync="viewDialogVisible" width="600px" :modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报工单号">{{ viewForm.feedbackCode }}</el-descriptions-item>
        <el-descriptions-item label="报工时间">{{ viewForm.feedbackTime }}</el-descriptions-item>
        <el-descriptions-item label="工单编码">{{ viewForm.workorderCode }}</el-descriptions-item>
        <el-descriptions-item label="任务编码">{{ viewForm.taskCode }}</el-descriptions-item>
        <el-descriptions-item label="工序">{{ viewForm.processName }}</el-descriptions-item>
        <el-descriptions-item label="工作站">{{ viewForm.workstationName }}</el-descriptions-item>
        <el-descriptions-item label="报工数量">{{ viewForm.quantity }}</el-descriptions-item>
        <el-descriptions-item label="合格数量">
          <span style="color: #67c23a">{{ viewForm.qualifiedQuantity || 0 }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="不良数量">
          <span style="color: #f56c6c">{{ viewForm.defectiveQuantity || 0 }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="报废数量">{{ viewForm.scrapQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="操作员">{{ viewForm.operatorName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewForm.status === 'APPROVED' ? 'success' : 'info'">
            {{ viewForm.status === 'APPROVED' ? '已审核' : '已提交' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listProFeedback, getProFeedback, addProFeedback, updateProFeedback, delProFeedback, approveFeedback } from '@/api/pro'
import { listProTask } from '@/api/pro'

export default {
  name: 'ProFeedback',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        feedbackCode: '',
        workorderCode: '',
        processName: '',
        operatorName: '',
        status: ''
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      form: {
        feedbackId: null,
        feedbackCode: '',
        workorderId: null,
        workorderCode: '',
        taskId: null,
        taskCode: '',
        processId: null,
        processCode: '',
        processName: '',
        workstationId: null,
        workstationName: '',
        quantity: 0,
        qualifiedQuantity: 0,
        defectiveQuantity: 0,
        scrapQuantity: 0,
        operatorId: null,
        operatorName: '',
        feedbackTime: '',
        status: 'SUBMITTED',
        remark: ''
      },
      viewForm: {},
      rules: {
        taskId: [{ required: true, message: '请选择生产任务', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入报工数量', trigger: 'blur' }],
        qualifiedQuantity: [{ required: true, message: '请输入合格数量', trigger: 'blur' }],
        operatorName: [{ required: true, message: '请输入操作员姓名', trigger: 'blur' }],
        feedbackTime: [{ required: true, message: '请选择报工时间', trigger: 'change' }]
      },
      taskList: []
    }
  },
  created() {
    this.fetchData()
    this.loadTaskList()
  },
  methods: {
    fetchData() {
      this.loading = true
      listProFeedback(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadTaskList() {
      listProTask({ pageNum: 1, pageSize: 100 }).then(response => {
        this.taskList = response.rows || []
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        feedbackCode: '',
        workorderCode: '',
        processName: '',
        operatorName: '',
        status: ''
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
      this.dialogTitle = '新增报工'
      this.form = {
        feedbackId: null,
        feedbackCode: '',
        workorderId: null,
        workorderCode: '',
        taskId: null,
        taskCode: '',
        processId: null,
        processCode: '',
        processName: '',
        workstationId: null,
        workstationName: '',
        quantity: 0,
        qualifiedQuantity: 0,
        defectiveQuantity: 0,
        scrapQuantity: 0,
        operatorId: null,
        operatorName: '',
        feedbackTime: this.formatDateTime(new Date()),
        status: 'SUBMITTED',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewForm = { ...row }
      this.viewDialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑报工'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除报工单 "${row.feedbackCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProFeedback(row.feedbackId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleApprove(row) {
      this.$confirm(`确认审核通过报工单 "${row.feedbackCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        approveFeedback(row.feedbackId, 'APPROVED').then(() => {
          this.$message.success('审核成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleTaskChange(taskId) {
      const task = this.taskList.find(t => t.taskId === taskId)
      if (task) {
        this.form.workorderId = task.workorderId
        this.form.workorderCode = task.workorderCode
        this.form.taskCode = task.taskCode
        this.form.processId = task.processId
        this.form.processCode = task.processCode
        this.form.processName = task.processName
        this.form.workstationId = task.workstationId
        this.form.workstationName = task.workstationName
      }
    },
    calcTotal() {
      this.calcDefective()
    },
    calcDefective() {
      this.form.defectiveQuantity = (this.form.quantity || 0) - (this.form.qualifiedQuantity || 0)
      if (this.form.defectiveQuantity < 0) {
        this.form.defectiveQuantity = 0
        this.form.qualifiedQuantity = this.form.quantity
      }
    },
    formatDateTime(date) {
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      const h = String(date.getHours()).padStart(2, '0')
      const mi = String(date.getMinutes()).padStart(2, '0')
      const s = String(date.getSeconds()).padStart(2, '0')
      return `${y}-${m}-${d} ${h}:${mi}:${s}`
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 校验合格数量不能超过报工数量
          if (this.form.qualifiedQuantity > this.form.quantity) {
            this.$message.error('合格数量不能超过报工数量')
            return
          }
          if (this.form.feedbackId) {
            updateProFeedback(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addProFeedback(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
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
      color: #e6a23c;
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

::v-deep .el-loading-mask {
  display: none !important;
}
</style>
