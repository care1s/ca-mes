<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-upload2"></i>
        <span class="title">出库管理</span>
        <span class="subtitle">Issue Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="success" icon="el-icon-download">导出</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="出库单号">
          <el-input v-model="queryParams.issueNo" placeholder="请输入出库单号" clearable />
        </el-form-item>
        <el-form-item label="出库类型">
          <el-select v-model="queryParams.issueType" placeholder="请选择出库类型" clearable>
            <el-option label="销售出库" value="SALE" />
            <el-option label="生产出库" value="PRODUCTION" />
            <el-option label="退货出库" value="RETURN" />
            <el-option label="其他出库" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="待处理" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon blue">
            <i class="el-icon-s-grid"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orange">
            <i class="el-icon-time"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pending }}</div>
            <div class="stat-label">待处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon green">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.confirmed }}</div>
            <div class="stat-label">已确认</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon purple">
            <i class="el-icon-success"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.completed }}</div>
            <div class="stat-label">已完成</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never" v-loading="loading">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-upload2"></i>
          出库单列表
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
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="issueNo" label="出库单号" width="150" show-overflow-tooltip />
        <el-table-column prop="issueType" label="出库类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.issueType === 'SALE'" type="success">销售出库</el-tag>
            <el-tag v-else-if="scope.row.issueType === 'PRODUCTION'" type="primary">生产出库</el-tag>
            <el-tag v-else-if="scope.row.issueType === 'RETURN'" type="warning">退货出库</el-tag>
            <el-tag v-else type="info">{{ scope.row.issueType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="targetNo" label="目标单号" width="150" show-overflow-tooltip />
        <el-table-column prop="warehouseName" label="仓库" min-width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'PENDING'" type="warning">待处理</el-tag>
            <el-tag v-else-if="scope.row.status === 'CONFIRMED'" type="primary">已确认</el-tag>
            <el-tag v-else-if="scope.row.status === 'COMPLETED'" type="success">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 'CANCELLED'" type="danger">已取消</el-tag>
            <el-tag v-else type="info">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="出库日期" width="160" align="center" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button 
              v-if="scope.row.status === 'PENDING'" 
              type="text" 
              icon="el-icon-check" 
              style="color: #67c23a"
              @click="handleConfirm(scope.row)"
            >确认</el-button>
            <el-button 
              type="text" 
              icon="el-icon-delete" 
              style="color: #f56c6c" 
              @click="handleDelete(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="800px" :append-to-body="true">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出库单号" prop="issueNo">
              <el-input v-model="form.issueNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库类型" prop="issueType">
              <el-select v-model="form.issueType" placeholder="请选择出库类型" style="width: 100%">
                <el-option label="销售出库" value="SALE" />
                <el-option label="生产出库" value="PRODUCTION" />
                <el-option label="退货出库" value="RETURN" />
                <el-option label="其他出库" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目标类型" prop="targetType">
              <el-select v-model="form.targetType" placeholder="请选择目标类型" style="width: 100%">
                <el-option label="销售订单" value="SALE_ORDER" />
                <el-option label="生产工单" value="WORK_ORDER" />
                <el-option label="无" value="NONE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目标单号" prop="targetNo">
              <el-input v-model="form.targetNo" placeholder="请输入目标单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="出库仓库" prop="warehouseId">
              <el-input v-model="form.warehouseName" placeholder="请选择仓库" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出库日期" prop="issueDate">
              <el-date-picker
                v-model="form.issueDate"
                type="datetime"
                placeholder="选择出库日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
        
        <!-- 明细表格 -->
        <el-divider content-position="left">出库明细</el-divider>
        <el-table :data="form.items" border size="small" style="margin-bottom: 10px">
          <el-table-column type="index" label="序号" width="50" align="center" />
          <el-table-column label="物料编码" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemCode" placeholder="物料编码" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="物料名称" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" placeholder="物料名称" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="批次" width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.batchCode" placeholder="批次" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="100">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="0" :precision="2" size="small" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template slot-scope="scope">
              <el-input v-model="scope.row.unit" placeholder="单位" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="备注" min-width="100">
            <template slot-scope="scope">
              <el-input v-model="scope.row.remark" placeholder="备注" size="small" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" style="color: #f56c6c" @click="removeItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-button type="primary" icon="el-icon-plus" size="small" @click="addItem">添加明细</el-button>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="出库单详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="出库单号">{{ viewData.issueNo }}</el-descriptions-item>
        <el-descriptions-item label="出库类型">
          <el-tag v-if="viewData.issueType === 'SALE'" type="success">销售出库</el-tag>
          <el-tag v-else-if="viewData.issueType === 'PRODUCTION'" type="primary">生产出库</el-tag>
          <el-tag v-else-if="viewData.issueType === 'RETURN'" type="warning">退货出库</el-tag>
          <el-tag v-else type="info">{{ viewData.issueType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标单号">{{ viewData.targetNo }}</el-descriptions-item>
        <el-descriptions-item label="出库仓库">{{ viewData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewData.status === 'PENDING'" type="warning">待处理</el-tag>
          <el-tag v-else-if="viewData.status === 'CONFIRMED'" type="primary">已确认</el-tag>
          <el-tag v-else-if="viewData.status === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else-if="viewData.status === 'CANCELLED'" type="danger">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="出库日期">{{ viewData.issueDate }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 20px 0 10px 0;">出库明细</h4>
      <el-table :data="viewData.items" border size="small">
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="itemCode" label="物料编码" width="120" />
        <el-table-column prop="itemName" label="物料名称" width="150" />
        <el-table-column prop="batchCode" label="批次" width="100" />
        <el-table-column prop="quantity" label="数量" width="100" align="right" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="remark" label="备注" />
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWmIssue, getWmIssue, addWmIssue, updateWmIssue, delWmIssue, confirmWmIssue } from '@/api/md'

/**
 * 出库管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'WmIssue',
  data() {
    return {
      loading: false,
      submitLoading: false,
      total: 0,
      stats: {
        total: 0,
        pending: 0,
        confirmed: 0,
        completed: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        issueNo: '',
        issueType: '',
        warehouseName: '',
        status: ''
      },
      tableData: [],
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '新增',
      form: {
        id: null,
        issueNo: '',
        issueType: 'SALE',
        targetType: 'SALE_ORDER',
        targetId: null,
        targetNo: '',
        warehouseId: null,
        warehouseName: '',
        status: 'PENDING',
        issueDate: null,
        remark: '',
        items: []
      },
      viewData: {
        issueNo: '',
        issueType: '',
        targetNo: '',
        warehouseName: '',
        status: '',
        issueDate: '',
        remark: '',
        items: []
      },
      rules: {
        issueType: [{ required: true, message: '请选择出库类型', trigger: 'change' }],
        warehouseName: [{ required: true, message: '请输入出库仓库', trigger: 'blur' }]
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
        const res = await listWmIssue(this.queryParams)
        if (res.code === 200) {
          this.tableData = res.rows || []
          this.total = res.total || 0
          this.calculateStats()
        } else {
          this.$message.error(res.msg || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.pending = this.tableData.filter(item => item.status === 'PENDING').length
      this.stats.confirmed = this.tableData.filter(item => item.status === 'CONFIRMED').length
      this.stats.completed = this.tableData.filter(item => item.status === 'COMPLETED').length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        issueNo: '',
        issueType: '',
        warehouseName: '',
        status: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增出库单'
      this.form = {
        id: null,
        issueNo: '',
        issueType: 'SALE',
        targetType: 'SALE_ORDER',
        targetId: null,
        targetNo: '',
        warehouseId: null,
        warehouseName: '',
        status: 'PENDING',
        issueDate: new Date(),
        remark: '',
        items: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑出库单'
      try {
        const res = await getWmIssue(row.id)
        if (res.code === 200) {
          this.form = { ...res.data }
          if (!this.form.items) {
            this.form.items = []
          }
          this.dialogVisible = true
        } else {
          this.$message.error(res.msg || '获取详情失败')
        }
      } catch (error) {
        this.$message.error('获取详情失败: ' + error.message)
      }
    },
    handleView(row) {
      this.viewData = { ...row }
      this.viewDialogVisible = true
    },
    async handleConfirm(row) {
      try {
        await this.$confirm('确认该出库单吗？', '提示', { type: 'warning' })
        const res = await confirmWmIssue(row.id)
        if (res.code === 200) {
          this.$message.success('出库确认成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '确认失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('确认失败: ' + error.message)
        }
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确认删除出库单 "${row.issueNo}" 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await delWmIssue(row.id)
        if (res.code === 200) {
          this.$message.success('删除成功')
          this.fetchData()
        } else {
          this.$message.error(res.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },
    addItem() {
      this.form.items.push({
        itemId: null,
        itemId2: null,
        itemCode: '',
        itemName: '',
        batchCode: '',
        quantity: 0,
        unit: '',
        remark: ''
      })
    },
    removeItem(index) {
      this.form.items.splice(index, 1)
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
          this.submitLoading = true
          try {
            const api = this.form.id ? updateWmIssue : addWmIssue
            const res = await api(this.form)
            if (res.code === 200) {
              this.$message.success(this.form.id ? '修改成功' : '新增成功')
              this.dialogVisible = false
              this.fetchData()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          } catch (error) {
            this.$message.error('操作失败: ' + error.message)
          } finally {
            this.submitLoading = false
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

// 统计卡片
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }
  
  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 15px;
    
    i {
      font-size: 28px;
      color: #fff;
    }
    
    &.blue {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    &.green {
      background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
    }
    
    &.orange {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    
    &.purple {
      background: linear-gradient(135deg, #a855f7 0%, #6366f1 100%);
    }
  }
  
  .stat-info {
    flex: 1;
    
    .stat-value {
      font-size: 28px;
      font-weight: 700;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
    }
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
  
  .stat-row {
    .el-col {
      margin-bottom: 15px;
    }
  }
}
</style>
