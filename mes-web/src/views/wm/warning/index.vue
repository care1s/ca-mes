<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-warning-outline"></i>
        <span class="title">库存预警</span>
        <span class="subtitle">Stock Warning</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增预警</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">预警总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">库存不足</span>
        <span class="stat-value red">{{ stats.lowStock }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">库存积压</span>
        <span class="stat-value warning">{{ stats.overStock }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">未处理</span>
        <span class="stat-value orange">{{ stats.unhandled }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="物料编码">
          <el-input v-model="queryParams.itemCode" placeholder="请输入编码" clearable />
        </el-form-item>
        <el-form-item label="物料名称">
          <el-input v-model="queryParams.itemName" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select v-model="queryParams.warningType" placeholder="请选择类型" clearable>
            <el-option label="库存不足" :value="1" />
            <el-option label="库存积压" :value="2" />
            <el-option label="库存过期" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select v-model="queryParams.warningLevel" placeholder="请选择级别" clearable>
            <el-option label="一般" :value="1" />
            <el-option label="严重" :value="2" />
            <el-option label="紧急" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="未处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已处理" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table  :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="itemCode" label="物料编码" width="120" />
        <el-table-column prop="itemName" label="物料名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="specification" label="规格" width="120" show-overflow-tooltip />
        <el-table-column prop="currentQty" label="当前库存" width="90" align="right" />
        <el-table-column prop="safetyQty" label="安全库存" width="90" align="right" />
        <el-table-column prop="maxQty" label="最高库存" width="90" align="right" />
        <el-table-column prop="warningType" label="预警类型" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeType(scope.row.warningType)" size="small">
              {{ getTypeText(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningLevel" label="级别" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.warningLevel)" size="small">
              {{ getLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="预警时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" @click="handleHandle(scope.row)">处理</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 处理对话框 -->
    <el-dialog title="处理预警" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false" :append-to-body="true" :modal-append-to-body="true">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="物料信息">
          <div>{{ form.itemCode }} - {{ form.itemName }}</div>
        </el-form-item>
        <el-form-item label="当前库存">
          <div>{{ form.currentQty }}</div>
        </el-form-item>
        <el-form-item label="预警说明">
          <div>{{ form.warningDesc }}</div>
        </el-form-item>
        <el-form-item label="处理人" prop="handlerName">
          <el-input v-model="form.handlerName" placeholder="请输入处理人" />
        </el-form-item>
        <el-form-item label="处理备注" prop="handleRemark">
          <el-input v-model="form.handleRemark" type="textarea" :rows="3" placeholder="请输入处理备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认处理</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'WmWarning',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        itemCode: '',
        itemName: '',
        warningType: null,
        warningLevel: null,
        status: null
      },
      stats: {
        total: 0,
        lowStock: 0,
        overStock: 0,
        unhandled: 0
      },
      dialogVisible: false,
      form: {
        warningId: null,
        itemCode: '',
        itemName: '',
        currentQty: 0,
        warningDesc: '',
        handlerName: '',
        handleRemark: ''
      },
      rules: {
        handlerName: [{ required: true, message: '请输入处理人', trigger: 'blur' }],
        handleRemark: [{ required: true, message: '请输入处理备注', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            warningId: 1,
            warehouseName: '原材料仓库',
            itemCode: 'MAT001',
            itemName: '钢材A',
            specification: '20mm*2000mm',
            currentQty: 15,
            safetyQty: 50,
            maxQty: 500,
            warningType: 1,
            warningLevel: 3,
            warningDesc: '库存低于安全库存',
            status: 0,
            createTime: '2024-03-17 10:00:00'
          },
          {
            warningId: 2,
            warehouseName: '成品仓库',
            itemCode: 'PRO001',
            itemName: '产品B',
            specification: '标准款',
            currentQty: 800,
            safetyQty: 100,
            maxQty: 500,
            warningType: 2,
            warningLevel: 2,
            warningDesc: '库存超过最高库存',
            status: 0,
            createTime: '2024-03-17 11:00:00'
          }
        ]
        this.total = 2
        this.calculateStats()
        this.loading = false
      }, 500)
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.lowStock = this.tableData.filter(item => item.warningType === 1).length
      this.stats.overStock = this.tableData.filter(item => item.warningType === 2).length
      this.stats.unhandled = this.tableData.filter(item => item.status === 0).length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        itemCode: '',
        itemName: '',
        warningType: null,
        warningLevel: null,
        status: null
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    handleAdd() {
      this.$message.info('新增预警功能开发中')
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleHandle(row) {
      this.form = { 
        warningId: row.warningId,
        itemCode: row.itemCode,
        itemName: row.itemName,
        currentQty: row.currentQty,
        warningDesc: row.warningDesc,
        handlerName: '',
        handleRemark: ''
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该预警记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success('预警处理成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getTypeType(type) {
      const types = { 1: 'danger', 2: 'warning', 3: 'info' }
      return types[type] || 'info'
    },
    getTypeText(type) {
      const texts = { 1: '库存不足', 2: '库存积压', 3: '库存过期' }
      return texts[type] || '未知'
    },
    getLevelType(level) {
      const types = { 1: 'success', 2: 'warning', 3: 'danger' }
      return types[level] || 'info'
    },
    getLevelText(level) {
      const texts = { 1: '一般', 2: '严重', 3: '紧急' }
      return texts[level] || '未知'
    },
    getStatusType(status) {
      const types = { 0: 'danger', 1: 'warning', 2: 'success' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '未处理', 1: '处理中', 2: '已处理' }
      return texts[status] || '未知'
    }
  }
}
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 1px solid #ebeef5;

  .title-section {
    display: flex;
    align-items: center;
    gap: 10px;

    i {
      font-size: 24px;
      color: #e6a23c;
    }

    .title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
    }

    .subtitle {
      font-size: 14px;
      color: #909399;
      font-weight: normal;
    }
  }
}

.stats-bar {
  display: flex;
  align-items: center;
  gap: 0;
  margin-bottom: 20px;
  padding: 15px 20px;
  background: #f5f7fa;
  border-radius: 4px;

  .stat-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0 25px;

    .stat-label {
      font-size: 13px;
      color: #606266;
      margin-bottom: 5px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: 600;

      &.blue { color: #409eff; }
      &.red { color: #f56c6c; }
      &.warning { color: #e6a23c; }
      &.orange { color: #ffba00; }
    }
  }

  .stat-divider {
    width: 1px;
    height: 40px;
    background: #dcdfe6;
  }
}

.search-card {
  margin-bottom: 20px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }
}

.table-card {
  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 确保按钮可点击，移除任何可能的遮挡 */
.el-button {
  pointer-events: auto !important;
  z-index: 1;
}

/* 移除表格加载遮罩 */
.el-table {
  pointer-events: auto;
}

.el-table .el-table__body-wrapper {
  pointer-events: auto;
}

/* 确保操作列按钮可点击 */
.el-table .cell {
  pointer-events: auto;
}

/* 移除任何可能的遮罩层 */
::v-deep .el-loading-mask {
  display: none !important;
}

::v-deep .el-loading-spinner {
  display: none !important;
}

/* 确保卡片内容可点击 */
.el-card {
  pointer-events: auto;
}

.el-card__body {
  pointer-events: auto;
}
</style>
