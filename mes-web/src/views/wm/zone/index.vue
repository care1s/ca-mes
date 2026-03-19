<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-map-location"></i>
        <span class="title">仓区管理</span>
        <span class="subtitle">Zone Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="所属仓库">
          <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable style="width: 180px">
            <el-option
              v-for="item in warehouseOptions"
              :key="item.warehouseId"
              :label="item.warehouseName"
              :value="item.warehouseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="仓区编码">
          <el-input v-model="queryParams.zoneCode" placeholder="请输入仓区编码" clearable />
        </el-form-item>
        <el-form-item label="仓区名称">
          <el-input v-model="queryParams.zoneName" placeholder="请输入仓区名称" clearable />
        </el-form-item>
        <el-form-item label="仓区类型">
          <el-select v-model="queryParams.zoneType" placeholder="请选择仓区类型" clearable style="width: 150px">
            <el-option label="收货区" value="RECEIVE" />
            <el-option label="发货区" value="SHIP" />
            <el-option label="存储区" value="STORAGE" />
            <el-option label="拣货区" value="PICK" />
            <el-option label="特殊区" value="SPECIAL" />
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
          <i class="el-icon-map-location"></i>
          仓区列表
        </span>
        <div class="header-actions">
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="warehouseName" label="所属仓库" width="150" show-overflow-tooltip />
        <el-table-column prop="zoneCode" label="仓区编码" width="150" show-overflow-tooltip />
        <el-table-column prop="zoneName" label="仓区名称" min-width="180" show-overflow-tooltip />
        <el-table-column prop="zoneType" label="仓区类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getZoneTypeStyle(scope.row.zoneType)" size="small">
              {{ getZoneTypeLabel(scope.row.zoneType) }}
            </el-tag>
          </template>
        </el-table-column>
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
         <el-table-column label="操作" width="280" align="center" fixed="right">
           <template slot-scope="scope">
             <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
             <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
             <el-tooltip content="新增仓位" placement="top">
               <el-button type="text" style="color: #409EFF; font-size: 16px" @click="handleAddLocation(scope.row)">
                 <i class="el-icon-s-home"></i>
               </el-button>
             </el-tooltip>
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
        <el-form-item label="所属仓库" prop="warehouseId">
          <el-select v-model="form.warehouseId" placeholder="请选择所属仓库" style="width: 100%">
            <el-option
              v-for="item in warehouseOptions"
              :key="item.warehouseId"
              :label="item.warehouseName"
              :value="item.warehouseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="仓区编码" prop="zoneCode">
          <el-input v-model="form.zoneCode" placeholder="留空自动生成，或手动输入">
            <el-button slot="append" icon="el-icon-magic-stick" @click="generateZoneCode">自动生成</el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="仓区名称" prop="zoneName">
          <el-input v-model="form.zoneName" placeholder="请输入仓区名称" />
        </el-form-item>
        <el-form-item label="仓区类型" prop="zoneType">
          <el-select v-model="form.zoneType" placeholder="请选择仓区类型" style="width: 100%">
            <el-option label="收货区" value="RECEIVE" />
            <el-option label="发货区" value="SHIP" />
            <el-option label="存储区" value="STORAGE" />
            <el-option label="拣货区" value="PICK" />
            <el-option label="特殊区" value="SPECIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">启用</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
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

    <!-- 查看详情对话框 -->
    <el-dialog title="仓区详情" :visible.sync="viewDialogVisible" width="500px" :append-to-body="true">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="仓区编码">{{ viewData.zoneCode }}</el-descriptions-item>
        <el-descriptions-item label="仓区名称">{{ viewData.zoneName }}</el-descriptions-item>
        <el-descriptions-item label="所属仓库">{{ viewData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="仓区类型">
          <el-tag :type="getZoneTypeStyle(viewData.zoneType)" size="small">
            {{ getZoneTypeLabel(viewData.zoneType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="viewData.status === '0' ? 'success' : 'info'" size="small">
            {{ viewData.status === '0' ? '启用' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ viewData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listWmZone,
  getWmZone,
  addWmZone,
  updateWmZone,
  delWmZone,
  delWmZoneBatch,
  listWmWarehouse
} from '@/api/md'

export default {
  name: 'WmZone',
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
        warehouseId: '',
        zoneCode: '',
        zoneName: '',
        zoneType: ''
      },
      tableData: [],
      selectedRows: [],
      warehouseOptions: [],
      dialogVisible: false,
      dialogTitle: '新增仓区',
      viewDialogVisible: false,
      viewData: {},
      form: {
        zoneId: null,
        warehouseId: '',
        zoneCode: '',
        zoneName: '',
        zoneType: '',
        status: '0',
        remark: ''
      },
      rules: {
        warehouseId: [{ required: true, message: '请选择所属仓库', trigger: 'change' }],
        zoneCode: [{ required: true, message: '请输入仓区编码', trigger: 'blur' }],
        zoneName: [{ required: true, message: '请输入仓区名称', trigger: 'blur' }],
        zoneType: [{ required: true, message: '请选择仓区类型', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchWarehouseOptions()
    // 检查是否从仓库页面跳转过来
    const { warehouseId, warehouseName } = this.$route.query
    if (warehouseId) {
      this.queryParams.warehouseId = parseInt(warehouseId)
      this.$message.info(`已筛选仓库：${warehouseName || ''}`)
    }
    this.fetchData()
  },
  methods: {
    // 获取仓库选项
    fetchWarehouseOptions() {
      listWmWarehouse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.warehouseOptions = response.rows || []
      })
    },
    // 获取数据
    fetchData() {
      this.loading = true
      listWmZone(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
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
        warehouseId: '',
        zoneCode: '',
        zoneName: '',
        zoneType: ''
      }
      this.fetchData()
    },
    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增仓区'
      this.resetForm()
      // 如果路由中有仓库ID，自动填充
      const { warehouseId } = this.$route.query
      if (warehouseId) {
        this.form.warehouseId = parseInt(warehouseId)
      }
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑仓区'
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 查看
    handleView(row) {
      getWmZone(row.zoneId).then(response => {
        this.viewData = response.data
        this.viewDialogVisible = true
      })
    },
    // 新增仓位
    handleAddLocation(row) {
      this.$router.push({
        path: '/wm/location',
        query: {
          warehouseId: row.warehouseId,
          warehouseName: row.warehouseName,
          zoneId: row.zoneId,
          zoneName: row.zoneName
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.zoneName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delWmZone(row.zoneId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 批量删除
    handleBatchDelete() {
      const zoneIds = this.selectedRows.map(row => row.zoneId)
      const zoneNames = this.selectedRows.map(row => row.zoneName).join(', ')
      this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个仓区吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delWmZoneBatch(zoneIds).then(() => {
          this.$message.success('批量删除成功')
          this.selectedRows = []
          this.fetchData()
        })
      })
    },
    // 状态变更
    handleStatusChange(row) {
      const text = row.status === '0' ? '启用' : '停用'
      this.$confirm(`确认要"${text}""${row.zoneName}"吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateWmZone(row).then(() => {
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
        zoneId: null,
        warehouseId: '',
        zoneCode: '',
        zoneName: '',
        zoneType: '',
        status: '0',
        remark: ''
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.zoneId) {
            updateWmZone(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addWmZone(this.form).then(() => {
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
    // 生成仓区编码
    generateZoneCode() {
      const prefix = 'ZONE'
      const timestamp = Date.now().toString().slice(-6)
      const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      this.form.zoneCode = `${prefix}${timestamp}${random}`
    },
    // 获取仓区类型标签
    getZoneTypeLabel(type) {
      const map = {
        RECEIVE: '收货区',
        SHIP: '发货区',
        STORAGE: '存储区',
        PICK: '拣货区',
        SPECIAL: '特殊区'
      }
      return map[type] || type
    },
    // 获取仓区类型样式
    getZoneTypeStyle(type) {
      const map = {
        RECEIVE: 'success',
        SHIP: 'warning',
        STORAGE: 'primary',
        PICK: 'info',
        SPECIAL: 'danger'
      }
      return map[type] || ''
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

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .el-table {
    margin-top: 15px;
  }
}

// 分页
.pagination {
  margin-top: 20px;
  text-align: right;
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
