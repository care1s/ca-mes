<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pro">
      <div class="title-section">
        <i class="el-icon-s-tools"></i>
        <span class="title">工序管理</span>
        <span class="subtitle">Process Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增工序</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="工序编码">
          <el-input v-model="queryParams.processCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="工序名称">
          <el-input v-model="queryParams.processName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="工序类型">
          <el-select v-model="queryParams.processType" placeholder="请选择类型" clearable>
            <el-option label="普通工序" value="NORMAL" />
            <el-option label="检验工序" value="INSPECT" />
            <el-option label="包装工序" value="PACKAGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="0" />
            <el-option label="停用" value="1" />
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
      <el-table :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="processCode" label="工序编码" width="120" />
        <el-table-column prop="processName" label="工序名称" min-width="150" />
        <el-table-column prop="processType" label="工序类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getProcessTypeType(scope.row.processType)" size="small">
              {{ getProcessTypeText(scope.row.processType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="workshopName" label="所属车间" width="120" />
        <el-table-column prop="standardHours" label="标准工时" width="100" align="right">
          <template slot-scope="scope">{{ scope.row.standardHours }}h</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'" size="small">
              {{ scope.row.status === '0' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === '1'" type="text" size="small" @click="handleEnable(scope.row)">启用</el-button>
            <el-button v-if="scope.row.status === '0'" type="text" size="small" style="color: #e6a23c" @click="handleDisable(scope.row)">停用</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false" :modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工序编码" prop="processCode">
              <el-input v-model="form.processCode" placeholder="请输入编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="工序名称" prop="processName">
              <el-input v-model="form.processName" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工序类型" prop="processType">
              <el-select v-model="form.processType" placeholder="请选择类型" style="width: 100%">
                <el-option label="普通工序" value="NORMAL" />
                <el-option label="检验工序" value="INSPECT" />
                <el-option label="包装工序" value="PACKAGE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属车间" prop="workshopId">
              <el-select v-model="form.workshopId" placeholder="请选择车间" style="width: 100%" @change="handleWorkshopChange">
                <el-option v-for="item in workshopList" :key="item.workshopId" :label="item.workshopName" :value="item.workshopId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标准工时" prop="standardHours">
              <el-input-number v-model="form.standardHours" :min="0" :precision="2" style="width: 100%" />
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
    <el-dialog title="工序详情" :visible.sync="viewDialogVisible" width="500px" :modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="工序编码">{{ viewForm.processCode }}</el-descriptions-item>
        <el-descriptions-item label="工序名称">{{ viewForm.processName }}</el-descriptions-item>
        <el-descriptions-item label="工序类型">
          <el-tag :type="getProcessTypeType(viewForm.processType)">{{ getProcessTypeText(viewForm.processType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="所属车间">{{ viewForm.workshopName }}</el-descriptions-item>
        <el-descriptions-item label="标准工时">{{ viewForm.standardHours }}小时</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewForm.status === '0' ? 'success' : 'danger'">
            {{ viewForm.status === '0' ? '启用' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listProProcess, getProProcess, addProProcess, updateProProcess, delProProcess, enableProcess, disableProcess } from '@/api/pro'

export default {
  name: 'ProProcess',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        processCode: '',
        processName: '',
        processType: '',
        status: ''
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      form: {
        processId: null,
        processCode: '',
        processName: '',
        processType: 'NORMAL',
        workshopId: null,
        workshopName: '',
        standardHours: 0,
        remark: ''
      },
      viewForm: {},
      rules: {
        processCode: [{ required: true, message: '请输入工序编码', trigger: 'blur' }],
        processName: [{ required: true, message: '请输入工序名称', trigger: 'blur' }],
        processType: [{ required: true, message: '请选择工序类型', trigger: 'change' }],
        workshopId: [{ required: true, message: '请选择所属车间', trigger: 'change' }]
      },
      workshopList: [
        { workshopId: 1, workshopName: '总装车间' },
        { workshopId: 2, workshopName: '注塑车间' },
        { workshopId: 3, workshopName: '钣金车间' }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listProProcess(this.queryParams).then(response => {
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
        processCode: '',
        processName: '',
        processType: '',
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
      this.dialogTitle = '新增工序'
      this.form = {
        processId: null,
        processCode: '',
        processName: '',
        processType: 'NORMAL',
        workshopId: null,
        workshopName: '',
        standardHours: 0,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewForm = { ...row }
      this.viewDialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑工序'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该工序吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delProProcess(row.processId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleEnable(row) {
      this.$confirm('确认启用该工序吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        enableProcess(row.processId).then(() => {
          this.$message.success('工序已启用')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleDisable(row) {
      this.$confirm('确认停用该工序吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        disableProcess(row.processId).then(() => {
          this.$message.success('工序已停用')
          this.fetchData()
        })
      }).catch(() => {})
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
          if (this.form.processId) {
            updateProProcess(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addProProcess(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    getProcessTypeType(type) {
      const types = { NORMAL: 'primary', INSPECT: 'warning', PACKAGE: 'success' }
      return types[type] || 'info'
    },
    getProcessTypeText(type) {
      const texts = { NORMAL: '普通', INSPECT: '检验', PACKAGE: '包装' }
      return texts[type] || type
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
