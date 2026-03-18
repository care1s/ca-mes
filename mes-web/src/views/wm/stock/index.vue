<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-data"></i>
        <span class="title">库存查询</span>
        <span class="subtitle">Stock Inquiry</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="物料编码">
          <el-input v-model="queryParams.itemCode" placeholder="请输入物料编码" clearable />
        </el-form-item>
        <el-form-item label="物料名称">
          <el-input v-model="queryParams.itemName" placeholder="请输入物料名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon blue">
            <i class="el-icon-s-grid"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">总库存</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon green">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.available }}</div>
            <div class="stat-label">可用库存</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orange">
            <i class="el-icon-lock"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.locked }}</div>
            <div class="stat-label">锁定库存</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-s-data"></i>
          库存列表
        </span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="warehouseName" label="仓库" width="150" show-overflow-tooltip />
        <el-table-column prop="itemCode" label="物料编码" width="150" show-overflow-tooltip />
        <el-table-column prop="itemName" label="物料名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="batchCode" label="批次号" width="120" show-overflow-tooltip />
        <el-table-column prop="quantity" label="总数量" width="100" align="right" />
        <el-table-column prop="availableQty" label="可用数量" width="100" align="right">
          <template slot-scope="scope">
            <span style="color: #67C23A; font-weight: bold;">{{ scope.row.availableQty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lockedQty" label="锁定数量" width="100" align="right">
          <template slot-scope="scope">
            <span style="color: #E6A23C; font-weight: bold;">{{ scope.row.lockedQty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <el-pagination
        class="pagination"
        background
        layout="total, sizes, prev, pager, next"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.pageSize"
        :current-page="queryParams.pageNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :append-to-body="true" :modal-append-to-body="true">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择仓库" style="width: 100%">
            <el-option v-for="item in warehouseList" :key="item.warehouseId" :label="item.warehouseName" :value="item.warehouseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="物料编码" prop="itemCode">
          <el-input v-model="form.itemCode" placeholder="请输入物料编码" />
        </el-form-item>
        <el-form-item label="物料名称" prop="itemName">
          <el-input v-model="form.itemName" placeholder="请输入物料名称" />
        </el-form-item>
        <el-form-item label="批次号">
          <el-input v-model="form.batchCode" placeholder="请输入批次号" />
        </el-form-item>
        <el-form-item label="总数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="可用数量" prop="availableQty">
          <el-input-number v-model="form.availableQty" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="锁定数量">
          <el-input-number v-model="form.lockedQty" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" placeholder="请输入单位" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWmStock, addWmStock, updateWmStock, delWmStock, listWmWarehouse } from '@/api/md'

export default {
  name: 'WmStock',
  data() {
    return {
      loading: false,
      submitLoading: false,
      total: 0,
      stats: {
        total: 0,
        available: 0,
        locked: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        itemCode: '',
        itemName: ''
      },
      tableData: [],
      warehouseList: [],
      dialogVisible: false,
      dialogTitle: '新增库存',
      form: {
        id: null,
        warehouseId: null,
        warehouseName: '',
        itemCode: '',
        itemName: '',
        batchCode: '',
        quantity: 0,
        availableQty: 0,
        lockedQty: 0,
        unit: ''
      },
      rules: {
        warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }],
        itemCode: [{ required: true, message: '请输入物料编码', trigger: 'blur' }],
        itemName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }],
        quantity: [{ required: true, message: '请输入总数量', trigger: 'blur' }],
        availableQty: [{ required: true, message: '请输入可用数量', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.fetchWarehouseList()
    this.fetchData()
  },
  methods: {
    // 获取仓库列表
    fetchWarehouseList() {
      listWmWarehouse({ pageNum: 1, pageSize: 100 }).then(response => {
        this.warehouseList = response.data.rows || []
      })
    },
    // 获取数据
    fetchData() {
      this.loading = true
      listWmStock(this.queryParams).then(response => {
        this.tableData = response.data.rows || []
        this.total = response.data.total || 0
        this.updateStats(this.tableData)
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 更新统计
    updateStats(data) {
      this.stats.total = data.reduce((sum, item) => sum + parseFloat(item.quantity || 0), 0)
      this.stats.available = data.reduce((sum, item) => sum + parseFloat(item.availableQty || 0), 0)
      this.stats.locked = data.reduce((sum, item) => sum + parseFloat(item.lockedQty || 0), 0)
    },
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    // 重置
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        itemCode: '',
        itemName: ''
      }
      this.fetchData()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增库存'
      this.resetForm()
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑库存'
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.itemName}" 的库存记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delWmStock(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 分页大小变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    // 页码变化
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    // 重置表单
    resetForm() {
      this.form = {
        id: null,
        warehouseId: null,
        warehouseName: '',
        itemCode: '',
        itemName: '',
        batchCode: '',
        quantity: 0,
        availableQty: 0,
        lockedQty: 0,
        unit: ''
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          // 自动设置仓库名称
          const warehouse = this.warehouseList.find(w => w.warehouseId === this.form.warehouseId)
          if (warehouse) {
            this.form.warehouseName = warehouse.warehouseName
          }
          if (this.form.id) {
            updateWmStock(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addWmStock(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
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

.search-card {
  margin-bottom: 20px;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  
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
    
    &.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
    &.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
    &.orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
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

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-title {
      font-size: 16px;
      font-weight: 600;
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
