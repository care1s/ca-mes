<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pur">
      <div class="title-section">
        <i class="el-icon-document"></i>
        <span class="title">采购订单</span>
        <span class="subtitle">Purchase Order</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增订单</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">草稿</span>
        <span class="stat-value gray">{{ stats.draft }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已确认</span>
        <span class="stat-value green">{{ stats.confirmed }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value blue">{{ stats.completed }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入编号" clearable />
        </el-form-item>
        <el-form-item label="供应商">
          <el-input v-model="queryParams.vendorName" placeholder="请输入供应商" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="已确认" :value="1" />
            <el-option label="部分收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
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
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="orderNo" label="订单编号" width="140" />
        <el-table-column prop="vendorName" label="供应商" min-width="120" />
        <el-table-column prop="orderDate" label="订单日期" width="100" />
        <el-table-column prop="deliveryDate" label="交货日期" width="100" />
        <el-table-column prop="totalAmount" label="总金额" width="100" align="right">
          <template slot-scope="scope">{{ formatMoney(scope.row.totalAmount) }}</template>
        </el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getOrderStatusType(scope.row.orderStatus)">
              {{ getOrderStatusText(scope.row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审批状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAuditStatusType(scope.row.auditStatus)" size="small">
              {{ getAuditStatusText(scope.row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单编号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请单号" prop="requestNo">
              <el-input v-model="form.requestNo" placeholder="关联申请单号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="供应商" prop="vendorName">
              <el-input v-model="form.vendorName" placeholder="请选择供应商" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker v-model="form.orderDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交货日期" prop="deliveryDate">
              <el-date-picker v-model="form.deliveryDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="币种" prop="currency">
              <el-select v-model="form.currency" placeholder="请选择币种" style="width: 100%">
                <el-option label="人民币 (CNY)" value="CNY" />
                <el-option label="美元 (USD)" value="USD" />
                <el-option label="欧元 (EUR)" value="EUR" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人" prop="contactName">
              <el-input v-model="form.contactName" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="送货地址" prop="deliveryAddress">
          <el-input v-model="form.deliveryAddress" type="textarea" :rows="2" placeholder="请输入送货地址" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPurOrder, addPurOrder, updatePurOrder, delPurOrder, getPurOrder } from '@/api/md'

export default {
  name: 'PurOrder',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        orderNo: '',
        vendorName: '',
        orderStatus: null
      },
      stats: {
        total: 0,
        draft: 0,
        confirmed: 0,
        completed: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        orderNo: '',
        requestNo: '',
        vendorId: null,
        vendorCode: '',
        vendorName: '',
        orderDate: null,
        deliveryDate: null,
        totalAmount: 0,
        totalQuantity: 0,
        currency: 'CNY',
        orderStatus: 0,
        auditStatus: 0,
        contactName: '',
        contactPhone: '',
        deliveryAddress: '',
        remark: ''
      },
      rules: {
        vendorName: [{ required: true, message: '请选择供应商', trigger: 'blur' }],
        orderDate: [{ required: true, message: '请选择订单日期', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listPurOrder(this.queryParams).then(response => {
        this.tableData = response.rows || []
        this.total = response.total || 0
        this.calculateStats()
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.draft = this.tableData.filter(item => item.orderStatus === 0).length
      this.stats.confirmed = this.tableData.filter(item => item.orderStatus === 1).length
      this.stats.completed = this.tableData.filter(item => item.orderStatus === 3).length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        orderNo: '',
        vendorName: '',
        orderStatus: null
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
      this.dialogTitle = '新增采购订单'
      this.form = {
        orderNo: '',
        requestNo: '',
        vendorId: null,
        vendorCode: '',
        vendorName: '',
        orderDate: new Date(),
        deliveryDate: null,
        totalAmount: 0,
        totalQuantity: 0,
        currency: 'CNY',
        orderStatus: 0,
        auditStatus: 0,
        contactName: '',
        contactPhone: '',
        deliveryAddress: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看功能开发中')
    },
    handleEdit(row) {
      this.dialogTitle = '编辑采购订单'
      getPurOrder(row.id).then(response => {
        this.form = { ...response.data }
        this.dialogVisible = true
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该采购订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delPurOrder(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id) {
            updatePurOrder(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addPurOrder(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    formatMoney(value) {
      if (!value) return '¥0.00'
      return '¥' + parseFloat(value).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,')
    },
    getOrderStatusType(status) {
      const types = { 0: 'info', 1: 'success', 2: 'warning', 3: 'primary', 4: 'danger' }
      return types[status] || 'info'
    },
    getOrderStatusText(status) {
      const texts = { 0: '草稿', 1: '已确认', 2: '部分收货', 3: '已完成', 4: '已取消' }
      return texts[status] || '未知'
    },
    getAuditStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },
    getAuditStatusText(status) {
      const texts = { 0: '草稿', 1: '待审批', 2: '已通过', 3: '已拒绝' }
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
      color: #409eff;
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
      &.green { color: #67c23a; }
      &.orange { color: #e6a23c; }
      &.gray { color: #909399; }
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
</style>
