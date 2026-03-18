<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-office-building"></i>
        <span class="title">仓库管理</span>
        <span class="subtitle">Warehouse Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="仓库编码">
          <el-input v-model="queryParams.warehouseCode" placeholder="请输入仓库编码" clearable />
        </el-form-item>
        <el-form-item label="仓库名称">
          <el-input v-model="queryParams.warehouseName" placeholder="请输入仓库名称" clearable />
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
            <div class="stat-label">总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon green">
            <i class="el-icon-check"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.active }}</div>
            <div class="stat-label">启用</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orange">
            <i class="el-icon-close"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.inactive }}</div>
            <div class="stat-label">停用</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-office-building"></i>
          仓库列表
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
        <el-table-column prop="warehouseCode" label="仓库编码" width="150" show-overflow-tooltip />
        <el-table-column prop="warehouseName" label="仓库名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="warehouseType" label="仓库类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getWarehouseTypeStyle(scope.row.warehouseType)" size="small">
              {{ getWarehouseTypeLabel(scope.row.warehouseType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="managerName" label="负责人" width="120" show-overflow-tooltip />
        <el-table-column prop="phone" label="联系电话" width="150" show-overflow-tooltip />
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
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
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
        <el-form-item label="仓库编码" prop="warehouseCode">
          <el-input v-model="form.warehouseCode" placeholder="请输入仓库编码" />
        </el-form-item>
        <el-form-item label="仓库名称" prop="warehouseName">
          <el-input v-model="form.warehouseName" placeholder="请输入仓库名称" />
        </el-form-item>
        <el-form-item label="仓库类型" prop="warehouseType">
          <el-select v-model="form.warehouseType" placeholder="请选择仓库类型" style="width: 100%">
            <el-option label="原材料仓" value="RAW" />
            <el-option label="半成品仓" value="SEMI" />
            <el-option label="成品仓" value="PRODUCT" />
            <el-option label="备件仓" value="SPARE" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.managerName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入仓库地址" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
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
import { listWmWarehouse, addWmWarehouse, updateWmWarehouse, delWmWarehouse, updateWmWarehouseStatus } from '@/api/md'

export default {
  name: 'WmWarehouse',
  data() {
    return {
      loading: false,
      submitLoading: false,
      total: 0,
      stats: {
        total: 0,
        active: 0,
        inactive: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        warehouseCode: '',
        warehouseName: ''
      },
      tableData: [],
      dialogVisible: false,
      dialogTitle: '新增仓库',
      form: {
        warehouseId: null,
        warehouseCode: '',
        warehouseName: '',
        warehouseType: '',
        managerName: '',
        phone: '',
        address: '',
        remark: ''
      },
      rules: {
        warehouseCode: [{ required: true, message: '请输入仓库编码', trigger: 'blur' }],
        warehouseName: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
        warehouseType: [{ required: true, message: '请选择仓库类型', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    fetchData() {
      this.loading = true
      listWmWarehouse(this.queryParams).then(response => {
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
      this.stats.total = data.length
      this.stats.active = data.filter(item => item.status === '0').length
      this.stats.inactive = data.filter(item => item.status === '1').length
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
        warehouseCode: '',
        warehouseName: ''
      }
      this.fetchData()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增仓库'
      this.resetForm()
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑仓库'
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.warehouseName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delWmWarehouse(row.warehouseId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 状态变更
    handleStatusChange(row) {
      const text = row.status === '0' ? '启用' : '停用'
      this.$confirm(`确认要"${text}""${row.warehouseName}"吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateWmWarehouseStatus(row.warehouseId, row.status).then(() => {
          this.$message.success(text + '成功')
        }).catch(() => {
          row.status = row.status === '0' ? '1' : '0'
        })
      }).catch(() => {
        row.status = row.status === '0' ? '1' : '0'
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
        warehouseId: null,
        warehouseCode: '',
        warehouseName: '',
        warehouseType: '',
        managerName: '',
        phone: '',
        address: '',
        remark: ''
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.warehouseId) {
            updateWmWarehouse(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addWmWarehouse(this.form).then(() => {
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
    },
    // 获取仓库类型标签
    getWarehouseTypeLabel(type) {
      const map = { RAW: '原材料仓', SEMI: '半成品仓', PRODUCT: '成品仓', SPARE: '备件仓' }
      return map[type] || type
    },
    // 获取仓库类型样式
    getWarehouseTypeStyle(type) {
      const map = { RAW: 'primary', SEMI: 'warning', PRODUCT: 'success', SPARE: 'info' }
      return map[type] || ''
    }
  }
}
</script>
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

<script>
/**
 * 仓库管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'WmWarehouse',
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
