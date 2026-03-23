<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-s-claim"></i>
        <span class="title">生产任务</span>
        <span class="subtitle">Production Task</span>
      </div>
      <div class="action-section">
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="任务编码">
          <el-input v-model="queryParams.taskCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="工单编码">
          <el-input v-model="queryParams.workorderCode" placeholder="请输入工单编码" clearable />
        </el-form-item>
        <el-form-item label="工序名称">
          <el-input v-model="queryParams.processName" placeholder="请输入工序名称" clearable />
        </el-form-item>
        <el-form-item label="任务状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="待执行" value="PENDING" />
            <el-option label="执行中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
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
        <el-table-column prop="taskCode" label="任务编码" width="150" show-overflow-tooltip />
        <el-table-column prop="workorderCode" label="工单编码" width="150" />
        <el-table-column prop="processName" label="工序名称" min-width="120" />
        <el-table-column prop="processCode" label="工序编码" width="100" />
        <el-table-column prop="workstationName" label="工作站" width="120" />
        <el-table-column prop="planQuantity" label="计划数量" width="100" align="right" />
        <el-table-column prop="completedQuantity" label="完工数量" width="100" align="right" />
        <el-table-column prop="qualifiedQuantity" label="合格数量" width="100" align="right" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operatorName" label="操作员" width="100" />
        <el-table-column prop="planStartTime" label="计划开始" width="160" />
        <el-table-column prop="planEndTime" label="计划完成" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              style="color: #67c23a"
              @click="handleStart(scope.row)"
            >开始</el-button>
            <el-button
              v-if="scope.row.status === 'PROCESSING'"
              type="text"
              size="small"
              style="color: #409eff"
              @click="handleComplete(scope.row)"
            >完成</el-button>
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

    <!-- 查看详情对话框 -->
    <el-dialog title="任务详情" :visible.sync="viewDialogVisible" width="600px" :modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务编码">{{ viewForm.taskCode }}</el-descriptions-item>
        <el-descriptions-item label="工单编码">{{ viewForm.workorderCode }}</el-descriptions-item>
        <el-descriptions-item label="工序">{{ viewForm.processName }} ({{ viewForm.processCode }})</el-descriptions-item>
        <el-descriptions-item label="工作站">{{ viewForm.workstationName }}</el-descriptions-item>
        <el-descriptions-item label="计划数量">{{ viewForm.planQuantity }}</el-descriptions-item>
        <el-descriptions-item label="完工数量">{{ viewForm.completedQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="合格数量">{{ viewForm.qualifiedQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="不良数量">{{ viewForm.defectiveQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewForm.status)">{{ getStatusText(viewForm.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作员">{{ viewForm.operatorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划开始">{{ viewForm.planStartTime }}</el-descriptions-item>
        <el-descriptions-item label="计划完成">{{ viewForm.planEndTime }}</el-descriptions-item>
        <el-descriptions-item label="实际开始">{{ viewForm.actualStartTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="实际完成">{{ viewForm.actualEndTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 任务完成对话框 -->
    <el-dialog title="任务完成报工" :visible.sync="completeDialogVisible" width="500px" :modal="false">
      <el-form ref="completeForm" :model="completeForm" label-width="100px">
        <el-form-item label="任务编码">
          <span>{{ completeForm.taskCode }}</span>
        </el-form-item>
        <el-form-item label="工序">
          <span>{{ completeForm.processName }}</span>
        </el-form-item>
        <el-form-item label="完工数量" prop="completedQuantity">
          <el-input-number v-model="completeForm.completedQuantity" :min="0" :max="completeForm.planQuantity" style="width: 100%" />
        </el-form-item>
        <el-form-item label="合格数量" prop="qualifiedQuantity">
          <el-input-number v-model="completeForm.qualifiedQuantity" :min="0" :max="completeForm.completedQuantity" style="width: 100%" />
        </el-form-item>
        <el-form-item label="不良数量">
          <el-input-number v-model="completeForm.defectiveQuantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="completeForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="completeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitComplete">确认完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProTask, getProTask, delProTask, startTask, completeTask, updateProTask } from '@/api/pro'

export default {
  name: 'ProTask',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        taskCode: '',
        workorderCode: '',
        processName: '',
        status: ''
      },
      viewDialogVisible: false,
      viewForm: {},
      completeDialogVisible: false,
      completeForm: {
        taskId: null,
        taskCode: '',
        processName: '',
        planQuantity: 0,
        completedQuantity: 0,
        qualifiedQuantity: 0,
        defectiveQuantity: 0,
        remark: ''
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listProTask(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
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
        taskCode: '',
        workorderCode: '',
        processName: '',
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
    handleView(row) {
      this.viewForm = { ...row }
      this.viewDialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除任务 "${row.taskCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProTask(row.taskId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleStart(row) {
      this.$confirm(`确认开始执行任务 "${row.taskCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        startTask(row.taskId).then(() => {
          this.$message.success('任务已开始')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleComplete(row) {
      this.completeForm = {
        taskId: row.taskId,
        taskCode: row.taskCode,
        processName: row.processName,
        planQuantity: row.planQuantity,
        completedQuantity: row.planQuantity,
        qualifiedQuantity: row.planQuantity,
        defectiveQuantity: 0,
        remark: ''
      }
      this.completeDialogVisible = true
    },
    submitComplete() {
      // 先更新任务数量信息
      const updateData = {
        taskId: this.completeForm.taskId,
        completedQuantity: this.completeForm.completedQuantity,
        qualifiedQuantity: this.completeForm.qualifiedQuantity,
        defectiveQuantity: this.completeForm.defectiveQuantity,
        remark: this.completeForm.remark
      }
      updateProTask(updateData).then(() => {
        // 再完成任务
        completeTask(this.completeForm.taskId).then(() => {
          this.$message.success('任务已完成')
          this.completeDialogVisible = false
          this.fetchData()
        })
      })
    },
    getStatusType(status) {
      const types = {
        PENDING: 'info',
        PROCESSING: 'warning',
        COMPLETED: 'success'
      }
      return types[status] || ''
    },
    getStatusText(status) {
      const texts = {
        PENDING: '待执行',
        PROCESSING: '执行中',
        COMPLETED: '已完成'
      }
      return texts[status] || status
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
