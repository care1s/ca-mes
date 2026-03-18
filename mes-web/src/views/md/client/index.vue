<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header md">
      <div class="title-section">
        <i class="el-icon-user"></i>
        <span class="title">客户管理</span>
        <span class="subtitle">Client Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
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
        <span class="stat-label">启用</span>
        <span class="stat-value green">{{ stats.active }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">停用</span>
        <span class="stat-value orange">{{ stats.inactive }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="编码">
          <el-input v-model="queryParams.clientCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.clientName" placeholder="请输入名称" clearable />
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
          <i class="el-icon-user"></i>
          客户管理列表
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
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="clientCode" label="编码" width="120" show-overflow-tooltip />
        <el-table-column prop="clientName" label="名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="contactPerson" label="联系人" width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.contactPerson">{{ scope.row.contactPerson }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="120" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.phone">{{ scope.row.phone }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.email">{{ scope.row.email }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :modal="false" custom-class="no-mask-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="客户编码" prop="clientCode">
          <el-input v-model="form.clientCode" placeholder="请输入客户编码" />
        </el-form-item>
        <el-form-item label="客户名称" prop="clientName">
          <el-input v-model="form.clientName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" type="textarea" :rows="2" placeholder="请输入地址" />
        </el-form-item>
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
import { listClient, addClient, updateClient, delClient, delClientBatch } from '@/api/md'

/**
 * 客户管理 - carels
 * @author carels
 * @version V9.1
 * @date 2026-03-17
 */
export default {
  name: 'MdClient',
  data() {
    return {
      loading: false,
      total: 0,
      stats: {
        total: 0,
        active: 0,
        inactive: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        clientCode: '',
        clientName: ''
      },
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        clientId: null,
        clientCode: '',
        clientName: '',
        contactPerson: '',
        phone: '',
        email: '',
        address: '',
        remark: '',
        status: '0'
      },
      rules: {
        clientCode: [{ required: true, message: '请输入客户编码', trigger: 'blur' }],
        clientName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }]
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
        const res = await listClient(this.queryParams)
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
      this.stats.active = this.tableData.filter(item => item.status === '0').length
      this.stats.inactive = this.tableData.filter(item => item.status === '1').length
    },
    
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        clientCode: '',
        clientName: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.dialogTitle = '新增客户'
      this.form = {
        clientId: null,
        clientCode: '',
        clientName: '',
        contactPerson: '',
        phone: '',
        email: '',
        address: '',
        remark: '',
        status: '0'
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑客户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleView(row) {
      this.$alert(`编码：${row.clientCode}<br>名称：${row.clientName}<br>联系人：${row.contactPerson || '-'}<br>电话：${row.phone || '-'}`, '客户详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    
    handleRowDblclick(row) {
      this.handleView(row)
    },
    
    handleDelete(row) {
      this.$confirm(`确定删除客户 "${row.clientName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await delClient(row.clientId)
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    
    async handleStatusChange(row) {
      const statusText = row.status === '0' ? '启用' : '停用'
      try {
        await updateClient({ clientId: row.clientId, status: row.status })
        this.$message.success(`已${statusText}`)
      } catch (error) {
        this.$message.error('状态更新失败')
        row.status = row.status === '0' ? '1' : '0'
      }
    },
    
    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.form.clientId) {
              await updateClient(this.form)
              this.$message.success('更新成功')
            } else {
              await addClient(this.form)
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

// 统计信息栏
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
      
      &.blue {
        color: #409EFF;
      }
      
      &.green {
        color: #67c23a;
      }
      
      &.orange {
        color: #e6a23c;
      }
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
    
    .header-title {
      font-weight: bold;
      
      i {
        margin-right: 5px;
      }
    }
  }
  
  .el-table {
    margin-bottom: 15px;
    
    ::v-deep .el-table__row {
      height: 40px;
    }
    
    ::v-deep .el-table__cell {
      padding: 4px 0;
    }
    
    ::v-deep th.el-table__cell {
      padding: 8px 0;
    }
  }
  
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;
  }
}

// 无遮罩弹框样式
.no-mask-dialog {
  .el-dialog {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3) !important;
  }
  
  &.el-dialog__wrapper {
    background: transparent !important;
  }
}
</style>
