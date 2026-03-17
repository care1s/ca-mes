<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-operation"></i>
        <span class="title">生产线管理</span>
        <span class="subtitle">Production Line Management</span>
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
        <el-form-item label="所属车间">
          <el-select v-model="queryParams.workshopId" placeholder="请选择车间" clearable style="width: 200px">
            <el-option
              v-for="item in workshopList"
              :key="item.workshopId"
              :label="item.workshopName"
              :value="item.workshopId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="queryParams.lineCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="queryParams.lineName" placeholder="请输入名称" clearable />
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
          生产线列表
        </span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @row-dblclick="handleRowDblclick"
      >
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="lineCode" label="编码" width="120" show-overflow-tooltip />
        <el-table-column prop="lineName" label="名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="workshopName" label="所属车间" width="130" show-overflow-tooltip />
        <el-table-column prop="lineType" label="类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.lineType === 'ASSEMBLY'" type="success" size="mini">装配线</el-tag>
            <el-tag v-else-if="scope.row.lineType === 'PROCESSING'" type="warning" size="mini">加工线</el-tag>
            <el-tag v-else-if="scope.row.lineType === 'PACKING'" type="info" size="mini">包装线</el-tag>
            <span v-else>{{ scope.row.lineType || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="capacity" label="产能" width="100" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.capacity ? scope.row.capacity + '/h' : '-' }}</span>
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
        <el-form-item label="所属车间" prop="workshopId">
          <el-select v-model="form.workshopId" placeholder="请选择车间" style="width: 100%" @change="handleWorkshopChange">
            <el-option
              v-for="item in completeModeWorkshops"
              :key="item.workshopId"
              :label="item.workshopName"
              :value="item.workshopId"
            />
          </el-select>
          <span v-if="completeModeWorkshops.length === 0" style="color: #f56c6c; font-size: 12px;">
            暂无可用的完整模式车间，请先在车间管理中设置组织模式为"完整模式"
          </span>
        </el-form-item>
        <el-form-item label="生产线编码" prop="lineCode">
          <el-input v-model="form.lineCode" placeholder="请输入生产线编码" />
        </el-form-item>
        <el-form-item label="生产线名称" prop="lineName">
          <el-input v-model="form.lineName" placeholder="请输入生产线名称" />
        </el-form-item>
        <el-form-item label="生产线类型" prop="lineType">
          <el-select v-model="form.lineType" placeholder="请选择类型" style="width: 100%">
            <el-option label="装配线" value="ASSEMBLY" />
            <el-option label="加工线" value="PROCESSING" />
            <el-option label="包装线" value="PACKING" />
            <el-option label="检测线" value="INSPECTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="产能(件/小时)" prop="capacity">
          <el-input-number v-model="form.capacity" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :disabled="completeModeWorkshops.length === 0">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProductionLine, addProductionLine, updateProductionLine, delProductionLine, delProductionLineBatch, getWorkshopOptions } from '@/api/md'

/**
 * 生产线管理 - carels
 * @author carels
 * @version V9.1
 * @date 2026-03-16
 * @description 支持车间-生产线-工作站三级架构
 */
export default {
  name: 'MdProductionLine',
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
        lineCode: '',
        lineName: ''
      },
      tableData: [],
      workshopList: [],
      dialogVisible: false,
      dialogTitle: '',
      form: {
        lineId: null,
        workshopId: '',
        workshopName: '',
        lineCode: '',
        lineName: '',
        lineType: '',
        capacity: 0,
        status: '0',
        remark: ''
      },
      rules: {
        workshopId: [{ required: true, message: '请选择所属车间', trigger: 'change' }],
        lineCode: [{ required: true, message: '请输入生产线编码', trigger: 'blur' }],
        lineName: [{ required: true, message: '请输入生产线名称', trigger: 'blur' }],
        lineType: [{ required: true, message: '请选择生产线类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    // 只显示完整模式的车间
    completeModeWorkshops() {
      console.log('过滤前车间列表:', this.workshopList)
      const filtered = this.workshopList.filter(ws => {
        console.log('检查车间:', ws.workshopName, 'orgMode:', ws.orgMode)
        return ws.orgMode === 'COMPLETE'
      })
      console.log('过滤后完整模式车间:', filtered)
      return filtered
    }
  },
  mounted() {
    this.fetchData()
    this.fetchWorkshopList()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listProductionLine(this.queryParams)
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
    
    async fetchWorkshopList() {
      try {
        const res = await getWorkshopOptions()
        console.log('车间列表原始返回:', res)
        this.workshopList = res.data || []
        console.log('车间列表数据:', this.workshopList)
        console.log('完整模式车间:', this.completeModeWorkshops)
      } catch (error) {
        console.error('获取车间列表失败:', error)
      }
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
        lineCode: '',
        lineName: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.dialogTitle = '新增生产线'
      this.form = {
        lineId: null,
        workshopId: '',
        workshopName: '',
        lineCode: '',
        lineName: '',
        lineType: '',
        capacity: 0,
        status: '0',
        remark: ''
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.dialogTitle = '编辑生产线'
      this.form = { ...row }
      this.dialogVisible = true
    },
    
    handleView(row) {
      this.$alert(`编码：${row.lineCode}<br>名称：${row.lineName}<br>所属车间：${row.workshopName || '-'}<br>类型：${row.lineType || '-'}`, '生产线详情', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定'
      })
    },
    
    handleRowDblclick(row) {
      this.handleView(row)
    },
    
    handleWorkshopChange(val) {
      const workshop = this.workshopList.find(ws => ws.workshopId === val)
      if (workshop) {
        this.form.workshopName = workshop.workshopName
      }
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.form.lineId) {
              await updateProductionLine(this.form)
              this.$message.success('修改成功')
            } else {
              await addProductionLine(this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(error.message || '保存失败')
          }
        }
      })
    },
    
    handleDelete(row) {
      this.$confirm(`确定删除生产线 "${row.lineName}" 吗？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await delProductionLine(row.lineId)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error(error.message || '删除失败')
        }
      })
    },
    
    async handleStatusChange(row) {
      const statusText = row.status === '0' ? '启用' : '停用'
      try {
        await updateProductionLine({ lineId: row.lineId, status: row.status })
        this.$message.success(`已${statusText}`)
      } catch (error) {
        this.$message.error('状态更新失败')
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

.search-card {
  margin-bottom: 20px;
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
