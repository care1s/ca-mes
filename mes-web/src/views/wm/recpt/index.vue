<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-download"></i>
        <span class="title">入库管理</span>
        <span class="subtitle">Receipt Management</span>
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
        <el-form-item label="入库单号">
          <el-input v-model="queryParams.recptNo" placeholder="请输入入库单号" clearable />
        </el-form-item>
        <el-form-item label="入库类型">
          <el-select v-model="queryParams.recptType" placeholder="请选择入库类型" clearable>
            <el-option label="采购入库" value="PURCHASE" />
            <el-option label="生产入库" value="PRODUCTION" />
            <el-option label="退货入库" value="RETURN" />
            <el-option label="其他入库" value="OTHER" />
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

    <!-- 统计信息 - 单行展示 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待处理</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已确认</span>
        <span class="stat-value green">{{ stats.confirmed }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never" v-loading="loading">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-download"></i>
          入库单列表
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
        <el-table-column prop="recptNo" label="入库单号" width="150" show-overflow-tooltip />
        <el-table-column prop="recptType" label="入库类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.recptType === 'PURCHASE'" type="primary">采购入库</el-tag>
            <el-tag v-else-if="scope.row.recptType === 'PRODUCTION'" type="success">生产入库</el-tag>
            <el-tag v-else-if="scope.row.recptType === 'RETURN'" type="warning">退货入库</el-tag>
            <el-tag v-else type="info">{{ scope.row.recptType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sourceNo" label="来源单号" width="150" show-overflow-tooltip />
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
        <el-table-column prop="recptDate" label="入库日期" width="160" align="center" />
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
            <el-form-item label="入库单号" prop="recptNo">
              <el-input v-model="form.recptNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库类型" prop="recptType">
              <el-select v-model="form.recptType" placeholder="请选择入库类型" style="width: 100%">
                <el-option label="采购入库" value="PURCHASE" />
                <el-option label="生产入库" value="PRODUCTION" />
                <el-option label="退货入库" value="RETURN" />
                <el-option label="其他入库" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="来源类型" prop="sourceType">
              <el-select v-model="form.sourceType" placeholder="请选择来源类型" style="width: 100%">
                <el-option label="采购订单" value="PUR_ORDER" />
                <el-option label="生产工单" value="WORK_ORDER" />
                <el-option label="无" value="NONE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="来源单号" prop="sourceNo">
              <el-input v-model="form.sourceNo" placeholder="请输入来源单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
             <el-form-item label="入库仓库" prop="warehouseId">
               <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%" @change="handleWarehouseChange">
                 <el-option
                   v-for="item in warehouseOptions"
                   :key="item.warehouseId"
                   :label="item.warehouseName"
                   :value="item.warehouseId"
                 />
               </el-select>
             </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入库日期" prop="recptDate">
              <el-date-picker
                v-model="form.recptDate"
                type="datetime"
                placeholder="选择入库日期"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
        
        <!-- 明细表格 -->
        <el-divider content-position="left">入库明细</el-divider>
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
    <el-dialog title="入库单详情" :visible.sync="viewDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="入库单号">{{ viewData.recptNo }}</el-descriptions-item>
        <el-descriptions-item label="入库类型">
          <el-tag v-if="viewData.recptType === 'PURCHASE'" type="primary">采购入库</el-tag>
          <el-tag v-else-if="viewData.recptType === 'PRODUCTION'" type="success">生产入库</el-tag>
          <el-tag v-else-if="viewData.recptType === 'RETURN'" type="warning">退货入库</el-tag>
          <el-tag v-else type="info">{{ viewData.recptType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="来源单号">{{ viewData.sourceNo }}</el-descriptions-item>
        <el-descriptions-item label="入库仓库">{{ viewData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewData.status === 'PENDING'" type="warning">待处理</el-tag>
          <el-tag v-else-if="viewData.status === 'CONFIRMED'" type="primary">已确认</el-tag>
          <el-tag v-else-if="viewData.status === 'COMPLETED'" type="success">已完成</el-tag>
          <el-tag v-else-if="viewData.status === 'CANCELLED'" type="danger">已取消</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="入库日期">{{ viewData.recptDate }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark }}</el-descriptions-item>
      </el-descriptions>
      <h4 style="margin: 20px 0 10px 0;">入库明细</h4>
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
import { listWmRecpt, getWmRecpt, addWmRecpt, updateWmRecpt, delWmRecpt, confirmWmRecpt, listWmWarehouse } from '@/api/md'

/**
 * 入库管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'WmRecpt',
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
        recptNo: '',
        recptType: '',
        warehouseName: '',
        status: ''
      },
      tableData: [],
      warehouseOptions: [],
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '新增',
      form: {
        id: null,
        recptNo: '',
        recptType: 'PURCHASE',
        sourceType: 'PUR_ORDER',
        sourceId: null,
        sourceNo: '',
        warehouseId: null,
        warehouseName: '',
        status: 'PENDING',
        recptDate: null,
        remark: '',
        items: []
      },
      viewData: {
        recptNo: '',
        recptType: '',
        sourceNo: '',
        warehouseName: '',
        status: '',
        recptDate: '',
        remark: '',
        items: []
      },
      rules: {
        recptType: [{ required: true, message: '请选择入库类型', trigger: 'change' }],
        warehouseId: [{ required: true, message: '请选择入库仓库', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
    this.fetchWarehouseOptions()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listWmRecpt(this.queryParams)
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
    async fetchWarehouseOptions() {
      try {
        const res = await listWmWarehouse({ pageNum: 1, pageSize: 1000 })
        if (res.code === 200) {
          this.warehouseOptions = res.rows || []
        }
      } catch (error) {
        console.error('获取仓库列表失败:', error)
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
        recptNo: '',
        recptType: '',
        warehouseName: '',
        status: ''
      }
      this.fetchData()
    },
    handleWarehouseChange(val) {
      const warehouse = this.warehouseOptions.find(item => item.warehouseId === val)
      if (warehouse) {
        this.form.warehouseName = warehouse.warehouseName
      }
    },
    handleAdd() {
      this.dialogTitle = '新增入库单'
      this.form = {
        id: null,
        recptNo: '',
        recptType: 'PURCHASE',
        sourceType: 'PUR_ORDER',
        sourceId: null,
        sourceNo: '',
        warehouseId: null,
        warehouseName: '',
        status: 'PENDING',
        recptDate: new Date(),
        remark: '',
        items: []
      }
      this.dialogVisible = true
    },
    async handleEdit(row) {
      this.dialogTitle = '编辑入库单'
      try {
        const res = await getWmRecpt(row.id)
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
        await this.$confirm('确认该入库单吗？', '提示', { type: 'warning' })
        const res = await confirmWmRecpt(row.id)
        if (res.code === 200) {
          this.$message.success('入库确认成功')
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
        await this.$confirm(`确认删除入库单 "${row.recptNo}" 吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await delWmRecpt(row.id)
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
            const api = this.form.id ? updateWmRecpt : addWmRecpt
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
