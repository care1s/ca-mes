<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-document"></i>
        <span class="title">生产工单</span>
        <span class="subtitle">Workorder Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增工单</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="工单编码">
          <el-input v-model="queryParams.workorderCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="产品名称">
          <el-input v-model="queryParams.itemName" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="工单状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="待下达" value="PENDING" />
            <el-option label="已下达" value="RELEASED" />
            <el-option label="生产中" value="PRODUCING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已关闭" value="CLOSED" />
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
        <el-table-column prop="workorderCode" label="工单编码" width="150" show-overflow-tooltip />
        <el-table-column prop="itemName" label="产品名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="itemCode" label="产品编码" width="120" />
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
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.priority === 'HIGH'" type="danger" size="small">高</el-tag>
            <el-tag v-else-if="scope.row.priority === 'LOW'" type="info" size="small">低</el-tag>
            <span v-else>正常</span>
          </template>
        </el-table-column>
        <el-table-column prop="planStartTime" label="计划开始" width="160" />
        <el-table-column prop="planEndTime" label="计划完成" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              style="color: #67c23a"
              @click="handleRelease(scope.row)"
            >下达</el-button>
            <el-button
              v-if="scope.row.status !== 'CLOSED' && scope.row.status !== 'PENDING'"
              type="text"
              size="small"
              style="color: #e6a23c"
              @click="handleClose(scope.row)"
            >关闭</el-button>
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
            <el-form-item label="工单编码" prop="workorderCode">
              <el-input
                v-model="form.workorderCode"
                placeholder="留空则自动生成"
                :disabled="!!form.workorderId"
              />
              <div v-if="!form.workorderId" class="input-tip">如不填写，系统将根据规则自动生成</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工单类型" prop="workorderType">
              <el-select v-model="form.workorderType" placeholder="请选择类型" style="width: 100%">
                <el-option label="标准工单" value="STANDARD" />
                <el-option label="返工工单" value="REWORK" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="生产计划" prop="planId">
              <el-select v-model="form.planId" placeholder="请选择生产计划（必选项）" style="width: 100%" filterable @change="handlePlanChange">
                <el-option v-for="item in planList" :key="item.planId" :label="item.planNo" :value="item.planId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="产品">
              <div v-if="form.itemName" class="product-display">
                <el-tag type="info" size="medium">
                  <i class="el-icon-goods" /> {{ form.itemName }}
                </el-tag>
                <span class="product-code">({{ form.itemCode }})</span>
              </div>
              <el-input v-else disabled placeholder="选择生产计划后自动显示" />
              <div class="input-tip">
                <i class="el-icon-info" /> 产品信息从生产计划自动获取
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工艺路线" prop="routeId">
              <el-select v-model="form.routeId" placeholder="请选择工艺路线（必选项）" style="width: 100%" filterable>
                <el-option v-for="item in routeList" :key="item.routeId" :label="item.routeName" :value="item.routeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划数量" prop="planQuantity">
              <el-input-number v-model="form.planQuantity" :min="1" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="计划开始" prop="planStartTime">
              <el-date-picker v-model="form.planStartTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计划完成" prop="planEndTime">
              <el-date-picker v-model="form.planEndTime" type="datetime" placeholder="选择时间" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="选择优先级" style="width: 100%">
                <el-option label="高" value="HIGH" />
                <el-option label="正常" value="NORMAL" />
                <el-option label="低" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生产车间" prop="workshopId">
              <el-select v-model="form.workshopId" placeholder="请选择车间（必选项）" style="width: 100%" filterable @change="handleWorkshopChange">
                <el-option v-for="item in workshopList" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
              </el-select>
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
    <el-dialog title="工单详情" :visible.sync="viewDialogVisible" width="700px" :modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="工单编码">{{ viewForm.workorderCode }}</el-descriptions-item>
        <el-descriptions-item label="工单类型">{{ viewForm.workorderType === 'REWORK' ? '返工工单' : '标准工单' }}</el-descriptions-item>
        <el-descriptions-item label="产品">{{ viewForm.itemName }} ({{ viewForm.itemCode }})</el-descriptions-item>
        <el-descriptions-item label="工艺路线">{{ viewForm.routeName }}</el-descriptions-item>
        <el-descriptions-item label="计划数量">{{ viewForm.planQuantity }}</el-descriptions-item>
        <el-descriptions-item label="完工数量">{{ viewForm.completedQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="合格数量">{{ viewForm.qualifiedQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="不良数量">{{ viewForm.defectiveQuantity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewForm.status)">{{ getStatusText(viewForm.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="优先级">
          <el-tag v-if="viewForm.priority === 'HIGH'" type="danger">高</el-tag>
          <el-tag v-else-if="viewForm.priority === 'LOW'" type="info">低</el-tag>
          <span v-else>正常</span>
        </el-descriptions-item>
        <el-descriptions-item label="计划开始">{{ viewForm.planStartTime }}</el-descriptions-item>
        <el-descriptions-item label="计划完成">{{ viewForm.planEndTime }}</el-descriptions-item>
        <el-descriptions-item label="生产车间">{{ viewForm.workshopName }}</el-descriptions-item>
        <el-descriptions-item label="生产计划">{{ viewForm.planNo }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listProWorkorder, getProWorkorder, addProWorkorder, updateProWorkorder, delProWorkorder, releaseWorkorder, closeWorkorder, listProPlan, listProRoute } from '@/api/pro'
import { getWorkshopOptions } from '@/api/md'

export default {
  name: 'ProWorkorder',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        workorderCode: '',
        itemName: '',
        status: ''
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      form: {
        workorderId: null,
        workorderCode: '',
        workorderType: 'STANDARD',
        planId: null,
        planNo: '',
        itemId: null,
        itemCode: '',
        itemName: '',
        routeId: null,
        routeName: '',
        planQuantity: 1,
        priority: 'NORMAL',
        workshopId: null,
        workshopName: '',
        planStartTime: '',
        planEndTime: '',
        remark: ''
      },
      viewForm: {},
      rules: {
        workorderType: [{ required: true, message: '请选择工单类型', trigger: 'change' }],
        planId: [{ required: true, message: '请选择生产计划', trigger: 'change' }],
        routeId: [{ required: true, message: '请选择工艺路线', trigger: 'change' }],
        planQuantity: [{ required: true, message: '请输入计划数量', trigger: 'blur' }],
        planStartTime: [{ required: true, message: '请选择计划开始时间', trigger: 'change' }],
        planEndTime: [{ required: true, message: '请选择计划完成时间', trigger: 'change' }],
        workshopId: [{ required: true, message: '请选择生产车间', trigger: 'change' }]
      },
      // 下拉选项数据
      planList: [],
      itemList: [],
      routeList: [],
      workshopList: []
    }
  },
  created() {
    this.fetchData()
    this.loadPlanList()
    this.loadWorkshopList()
    this.loadRouteList()
  },
  methods: {
    fetchData() {
      this.loading = true
      listProWorkorder(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadPlanList() {
      listProPlan({ pageNum: 1, pageSize: 100 }).then(response => {
        this.planList = response.rows || []
      })
    },
    loadWorkshopList() {
      getWorkshopOptions().then(response => {
        this.workshopList = response.data || []
      })
    },
    loadRouteList() {
      listProRoute({ pageNum: 1, pageSize: 100 }).then(response => {
        this.routeList = response.rows || []
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
        workorderCode: '',
        itemName: '',
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
      this.dialogTitle = '新增工单'
      this.form = {
        workorderId: null,
        workorderCode: '',
        workorderType: 'STANDARD',
        planId: null,
        planNo: '',
        itemId: null,
        itemCode: '',
        itemName: '',
        routeId: null,
        routeName: '',
        planQuantity: 1,
        priority: 'NORMAL',
        workshopId: null,
        workshopName: '',
        planStartTime: '',
        planEndTime: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewForm = { ...row }
      this.viewDialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑工单'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除工单 "${row.workorderCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProWorkorder(row.workorderId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleRelease(row) {
      this.$confirm(`确认下达工单 "${row.workorderCode}" 吗？下达后将自动生成生产任务。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        releaseWorkorder(row.workorderId).then(() => {
          this.$message.success('工单已下达，生产任务已生成')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleClose(row) {
      this.$confirm(`确认关闭工单 "${row.workorderCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        closeWorkorder(row.workorderId).then(() => {
          this.$message.success('工单已关闭')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handlePlanChange(planId) {
      const plan = this.planList.find(p => p.planId === planId)
      if (plan) {
        this.form.planNo = plan.planNo
        this.form.itemId = plan.itemId
        this.form.itemCode = plan.itemCode
        this.form.itemName = plan.itemName
      }
    },
    handleWorkshopChange(workshopId) {
      const workshop = this.workshopList.find(w => w.workshopId === workshopId)
      if (workshop) {
        this.form.workshopName = workshop.workshopName
      }
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.workorderId) {
            updateProWorkorder(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addProWorkorder(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    getStatusType(status) {
      const types = {
        PENDING: 'info',
        RELEASED: 'primary',
        PRODUCING: 'warning',
        COMPLETED: 'success',
        CLOSED: ''
      }
      return types[status] || ''
    },
    getStatusText(status) {
      const texts = {
        PENDING: '待下达',
        RELEASED: '已下达',
        PRODUCING: '生产中',
        COMPLETED: '已完成',
        CLOSED: '已关闭'
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

/* 输入框提示文字样式 */
.input-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
  line-height: 1.4;
}

/* 产品展示样式 */
.product-display {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
  border: 1px solid #e4e7ed;

  .product-code {
    font-size: 13px;
    color: #606266;
  }
}

/* 禁用输入框样式优化 */
::v-deep .el-input.is-disabled .el-input__inner {
  background-color: #f5f7fa;
  color: #606266;
}

/* 表单标签加粗 */
::v-deep .el-form-item__label {
  font-weight: 500;
}

/* 必填项标记 */
::v-deep .el-form-item.is-required .el-form-item__label::before {
  color: #f56c6c;
  font-weight: bold;
}

::v-deep .el-loading-mask {
  display: none !important;
}
</style>
