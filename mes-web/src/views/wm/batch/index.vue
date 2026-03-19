<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-collection-tag"></i>
        <span class="title">批次管理</span>
        <span class="subtitle">Batch Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="编码">
          <el-input v-model="queryParams.code" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

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

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-collection-tag"></i>
          批次管理列表
        </span>
        <el-pagination
          class="pagination"
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      
      <el-table
        
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="code" label="编码" width="150" show-overflow-tooltip />
        <el-table-column prop="name" label="名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :append-to-body="true" :modal-append-to-body="true">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

import XLSX from 'xlsx'

<script>
/**
 * 批次管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'WmBatch',
  data() {
    return {
      loading: false,
      total: 50,
      stats: {
        total: 50,
        active: 45,
        inactive: 5
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: '',
        name: ''
      },
      tableData: [
        {
          id: 1,
          code: 'CODE001',
          name: '示例数据1',
          status: '0',
          createTime: '2026-03-15 10:00:00'
        },
        {
          id: 2,
          code: 'CODE002',
          name: '示例数据2',
          status: '0',
          createTime: '2026-03-15 11:00:00'
        },
        {
          id: 3,
          code: 'CODE003',
          name: '示例数据3',
          status: '1',
          createTime: '2026-03-15 12:00:00'
        }
      ],
      dialogVisible: false,
      dialogTitle: '新增',
      form: {
        code: '',
        name: '',
        remark: ''
      },
      rules: {
        code: [{ required: true, message: '请输入编码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    handleExport() {
      if (this.tableData.length === 0) {
        this.$message.warning('暂无数据可导出')
        return
      }
      
      const headers = ['序号', '编码', '名称', '状态', '创建时间']
      const data = this.tableData.map((row, index) => [
        index + 1,
        row.code,
        row.name,
        row.status === '0' ? '启用' : '停用',
        row.createTime
      ])
      
      const ws = XLSX.utils.aoa_to_sheet([headers, ...data])
      const wb = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(wb, ws, '批次管理')
      
      const now = new Date()
      const filename = `批次管理_${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}.xlsx`
      
      XLSX.writeFile(wb, filename)
      this.$message.success('导出成功')
    },
    fetchData() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 500)
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        code: '',
        name: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增'
      this.form = {
        code: '',
        name: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$alert(`编码：${row.code}<br>名称：${row.name}`, '详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.name}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      })
    },
    handleStatusChange(row) {
      const status = row.status === '0' ? '启用' : '停用'
      this.$message.success(`已${status}：${row.name}`)
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success('保存成功')
          this.dialogVisible = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
}

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 2px solid #EBEEF5;

  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 28px;
      color: #409EFF;
      margin-right: 12px;
    }
    
    .title {
      font-size: 22px;
      font-weight: 600;
      color: #303133;
      margin-right: 10px;
    }
    
    .subtitle {
      font-size: 13px;
      color: #909399;
      font-weight: normal;
    }
  }
}

// 搜索栏
.search-card {
  margin-bottom: 20px;
  
  .search-form {
    .el-form-item {
      margin-bottom: 0;
      margin-right: 20px;
    }
  }
}

// 统计信息 - 单行展示
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
      &.green { color: #67c23a; }
      &.orange { color: #e6a23c; }
      &.red { color: #f56c6c; }
    }
  }
  
  .stat-divider {
    width: 1px;
    height: 20px;
    background: #ebeef5;
    margin: 0 20px;
  }
}

// 表格卡片
.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
  
  .el-table {
    margin-top: 15px;
  }
}

// 响应式调整
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    
    .action-section {
      margin-top: 10px;
    }
  }
  

}
</style>
