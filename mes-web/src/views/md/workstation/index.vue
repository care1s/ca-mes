<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-operation"></i>
        <span class="title">工作站管理</span>
        <span class="subtitle">Workstation Management</span>
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
        <el-form-item label="所属车间">
          <el-select v-model="queryParams.workshopId" placeholder="请选择车间" clearable style="width: 150px" @change="handleSearchWorkshopChange">
            <el-option
              v-for="item in workshopList"
              :key="item.workshopId"
              :label="item.workshopName"
              :value="item.workshopId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="queryParams.workstationCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.workstationName" placeholder="请输入名称" clearable />
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
          <i class="el-icon-s-operation"></i>
          工作站管理列表
        </span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @row-dblclick="handleRowDblclick"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="workstationCode" label="编码" width="120" show-overflow-tooltip />
        <el-table-column prop="workstationName" label="名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="workshopName" label="所属车间" width="130" show-overflow-tooltip />
        <el-table-column prop="productionLineName" label="所属生产线" width="130" show-overflow-tooltip>
          <template slot-scope="scope">
            <span v-if="scope.row.productionLineName">{{ scope.row.productionLineName }}</span>
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
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :modal="false" custom-class="no-mask-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="所属车间" prop="workshopId">
          <el-select v-model="form.workshopId" placeholder="请选择车间" style="width: 100%" @change="handleWorkshopChange">
            <el-option
              v-for="item in workshopList"
              :key="item.workshopId"
              :label="item.workshopName"
              :value="item.workshopId"
            />
          </el-select>
        </el-form-item>
        
        <!-- 仅当选择的车间是完整模式时显示生产线选择 -->
        <el-form-item v-if="selectedWorkshop && selectedWorkshop.orgMode === 'COMPLETE'" label="所属生产线" prop="productionLineId">
          <el-select v-model="form.productionLineId" placeholder="请选择生产线" style="width: 100%" @change="handleProductionLineChange">
            <el-option
              v-for="item in productionLineList"
              :key="item.lineId"
              :label="item.lineName"
              :value="item.lineId"
            />
          </el-select>
          <div v-if="productionLineList.length === 0 && form.workshopId" class="tip-text">
            <i class="el-icon-warning"></i> 该车间暂无生产线，请先创建生产线
          </div>
        </el-form-item>
        
        <el-form-item label="工作站编码" prop="workstationCode">
          <el-input v-model="form.workstationCode" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="工作站名称" prop="workstationName">
          <el-input v-model="form.workstationName" placeholder="请输入名称" />
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
import { 
  listWorkstation, 
  addWorkstation, 
  updateWorkstation, 
  delWorkstation, 
  delWorkstationBatch,
  getWorkshopOptions,
  getProductionLinesByWorkshop
} from '@/api/md'

/**
 * 工作站管理 - carels
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 */
export default {
  name: 'MdWorkstation',
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
        workshopId: '',
        productionLineId: '',
        workstationCode: '',
        workstationName: ''
      },
      tableData: [],
      selectedRows: [],
      workshopList: [],
      productionLineList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        workstationId: null,
        workshopId: '',
        workshopName: '',
        productionLineId: '',
        productionLineName: '',
        workstationCode: '',
        workstationName: '',
        remark: '',
        status: '0'
      },
      rules: {
        workshopId: [{ required: true, message: '请选择所属车间', trigger: 'change' }],
        workstationCode: [{ required: true, message: '请输入编码', trigger: 'blur' }],
        workstationName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        productionLineId: [{ validator: this.validateProductionLine, trigger: 'change' }]
      }
    }
  },
  computed: {
    // 获取当前选中的车间信息
    selectedWorkshop() {
      return this.workshopList.find(ws => ws.workshopId === this.form.workshopId)
    }
  },
  mounted() {
    this.fetchData()
    this.fetchWorkshopList()
  },
  methods: {
    // 自定义验证：完整模式车间必须选择生产线
    validateProductionLine(rule, value, callback) {
      if (this.selectedWorkshop && this.selectedWorkshop.orgMode === 'COMPLETE') {
        if (!value) {
          callback(new Error('完整模式车间必须选择生产线'))
        } else {
          callback()
        }
      } else {
        // 简单模式不需要验证
        callback()
      }
    },
    
    async fetchData() {
      this.loading = true
      try {
        const res = await listWorkstation(this.queryParams)
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
        workshopId: '',
        productionLineId: '',
        workstationCode: '',
        workstationName: ''
      }
      this.fetchData()
    },
    
    handleSearchWorkshopChange(val) {
      // 可以在这里添加根据车间筛选工作站的逻辑
      this.handleQuery()
    },
    
    handleAdd() {
      this.dialogTitle = '新增工作站'
      this.form = {
        workstationId: null,
        workshopId: '',
        workshopName: '',
        productionLineId: '',
        productionLineName: '',
        workstationCode: '',
        workstationName: '',
        remark: '',
        status: '0'
      }
      this.productionLineList = []
      this.dialogVisible = true
    },
    
    async handleEdit(row) {
      this.dialogTitle = '编辑工作站'
      this.form = { ...row }
      // 等待车间列表加载完成
      await this.fetchWorkshopList()
      if (row.workshopId) {
        // 获取当前车间的组织模式
        const currentWorkshop = this.workshopList.find(ws => ws.workshopId === row.workshopId)
        // 只有完整模式才加载生产线列表
        if (currentWorkshop && currentWorkshop.orgMode === 'COMPLETE') {
          await this.fetchProductionLineList(row.workshopId)
        } else {
          this.productionLineList = []
        }
      }
      this.dialogVisible = true
    },
    
    handleView(row) {
      this.$alert(`编码：${row.workstationCode}<br>名称：${row.workstationName}<br>所属车间：${row.workshopName || '-'}<br>所属生产线：${row.productionLineName || '-'}`, '工作站详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    
    handleRowDblclick(row) {
      this.handleView(row)
    },

    handleDelete(row) {
      this.$confirm(`确定删除工作站"${row.workstationName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await delWorkstation(row.workstationId)
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    
    async fetchWorkshopList() {
      try {
        const res = await getWorkshopOptions()
        this.workshopList = res.data || []
      } catch (error) {
        console.error('获取车间列表失败:', error)
      }
    },
    
    async fetchProductionLineList(workshopId) {
      try {
        const res = await getProductionLinesByWorkshop(workshopId)
        this.productionLineList = res.data || []
      } catch (error) {
        console.error('获取生产线列表失败:', error)
        this.productionLineList = []
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

    handleWorkshopChange(val) {
      if (val) {
        // 更新车间名称
        const selectedWs = this.workshopList.find(ws => ws.workshopId === val)
        if (selectedWs) {
          this.form.workshopName = selectedWs.workshopName
          // 只有完整模式才加载生产线列表，且不立即清空生产线ID（让用户选择）
          if (selectedWs.orgMode === 'COMPLETE') {
            this.fetchProductionLineList(val)
          } else {
            // 简单模式下清空生产线相关数据
            this.form.productionLineId = null
            this.form.productionLineName = ''
            this.productionLineList = []
          }
        }
      } else {
        this.form.workshopName = ''
        this.form.productionLineId = null
        this.form.productionLineName = ''
        this.productionLineList = []
      }
    },

    handleProductionLineChange(val) {
      const selectedLine = this.productionLineList.find(line => line.lineId === val)
      if (selectedLine) {
        this.form.productionLineName = selectedLine.lineName
      }
    },

    handleStatusChange(row) {
      const statusText = row.status === '0' ? '启用' : '停用'
      this.$confirm(`确定${statusText}该工作站吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await updateWorkstation({
            workstationId: row.workstationId,
            status: row.status
          })
          this.$message.success('状态更新成功')
        } catch (error) {
          this.$message.error('状态更新失败')
          row.status = row.status === '0' ? '1' : '0'
        }
      }).catch(() => {
        row.status = row.status === '0' ? '1' : '0'
      })
    },

    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            if (this.form.workstationId) {
              await updateWorkstation(this.form)
              this.$message.success('更新成功')
            } else {
              await addWorkstation(this.form)
              this.$message.success('添加成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('操作失败')
          }
        }
      })
    },

    handleDeleteBatch() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请至少选择一条记录')
        return
      }
      this.$confirm(`确定删除选中的 ${this.selectedRows.length} 条记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const ids = this.selectedRows.map(row => row.workstationId)
        await delWorkstationBatch(ids)
        this.$message.success('批量删除成功')
        this.fetchData()
      }).catch(() => {})
    },

    handleSelectionChange(val) {
      this.selectedRows = val
    }
  }
}
</script>

<style lang="scss" scoped>
.workstation-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.filter-card {
  margin-bottom: 20px;
}

.filter-row {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-item {
  display: flex;
  align-items: center;
}

.filter-item .label {
  margin-right: 8px;
  font-size: 14px;
  color: #606266;
  white-space: nowrap;
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

.content-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.table-card {
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
}

.stats-row {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.dialog-form {
  padding: 10px 20px;
}

.status-tag {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.status-active {
  background-color: #67c23a;
  color: #fff;
}

.status-inactive {
  background-color: #909399;
  color: #fff;
}
</style>
