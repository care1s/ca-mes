<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header sale">
      <div class="title-section">
        <i class="el-icon-s-order"></i>
        <span class="title">销售订单</span>
        <span class="subtitle">Sales Order</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增订单</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">订单总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">待审核</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">生产中</span>
        <span class="stat-value purple">{{ stats.producing }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已完成</span>
        <span class="stat-value green">{{ stats.completed }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="queryParams.orderNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="queryParams.clientName" placeholder="请输入客户名称" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" :value="0" />
            <el-option label="已提交" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="生产中" :value="3" />
            <el-option label="部分出库" :value="4" />
            <el-option label="已完成" :value="5" />
            <el-option label="已取消" :value="6" />
          </el-select>
        </el-form-item>
        <el-form-item label="交货状态">
          <el-select v-model="queryParams.deliveryStatus" placeholder="请选择交货状态" clearable>
            <el-option label="未发货" :value="0" />
            <el-option label="部分发货" :value="1" />
            <el-option label="已发货" :value="2" />
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
      <el-table :data="tableData" stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="orderNo" label="订单编号" width="140" />
        <el-table-column prop="orderDate" label="订单日期" width="100" />
        <el-table-column prop="clientName" label="客户名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="deliveryDate" label="交货日期" width="100" />
        <el-table-column prop="payableAmount" label="应付金额" width="120" align="right">
          <template slot-scope="scope">
            <span style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.payableAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deliveryStatus" label="交货状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getDeliveryStatusType(scope.row.deliveryStatus)" size="small">
              {{ getDeliveryStatusText(scope.row.deliveryStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="320" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button v-if="scope.row.status === 1" type="text" size="small" @click="handleAudit(scope.row)">审核</el-button>
            <el-button v-if="scope.row.status >= 2 && scope.row.status < 5" type="text" size="small" @click="handleCreateIssue(scope.row)">生成出库</el-button>
            <el-button v-if="scope.row.status < 5" type="text" size="small" style="color: #f56c6c" @click="handleCancel(scope.row)">取消</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false" :modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="订单编号">
              <el-input v-model="form.orderNo" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单日期" prop="orderDate">
              <el-date-picker v-model="form.orderDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户" prop="clientId">
              <el-select v-model="form.clientId" placeholder="请选择客户" style="width: 100%" @change="handleClientChange">
                <el-option v-for="item in clientList" :key="item.clientId" :label="item.clientName" :value="item.clientId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交货日期" prop="deliveryDate">
              <el-date-picker v-model="form.deliveryDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="交货地址">
          <el-input v-model="form.deliveryAddress" placeholder="请输入交货地址" />
        </el-form-item>

        <!-- 订单明细 -->
        <el-divider content-position="left">订单明细</el-divider>
        <div class="item-actions">
          <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAddItem">添加物料</el-button>
        </div>
        <el-table :data="form.items" border style="margin-top: 10px;">
          <el-table-column type="index" label="序号" width="50" align="center" />
          <el-table-column label="物料" min-width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.itemId2" placeholder="请选择物料" style="width: 100%" @change="handleItemChange(scope.row)">
                <el-option v-for="item in itemList" :key="item.itemId" :label="item.itemName" :value="item.itemId" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="规格型号" width="120">
            <template slot-scope="scope">
              <span>{{ scope.row.specification }}</span>
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template slot-scope="scope">
              <span>{{ scope.row.unit }}</span>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" style="width: 100%" @change="calculateItemTotal(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.unitPrice" :min="0" :precision="2" style="width: 100%" @change="calculateItemTotal(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="总价" width="120">
            <template slot-scope="scope">
              <span style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.totalPrice }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" style="color: #f56c6c" @click="handleDeleteItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 金额汇总 -->
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="8">
            <el-form-item label="订单金额">
              <el-input v-model="form.totalAmount" disabled>
                <template slot="prefix">¥</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税额">
              <el-input-number v-model="form.taxAmount" :min="0" :precision="2" style="width: 100%" @change="calculatePayable" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="折扣金额">
              <el-input-number v-model="form.discountAmount" :min="0" :precision="2" style="width: 100%" @change="calculatePayable" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="应付金额">
          <el-input v-model="form.payableAmount" disabled style="width: 200px;">
            <template slot="prefix" style="color: #f56c6c; font-weight: bold;">¥</template>
          </el-input>
          <span style="margin-left: 10px; color: #f56c6c; font-size: 16px; font-weight: bold;">
            ¥{{ form.payableAmount }}
          </span>
        </el-form-item>

        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button type="success" @click="submitAndAudit">保存并提交</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="viewDialogVisible" width="800px" :modal="false">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号">{{ viewForm.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="订单日期">{{ viewForm.orderDate }}</el-descriptions-item>
        <el-descriptions-item label="客户名称">{{ viewForm.clientName }}</el-descriptions-item>
        <el-descriptions-item label="交货日期">{{ viewForm.deliveryDate }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ viewForm.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ viewForm.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="交货地址" :span="2">{{ viewForm.deliveryAddress }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(viewForm.status)">{{ getStatusText(viewForm.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="交货状态">
          <el-tag :type="getDeliveryStatusType(viewForm.deliveryStatus)">{{ getDeliveryStatusText(viewForm.deliveryStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ viewForm.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="应付金额" style="color: #f56c6c; font-weight: bold;">¥{{ viewForm.payableAmount }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewForm.remark || '-' }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">订单明细</el-divider>
      <el-table :data="viewForm.items" border>
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="itemName" label="物料名称" min-width="150" />
        <el-table-column prop="specification" label="规格型号" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="quantity" label="数量" width="100" align="right" />
        <el-table-column prop="unitPrice" label="单价" width="100" align="right">
          <template slot-scope="scope">¥{{ scope.row.unitPrice }}</template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总价" width="100" align="right">
          <template slot-scope="scope">¥{{ scope.row.totalPrice }}</template>
        </el-table-column>
        <el-table-column prop="deliveredQty" label="已发货" width="100" align="right" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listSaleOrder, getSaleOrder, addSaleOrder, updateSaleOrder, delSaleOrder, auditOrder, cancelOrder, completeOrder } from '@/api/sale'

export default {
  name: 'SaleOrder',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        orderNo: '',
        clientName: '',
        status: null,
        deliveryStatus: null
      },
      stats: {
        total: 0,
        pending: 0,
        producing: 0,
        completed: 0
      },
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      form: {
        orderNo: '',
        orderDate: this.getCurrentDate(),
        clientId: null,
        clientCode: '',
        clientName: '',
        deliveryDate: null,
        deliveryAddress: '',
        contactPerson: '',
        contactPhone: '',
        totalAmount: 0,
        taxAmount: 0,
        discountAmount: 0,
        payableAmount: 0,
        items: []
      },
      viewForm: {},
      rules: {
        orderDate: [{ required: true, message: '请选择订单日期', trigger: 'change' }],
        clientId: [{ required: true, message: '请选择客户', trigger: 'change' }],
        deliveryDate: [{ required: true, message: '请选择交货日期', trigger: 'change' }]
      },
      // 模拟数据
      clientList: [
        { clientId: 1, clientCode: 'C001', clientName: '华为技术有限公司' },
        { clientId: 2, clientCode: 'C002', clientName: '比亚迪股份有限公司' },
        { clientId: 3, clientCode: 'C003', clientName: '美的集团股份有限公司' }
      ],
      itemList: [
        { itemId: 1, itemCode: 'P001', itemName: '手机主板', specification: '标准版', unit: '片' },
        { itemId: 2, itemCode: 'P002', itemName: '电池组件', specification: '4000mAh', unit: '个' },
        { itemId: 3, itemCode: 'P003', itemName: '显示屏', specification: '6.1英寸', unit: '片' }
      ]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    getCurrentDate() {
      const now = new Date()
      return now.toISOString().split('T')[0]
    },
    fetchData() {
      this.loading = true
      // 模拟数据
      setTimeout(() => {
        this.tableData = [
          {
            orderId: 1,
            orderNo: 'SO202403001',
            orderDate: '2024-03-15',
            clientName: '华为技术有限公司',
            deliveryDate: '2024-04-15',
            payableAmount: '158000.00',
            status: 2,
            deliveryStatus: 0,
            createTime: '2024-03-15 10:00:00'
          },
          {
            orderId: 2,
            orderNo: 'SO202403002',
            orderDate: '2024-03-18',
            clientName: '比亚迪股份有限公司',
            deliveryDate: '2024-04-20',
            payableAmount: '256000.00',
            status: 3,
            deliveryStatus: 0,
            createTime: '2024-03-18 14:30:00'
          }
        ]
        this.total = 2
        this.calculateStats()
        this.loading = false
      }, 500)
    },
    calculateStats() {
      this.stats.total = this.total
      this.stats.pending = this.tableData.filter(item => item.status === 1).length
      this.stats.producing = this.tableData.filter(item => item.status === 3).length
      this.stats.completed = this.tableData.filter(item => item.status === 5).length
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
        clientName: '',
        status: null,
        deliveryStatus: null
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
      this.dialogTitle = '新增销售订单'
      this.form = {
        orderNo: '',
        orderDate: this.getCurrentDate(),
        clientId: null,
        clientCode: '',
        clientName: '',
        deliveryDate: null,
        deliveryAddress: '',
        contactPerson: '',
        contactPhone: '',
        totalAmount: 0,
        taxAmount: 0,
        discountAmount: 0,
        payableAmount: 0,
        items: []
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.viewForm = { ...row, items: [
        { itemName: '手机主板', specification: '标准版', unit: '片', quantity: 100, unitPrice: 500, totalPrice: 50000, deliveredQty: 0 },
        { itemName: '电池组件', specification: '4000mAh', unit: '个', quantity: 200, unitPrice: 80, totalPrice: 16000, deliveredQty: 0 }
      ]}
      this.viewDialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑销售订单'
      this.form = { ...row, items: [
        { itemId2: 1, itemCode: 'P001', itemName: '手机主板', specification: '标准版', unit: '片', quantity: 100, unitPrice: 500, totalPrice: 50000 },
        { itemId2: 2, itemCode: 'P002', itemName: '电池组件', specification: '4000mAh', unit: '个', quantity: 200, unitPrice: 80, totalPrice: 16000 }
      ]}
      this.calculatePayable()
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该销售订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    handleAudit(row) {
      this.$confirm('确认审核通过该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('审核通过')
        this.fetchData()
      }).catch(() => {})
    },
    handleCancel(row) {
      this.$confirm('确认取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('订单已取消')
        this.fetchData()
      }).catch(() => {})
    },
    handleCreateIssue(row) {
      this.$confirm('确认生成出库单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.$message.success('出库单生成成功，请到出库管理查看')
        this.fetchData()
      }).catch(() => {})
    },
    handleClientChange(clientId) {
      const client = this.clientList.find(item => item.clientId === clientId)
      if (client) {
        this.form.clientCode = client.clientCode
        this.form.clientName = client.clientName
      }
    },
    handleAddItem() {
      this.form.items.push({
        itemId2: null,
        itemCode: '',
        itemName: '',
        specification: '',
        unit: '',
        quantity: 1,
        unitPrice: 0,
        totalPrice: 0
      })
    },
    handleDeleteItem(index) {
      this.form.items.splice(index, 1)
      this.calculateTotal()
    },
    handleItemChange(row) {
      const item = this.itemList.find(i => i.itemId === row.itemId2)
      if (item) {
        row.itemCode = item.itemCode
        row.itemName = item.itemName
        row.specification = item.specification
        row.unit = item.unit
      }
    },
    calculateItemTotal(row) {
      if (row.quantity && row.unitPrice) {
        row.totalPrice = (row.quantity * row.unitPrice).toFixed(2)
      }
      this.calculateTotal()
    },
    calculateTotal() {
      let total = 0
      this.form.items.forEach(item => {
        total += parseFloat(item.totalPrice || 0)
      })
      this.form.totalAmount = total.toFixed(2)
      this.calculatePayable()
    },
    calculatePayable() {
      const total = parseFloat(this.form.totalAmount || 0)
      const tax = parseFloat(this.form.taxAmount || 0)
      const discount = parseFloat(this.form.discountAmount || 0)
      this.form.payableAmount = (total + tax - discount).toFixed(2)
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.items.length === 0) {
            this.$message.error('请至少添加一个物料')
            return
          }
          this.$message.success(this.form.orderId ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    submitAndAudit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.items.length === 0) {
            this.$message.error('请至少添加一个物料')
            return
          }
          this.$message.success('保存并提交审核成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'primary', 4: 'warning', 5: 'success', 6: 'danger' }
      return types[status] || 'info'
    },
    getStatusText(status) {
      const texts = { 0: '草稿', 1: '已提交', 2: '已审核', 3: '生产中', 4: '部分出库', 5: '已完成', 6: '已取消' }
      return texts[status] || '未知'
    },
    getDeliveryStatusType(status) {
      const types = { 0: 'info', 1: 'warning', 2: 'success' }
      return types[status] || 'info'
    },
    getDeliveryStatusText(status) {
      const texts = { 0: '未发货', 1: '部分发货', 2: '已发货' }
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
      color: #67c23a;
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

.page-header.sale {
  i {
    color: #67c23a;
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
      &.purple { color: #9b59b6; }
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

.item-actions {
  margin-bottom: 10px;
}

::v-deep .el-loading-mask {
  display: none !important;
}
</style>
