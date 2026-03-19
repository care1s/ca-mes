<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-grid"></i>
        <span class="title">仓位管理</span>
        <span class="subtitle">Location Management</span>
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
          <el-select v-model="queryParams.warehouseId" placeholder="请选择仓库" clearable style="width: 180px" @change="handleWarehouseChange">
            <el-option
              v-for="item in warehouseOptions"
              :key="item.warehouseId"
              :label="item.warehouseName"
              :value="item.warehouseId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属仓区">
          <el-select v-model="queryParams.zoneId" placeholder="请先选择仓库" clearable style="width: 180px" :disabled="!queryParams.warehouseId">
            <el-option
              v-for="item in zoneOptionsFiltered"
              :key="item.zoneId"
              :label="item.zoneName"
              :value="item.zoneId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="仓位编码">
          <el-input v-model="queryParams.locationCode" placeholder="请输入仓位编码" clearable />
        </el-form-item>
        <el-form-item label="仓位名称">
          <el-input v-model="queryParams.locationName" placeholder="请输入仓位名称" clearable />
        </el-form-item>
        <el-form-item label="巷道号">
          <el-input v-model="queryParams.aisleNo" placeholder="请输入巷道号" clearable />
        </el-form-item>
        <el-form-item label="货架号">
          <el-input v-model="queryParams.shelfNo" placeholder="请输入货架号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="启用" value="0" />
            <el-option label="停用" value="1" />
            <el-option label="占用" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 统计信息 -->
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
        <span class="stat-label">占用</span>
        <span class="stat-value orange">{{ stats.occupied }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">停用</span>
        <span class="stat-value gray">{{ stats.inactive }}</span>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-s-grid"></i>
          仓位列表
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
        <el-table-column prop="zoneName" label="所属仓区" width="150" show-overflow-tooltip />
        <el-table-column prop="locationCode" label="仓位编码" width="150" show-overflow-tooltip />
        <el-table-column prop="locationName" label="仓位名称" min-width="180" show-overflow-tooltip />
        <el-table-column label="位置信息" width="180" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.aisleNo || scope.row.shelfNo || scope.row.layerNo || scope.row.positionNo">
              {{ formatLocation(scope.row) }}
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="容量/使用" width="160" align="center">
          <template slot-scope="scope">
            <div class="capacity-cell">
              <span class="capacity-text">{{ scope.row.usage || 0 }}/{{ scope.row.capacity || 0 }}</span>
              <el-progress
                :percentage="calculatePercentage(scope.row)"
                :color="getProgressColor(scope.row)"
                :show-text="false"
                :stroke-width="6"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusStyle(scope.row.status)" size="small">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :append-to-body="true" :modal-append-to-body="true">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属仓库" prop="warehouseId">
              <el-select v-model="form.warehouseId" placeholder="请选择所属仓库" style="width: 100%" @change="handleFormWarehouseChange">
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
            <el-form-item label="所属仓区" prop="zoneId">
              <el-select v-model="form.zoneId" placeholder="请先选择仓库" style="width: 100%" :disabled="!form.warehouseId">
                <el-option
                  v-for="item in zoneOptionsFilteredForm"
                  :key="item.zoneId"
                  :label="item.zoneName"
                  :value="item.zoneId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="仓位编码" prop="locationCode">
              <el-input v-model="form.locationCode" placeholder="留空自动生成，或手动输入">
                <el-button slot="append" icon="el-icon-magic-stick" @click="generateLocationCode">自动生成</el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="仓位名称" prop="locationName">
              <el-input v-model="form.locationName" placeholder="请输入仓位名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="巷道号">
              <el-input v-model="form.aisleNo" placeholder="巷道" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="货架号">
              <el-input v-model="form.shelfNo" placeholder="货架" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="层号">
              <el-input v-model="form.layerNo" placeholder="层" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="位号">
              <el-input v-model="form.positionNo" placeholder="位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="容量">
              <el-input-number v-model="form.capacity" :min="0" :max="999999" style="width: 100%" placeholder="请输入容量" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="启用" value="0" />
                <el-option label="停用" value="1" />
                <el-option label="占用" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
    <el-dialog title="仓位详情" :visible.sync="viewDialogVisible" width="550px" :append-to-body="true">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="仓位编码" :span="2">{{ viewData.locationCode }}</el-descriptions-item>
        <el-descriptions-item label="仓位名称" :span="2">{{ viewData.locationName }}</el-descriptions-item>
        <el-descriptions-item label="所属仓库">{{ viewData.warehouseName }}</el-descriptions-item>
        <el-descriptions-item label="所属仓区">{{ viewData.zoneName }}</el-descriptions-item>
        <el-descriptions-item label="位置信息" :span="2">
          <span v-if="viewData.aisleNo || viewData.shelfNo || viewData.layerNo || viewData.positionNo">
            {{ formatLocation(viewData) }}
          </span>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="容量">{{ viewData.capacity || 0 }}</el-descriptions-item>
        <el-descriptions-item label="已使用">{{ viewData.usage || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <el-tag :type="getStatusStyle(viewData.status)" size="small">
            {{ getStatusLabel(viewData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ viewData.createTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listWmLocation,
  getWmLocation,
  addWmLocation,
  updateWmLocation,
  delWmLocation,
  delWmLocationBatch,
  listWmWarehouse,
  listWmZone
} from '@/api/md'

export default {
  name: 'WmLocation',
  data() {
    return {
      loading: false,
      submitLoading: false,
      total: 0,
      stats: {
        total: 0,
        active: 0,
        occupied: 0,
        inactive: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        warehouseId: '',
        zoneId: '',
        locationCode: '',
        locationName: '',
        aisleNo: '',
        shelfNo: '',
        status: ''
      },
      tableData: [],
      selectedRows: [],
      warehouseOptions: [],
      zoneOptions: [],
      dialogVisible: false,
      dialogTitle: '新增仓位',
      viewDialogVisible: false,
      viewData: {},
      form: {
        locationId: null,
        warehouseId: '',
        zoneId: '',
        locationCode: '',
        locationName: '',
        aisleNo: '',
        shelfNo: '',
        layerNo: '',
        positionNo: '',
        capacity: null,
        status: '0',
        remark: ''
      },
      rules: {
        warehouseId: [{ required: true, message: '请选择所属仓库', trigger: 'change' }],
        zoneId: [{ required: true, message: '请选择所属仓区', trigger: 'change' }],
        locationCode: [{ required: true, message: '请输入仓位编码', trigger: 'blur' }],
        locationName: [{ required: true, message: '请输入仓位名称', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
    }
  },
  computed: {
    // 查询栏中根据所选仓库过滤的仓区选项
    zoneOptionsFiltered() {
      if (!this.queryParams.warehouseId) {
        return []
      }
      return this.zoneOptions.filter(zone => zone.warehouseId === this.queryParams.warehouseId)
    },
    // 表单中根据所选仓库过滤的仓区选项
    zoneOptionsFilteredForm() {
      if (!this.form.warehouseId) {
        return []
      }
      return this.zoneOptions.filter(zone => zone.warehouseId === this.form.warehouseId)
    }
  },
  mounted() {
    this.fetchWarehouseOptions()
    this.fetchZoneOptions()
    this.fetchData()
  },
  methods: {
    // 获取仓库选项
    fetchWarehouseOptions() {
      listWmWarehouse({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.warehouseOptions = response.rows || []
      })
    },
    // 获取仓区选项
    fetchZoneOptions() {
      listWmZone({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.zoneOptions = response.rows || []
      })
    },
    // 获取数据
    fetchData() {
      this.loading = true
      listWmLocation(this.queryParams).then(response => {
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
      this.stats.occupied = data.filter(item => item.status === '2').length
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
        zoneId: '',
        locationCode: '',
        locationName: '',
        aisleNo: '',
        shelfNo: '',
        status: ''
      }
      this.fetchData()
    },
    // 仓库选择变化（查询栏）
    handleWarehouseChange(val) {
      this.queryParams.zoneId = ''
    },
    // 表单中仓库选择变化
    handleFormWarehouseChange(val) {
      this.form.zoneId = ''
    },
    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增仓位'
      this.resetForm()
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑仓位'
      this.form = { ...row }
      this.dialogVisible = true
    },
    // 查看
    handleView(row) {
      getWmLocation(row.locationId).then(response => {
        this.viewData = response.data
        this.viewDialogVisible = true
      })
    },
    // 批量删除
    handleBatchDelete() {
      const locationIds = this.selectedRows.map(row => row.locationId)
      const locationNames = this.selectedRows.map(row => row.locationName).join(', ')
      this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个仓位吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delWmLocationBatch(locationIds).then(() => {
          this.$message.success('批量删除成功')
          this.selectedRows = []
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
        locationId: null,
        warehouseId: '',
        zoneId: '',
        locationCode: '',
        locationName: '',
        aisleNo: '',
        shelfNo: '',
        layerNo: '',
        positionNo: '',
        capacity: null,
        status: '0',
        remark: ''
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.form.locationId) {
            updateWmLocation(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addWmLocation(this.form).then(() => {
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
    // 生成仓位编码
    generateLocationCode() {
      const prefix = 'LOC'
      const timestamp = Date.now().toString().slice(-6)
      const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0')
      this.form.locationCode = `${prefix}${timestamp}${random}`
    },
    // 格式化位置信息
    formatLocation(row) {
      const parts = []
      if (row.aisleNo) parts.push(`${row.aisleNo}巷`)
      if (row.shelfNo) parts.push(`${row.shelfNo}架`)
      if (row.layerNo) parts.push(`${row.layerNo}层`)
      if (row.positionNo) parts.push(`${row.positionNo}位`)
      return parts.join('-') || '-'
    },
    // 计算使用百分比
    calculatePercentage(row) {
      if (!row.capacity || row.capacity === 0) return 0
      const usage = row.usage || 0
      return Math.round((usage / row.capacity) * 100)
    },
    // 获取进度条颜色
    getProgressColor(row) {
      const percentage = this.calculatePercentage(row)
      if (percentage >= 90) return '#f56c6c'
      if (percentage >= 70) return '#e6a23c'
      return '#67c23a'
    },
    // 获取状态标签
    getStatusLabel(status) {
      const map = { '0': '启用', '1': '停用', '2': '占用' }
      return map[status] || status
    },
    // 获取状态样式
    getStatusStyle(status) {
      const map = { '0': 'success', '1': 'info', '2': 'warning' }
      return map[status] || ''
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
      margin-bottom: 10px;
      margin-right: 20px;
    }
  }
}

// 统计信息
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

      &.gray {
        color: #909399;
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

// 容量单元格
.capacity-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;

  .capacity-text {
    font-size: 12px;
    color: #606266;
  }

  ::v-deep .el-progress {
    width: 80px;
  }
}

// 分页
.pagination {
  margin-top: 20px;
  text-align: right;
}

// 文字 muted
.text-muted {
  color: #909399;
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
