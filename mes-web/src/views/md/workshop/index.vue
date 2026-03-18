<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header md">
      <div class="title-section">
        <i class="el-icon-office-building"></i>
        <span class="title">车间管理</span>
        <span class="subtitle">Workshop Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="success" icon="el-icon-download">导出</el-button>
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
          <el-input v-model="queryParams.workshopCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.workshopName" placeholder="请输入名称" clearable />
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
          <i class="el-icon-office-building"></i>
          车间管理列表
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
      
      <!-- 数据状态提示 -->
      <div v-if="!loading" style="padding: 10px; color: #606266; font-size: 14px;">
        <span v-if="tableData.length > 0">共 {{ tableData.length }} 条数据</span>
        <span v-else style="color: #f56c6c;">暂无数据，请检查网络或刷新页面</span>
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
        <el-table-column prop="workshopCode" label="编码" width="120" show-overflow-tooltip />
        <el-table-column prop="workshopName" label="名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="managerName" label="负责人" width="100" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.managerName">{{ scope.row.managerName }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.remark">{{ scope.row.remark }}</span>
            <span v-else style="color: #909399;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="orgMode" label="模式" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orgMode === 'SIMPLE'" type="success" size="mini">简单模式</el-tag>
            <el-tag v-else-if="scope.row.orgMode === 'COMPLETE'" type="warning" size="mini">完整模式</el-tag>
            <span v-else>-</span>
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
        
        <template slot="empty">
          <div style="padding: 30px; text-align: center; color: #909399;">
            <i class="el-icon-s-grid" style="font-size: 48px; margin-bottom: 10px; display: block;"></i>
            <span>暂无数据</span>
          </div>
        </template>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :modal="false" custom-class="no-mask-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="车间编码" prop="workshopCode">
          <el-input v-model="form.workshopCode" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="车间名称" prop="workshopName">
          <el-input v-model="form.workshopName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="负责人" prop="managerName">
          <el-input v-model="form.managerName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="组织模式" prop="orgMode">
          <el-radio-group v-model="form.orgMode">
            <el-radio-button label="SIMPLE">
              <i class="el-icon-s-grid"></i> 简单模式
            </el-radio-button>
            <el-radio-button label="COMPLETE">
              <i class="el-icon-s-operation"></i> 完整模式
            </el-radio-button>
          </el-radio-group>
          <div class="mode-tip">
            <span v-if="form.orgMode === 'SIMPLE'" class="tip-text simple">
              <i class="el-icon-info"></i> 简单模式：车间直接管理工作站
            </span>
            <span v-else class="tip-text complete">
              <i class="el-icon-info"></i> 完整模式：车间 → 生产线 → 工作站
            </span>
          </div>
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

<script>
import { listWorkshop, addWorkshop, updateWorkshop, delWorkshop, delWorkshopBatch } from '@/api/md'

/**
 * 车间管理 - carels
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
export default {
  name: 'MdWorkshop',
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
        workshopCode: '',
        workshopName: ''
      },
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增',
      form: {
        workshopId: null,
        workshopCode: '',
        workshopName: '',
        managerName: '',
        orgMode: 'SIMPLE',
        status: '0',
        remark: ''
      },
      rules: {
        workshopCode: [{ required: true, message: '请输入编码', trigger: 'blur' }],
        workshopName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        orgMode: [{ required: true, message: '请选择组织模式', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      console.log('开始加载车间数据...')
      try {
        const res = await listWorkshop(this.queryParams)
        console.log('API返回数据:', res)
        this.tableData = res.rows || []
        this.total = res.total || 0
        console.log('表格数据:', this.tableData)
        console.log('总条数:', this.total)
        this.updateStatistics()
      } catch (error) {
        console.error('获取数据失败:', error)
        this.$message.error('获取数据失败: ' + (error.message || '未知错误'))
      } finally {
        this.loading = false
        console.log('加载完成')
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
        workshopCode: '',
        workshopName: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.dialogTitle = '新增车间'
      this.form = {
        workshopId: null,
        workshopCode: '',
        workshopName: '',
        orgMode: 'SIMPLE',
        status: '0',
        remark: ''
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑车间'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleView(row) {
      this.$alert(`编码：${row.workshopCode}<br>名称：${row.workshopName}`, '详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    
    handleRowDblclick(row) {
      this.handleView(row)
    },
    
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.workshopName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await delWorkshop(row.workshopId)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error(error.message || '删除失败')
        }
      })
    },
    
    async handleStatusChange(row) {
      const status = row.status === '0' ? '启用' : '停用'
      try {
        await updateWorkshop(row)
        this.$message.success(`已${status}：${row.workshopName}`)
      } catch (error) {
        this.$message.error('状态更新失败')
        // 恢复原状态
        row.status = row.status === '0' ? '1' : '0'
      }
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
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.form.workshopId) {
              await updateWorkshop(this.form)
              this.$message.success('修改成功')
            } else {
              await addWorkshop(this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(error.message || '保存失败')
          }
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

// 统计信息栏 - 单行展示
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
    
    // 扁平行高
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
}

// 组织模式提示样式
.mode-tip {
  margin-top: 8px;
  
  .tip-text {
    font-size: 12px;
    padding: 5px 10px;
    border-radius: 4px;
    display: inline-block;
    
    i {
      margin-right: 4px;
    }
    
    &.simple {
      color: #67c23a;
      background-color: #f0f9eb;
    }
    
    &.complete {
      color: #e6a23c;
      background-color: #fdf6ec;
    }
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
  
  .stats-bar {
    flex-wrap: wrap;
    gap: 10px;
    
    .stat-divider {
      display: none;
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
}
</style>
