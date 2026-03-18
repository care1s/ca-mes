<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pur">
      <div class="title-section">
        <i class="el-icon-document"></i>
        <span class="title">采购申请</span>
        <span class="subtitle">Purchase Request</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增申请</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 - 单行展示 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待审批</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已审批</span>
        <span class="stat-value green">{{ stats.approved }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="申请单号">
          <el-input v-model="queryParams.requestCode" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="申请类型">
          <el-select v-model="queryParams.requestType" placeholder="请选择类型" clearable>
            <el-option label="普通" value="NORMAL" />
            <el-option label="紧急" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
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
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-document"></i>
          采购申请列表
        </span>
      </div>
      
      <el-table
        
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @row-dblclick="handleRowDblclick"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="requestCode" label="申请单号" width="150" show-overflow-tooltip />
        <el-table-column prop="requestDate" label="申请日期" width="120" align="center" />
        <el-table-column prop="requestType" label="类型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.requestType === 'URGENT'" type="danger" size="mini">紧急</el-tag>
            <el-tag v-else type="info" size="mini">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="申请部门" width="120" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.totalAmount">{{ scope.row.totalAmount }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
            <el-tag v-else-if="scope.row.status === 'PENDING'" type="warning" size="mini">待审批</el-tag>
            <el-tag v-else-if="scope.row.status === 'APPROVED'" type="success" size="mini">已审批</el-tag>
            <el-tag v-else-if="scope.row.status === 'REJECTED'" type="danger" size="mini">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :modal="false" custom-class="no-mask-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请单号" prop="requestCode">
              <el-input v-model="form.requestCode" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请日期" prop="requestDate">
              <el-date-picker v-model="form.requestDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请类型" prop="requestType">
              <el-select v-model="form.requestType" placeholder="请选择类型" style="width: 100%">
                <el-option label="普通" value="NORMAL" />
                <el-option label="紧急" value="URGENT" />
              </el-select>
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
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurRequest, addPurRequest, updatePurRequest, delPurRequest } from '@/api/md'

export default {
  name: 'PurRequest',
  data() {
    return {
      loading: false,
      total: 0,
      stats: {
        total: 0,
        pending: 0,
        approved: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        requestCode: '',
        requestType: '',
        status: ''
      },
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        requestId: null,
        requestCode: '',
        requestDate: '',
        requestType: 'NORMAL',
        applicantName: '',
        deptName: '',
        totalAmount: 0,
        status: 'DRAFT',
        remark: ''
      },
      rules: {
        requestDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }],
        requestType: [{ required: true, message: '请选择申请类型', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listPurRequest(this.queryParams)
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.updateStatistics()
      } catch (error) {
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    
    updateStatistics() {
      this.stats.total = this.tableData.length
      this.stats.pending = this.tableData.filter(item => item.status === 'PENDING').length
      this.stats.approved = this.tableData.filter(item => item.status === 'APPROVED').length
    },
    
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        requestCode: '',
        requestType: '',
        status: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.dialogTitle = '新增采购申请'
      this.form = {
        requestId: null,
        requestCode: '',
        requestDate: new Date(),
        requestType: 'NORMAL',
        applicantName: '',
        deptName: '',
        totalAmount: 0,
        status: 'DRAFT',
        remark: ''
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑采购申请'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleView(row) {
      this.$alert(`单号：${row.requestCode}<br>类型：${row.requestType === 'URGENT' ? '紧急' : '普通'}<br>申请人：${row.applicantName || '-'}<br>部门：${row.deptName || '-'}`, '申请详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    
    handleRowDblclick(row) {
      this.handleView(row)
    },
    
    handleDelete(row) {
      this.$confirm(`确定删除申请单 "${row.requestCode}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await delPurRequest(row.requestId)
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    
    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.form.requestId) {
              await updatePurRequest(this.form)
              this.$message.success('更新成功')
            } else {
              await addPurRequest(this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('操作失败')
          }
        }
      })
    },
    
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 24px;
      color: #409EFF;
      margin-right: 10px;
    }
    
    .title {
      font-size: 20px;
      font-weight: bold;
      margin-right: 10px;
    }
    
    .subtitle {
      font-size: 14px;
      color: #909399;
    }
  }
}

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
      &.orange { color: #e6a23c; }
      &.green { color: #67c23a; }
    }
  }
  
  .stat-divider {
    width: 1px;
    height: 20px;
    background: #ebeef5;
    margin: 0 20px;
  }
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;
  }
}

.no-mask-dialog {
  .el-dialog {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3) !important;
  }
}
</style>
