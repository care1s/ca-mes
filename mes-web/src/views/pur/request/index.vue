<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header pur">
      <div class="title-section">
        <i class="el-icon-document"></i>
        <span class="title">采购申请</span>
        <span class="subtitle">Purchase Request</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增申请</el-button>
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
        <span class="stat-label">待审批</span>
        <span class="stat-value orange">{{ stats.pending }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">已审批</span>
        <span class="stat-value green">{{ stats.approved }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="申请单号">
          <el-input v-model="queryParams.requestCode" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="申请类型">
          <el-select v-model="queryParams.requestType" placeholder="请选择类型" clearable>
            <el-option label="普通" value="NORMAL" />
            <el-option label="紧急" value="URGENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
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
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-document"></i>
          采购申请列表
        </span>
      </div>

      <el-table
        :data="tableData"
        border
        stripe
        highlight-current-row
        style="width: 100%"
        @row-dblclick="handleRowDblclick"
      >
        <el-table-column type="index" label="序号" width="80" align="center" fixed />
        <el-table-column prop="requestCode" label="申请单号" width="150" show-overflow-tooltip />
        <el-table-column prop="requestDate" label="申请日期" width="120" align="center" />
        <el-table-column prop="requestType" label="类型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.requestType === 'URGENT'" type="danger" size="mini">紧急</el-tag>
            <el-tag v-else type="info" size="mini">普通</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="申请部门" width="120" show-overflow-tooltip />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.totalAmount">{{ formatMoney(scope.row.totalAmount) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'DRAFT'" type="info" size="mini">草稿</el-tag>
            <el-tag v-else-if="scope.row.status === 'PENDING'" type="warning" size="mini">待审批</el-tag>
            <el-tag v-else-if="scope.row.status === 'APPROVED'" type="success" size="mini">已审批</el-tag>
            <el-tag v-else-if="scope.row.status === 'REJECTED'" type="danger" size="mini">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <!-- 草稿状态显示提交审批 -->
            <el-button v-if="scope.row.status === 'DRAFT'" type="text" icon="el-icon-s-promotion" @click="handleSubmit(scope.row)">提交</el-button>
            <!-- 待审批状态显示审批操作 -->
            <template v-if="scope.row.status === 'PENDING'">
              <el-button type="text" icon="el-icon-check" style="color: #67c23a" @click="handleApprove(scope.row)">通过</el-button>
              <el-button type="text" icon="el-icon-close" style="color: #f56c6c" @click="handleReject(scope.row)">拒绝</el-button>
            </template>
            <!-- 已审批状态显示生成订单 -->
            <el-button v-if="scope.row.status === 'APPROVED'" type="text" icon="el-icon-s-order" style="color: #409eff" @click="handleGenerateOrder(scope.row)">生成订单</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="900px" :modal="false" custom-class="no-mask-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <!-- 基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请单号" prop="requestCode">
              <el-input v-model="form.requestCode" placeholder="系统自动生成" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请日期" prop="requestDate">
              <el-date-picker v-model="form.requestDate" type="date" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="申请类型" prop="requestType">
              <el-select v-model="form.requestType" placeholder="请选择类型" style="width: 100%">
                <el-option label="普通" value="NORMAL" />
                <el-option label="紧急" value="URGENT" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="申请部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入申请部门" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 明细表格 -->
        <el-divider content-position="left">采购明细</el-divider>
        <div class="item-table-header">
          <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAddItem">添加物料</el-button>
          <span class="total-amount">合计金额: {{ formatMoney(calculateTotal) }}</span>
        </div>
        <el-table :data="form.items" border size="small" style="width: 100%; margin-top: 10px;">
          <el-table-column type="index" label="序号" width="80" align="center" />
          <el-table-column label="物料编码" width="130">
            <template slot-scope="scope">
              <div style="display: flex; align-items: center;">
                <el-input v-model="scope.row.itemCode" size="mini" placeholder="点击选择" readonly style="flex: 1;" />
                <el-button type="text" icon="el-icon-search" size="mini" @click="openItemSelect(scope.$index)" style="margin-left: 5px;" />
              </div>
            </template>
          </el-table-column>
          <el-table-column label="物料名称" width="180">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemName" size="mini" placeholder="自动填充" readonly />
            </template>
          </el-table-column>
          <el-table-column label="规格型号" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.itemSpec" size="mini" placeholder="自动填充" readonly />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template slot-scope="scope">
              <el-input v-model="scope.row.unit" size="mini" placeholder="自动填充" readonly />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="110">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :precision="4" :min="0" size="mini" style="width: 100px" />
            </template>
          </el-table-column>
          <el-table-column label="需求日期" width="130">
            <template slot-scope="scope">
              <el-date-picker v-model="scope.row.requiredDate" type="date" size="mini" style="width: 120px" placeholder="选择日期" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
              <el-button type="text" size="small" style="color: #f56c6c" @click="handleDeleteItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-form-item label="备注" prop="remark" style="margin-top: 15px;">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog title="查看采购申请" :visible.sync="viewDialogVisible" width="800px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请单号">{{ viewData.requestCode }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ viewData.requestDate }}</el-descriptions-item>
        <el-descriptions-item label="申请类型">
          <el-tag v-if="viewData.requestType === 'URGENT'" type="danger">紧急</el-tag>
          <el-tag v-else type="info">普通</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="viewData.status === 'DRAFT'" type="info">草稿</el-tag>
          <el-tag v-else-if="viewData.status === 'PENDING'" type="warning">待审批</el-tag>
          <el-tag v-else-if="viewData.status === 'APPROVED'" type="success">已审批</el-tag>
          <el-tag v-else-if="viewData.status === 'REJECTED'" type="danger">已拒绝</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请人">{{ viewData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请部门">{{ viewData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="总金额" :span="2">
          <span style="color: #f56c6c; font-weight: bold; font-size: 16px;">{{ formatMoney(viewData.totalAmount) }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ viewData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">采购明细</el-divider>
      <el-table :data="viewData.items" border size="small" style="width: 100%">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="itemCode" label="物料编码" width="120" />
        <el-table-column prop="itemName" label="物料名称" width="150" />
        <el-table-column prop="itemSpec" label="规格型号" width="120" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="quantity" label="数量" width="100" align="right" />
        <el-table-column prop="price" label="单价" width="100" align="right">
          <template slot-scope="scope">{{ formatMoney(scope.row.price) }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="100" align="right">
          <template slot-scope="scope">{{ formatMoney(scope.row.amount) }}</template>
        </el-table-column>
        <el-table-column prop="requiredDate" label="需求日期" width="120" />
      </el-table>

      <div slot="footer">
        <el-button @click="viewDialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 物料选择弹窗 -->
    <el-dialog title="选择物料" :visible.sync="itemDialogVisible" width="700px" :modal="false" custom-class="no-mask-dialog">
      <el-form :inline="true" :model="itemQueryParams" class="search-form">
        <el-form-item label="物料编码/名称">
          <el-input v-model="itemQueryParams.keyword" placeholder="请输入物料编码或名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleItemQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="itemTableData" border highlight-current-row style="margin-top: 10px">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="itemCode" label="物料编码" width="120" />
        <el-table-column prop="itemName" label="物料名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="itemSpec" label="规格型号" width="120" show-overflow-tooltip />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="selectItem(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        class="pagination"
        background
        layout="total, prev, pager, next"
        :total="itemTotal"
        :page-size="itemQueryParams.pageSize"
        :current-page="itemQueryParams.pageNum"
        @size-change="handleItemSizeChange"
        @current-change="handleItemCurrentChange"
        style="margin-top: 15px;"
      />
    </el-dialog>
  </div>
</template>

<script>
import {
  listPurRequest, addPurRequest, updatePurRequest, delPurRequest,
  getPurRequest, submitPurRequest, approvePurRequest, rejectPurRequest,
  createOrderFromRequest, listItem
} from '@/api/md'

export default {
  name: 'PurRequest',
  data() {
    return {
      loading: false,
      total: 0,
      stats: {
        total: 0,
        pending: 0,
        approved: 0
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        requestCode: '',
        requestType: '',
        status: ''
      },
      tableData: [],
      dialogVisible: false,
      viewDialogVisible: false,
      dialogTitle: '',
      viewData: {},
      form: {
        requestId: null,
        requestCode: '',
        requestDate: '',
        requestType: 'NORMAL',
        applicantName: '',
        deptName: '',
        totalAmount: 0,
        status: 'DRAFT',
        remark: '',
        items: []
      },
      rules: {
        requestDate: [{ required: true, message: '请选择申请日期', trigger: 'change' }],
        requestType: [{ required: true, message: '请选择申请类型', trigger: 'change' }]
      },
      // 物料选择弹窗
      itemDialogVisible: false,
      itemQueryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: ''
      },
      itemTableData: [],
      itemTotal: 0,
      currentRowIndex: null
    }
  },
  computed: {
    calculateTotal() {
      if (!this.form.items || this.form.items.length === 0) return 0
      return this.form.items.reduce((sum, item) => {
        return sum + (item.amount || 0)
      }, 0)
    }
  },
  mounted() {
    this.fetchData()
    this.fetchDeptOptions()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await listPurRequest(this.queryParams)
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
      this.stats.pending = this.tableData.filter(item => item.status === 'PENDING').length
      this.stats.approved = this.tableData.filter(item => item.status === 'APPROVED').length
    },

    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },

    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        requestCode: '',
        requestType: '',
        status: ''
      }
      this.fetchData()
    },

    handleAdd() {
      this.dialogTitle = '新增采购申请'
      // 从store获取当前用户信息和部门
      const userName = this.$store.state.user.name || ''
      const deptName = this.$store.state.user.deptName || ''
      this.form = {
        requestId: null,
        requestCode: '',
        requestDate: new Date(),
        requestType: 'NORMAL',
        applicantName: userName,
        deptName: deptName,
        totalAmount: 0,
        status: 'DRAFT',
        remark: '',
        items: []
      }
      this.dialogVisible = true
    },

    async handleEdit(row) {
      this.dialogTitle = '编辑采购申请'
      try {
        const res = await getPurRequest(row.requestId)
        if (res.code === 200) {
          this.form = { ...res.data }
          // 确保明细存在
          if (!this.form.items) {
            this.form.items = []
          }
          this.dialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      }
    },

    async handleView(row) {
      try {
        const res = await getPurRequest(row.requestId)
        if (res.code === 200) {
          this.viewData = res.data
          if (!this.viewData.items) {
            this.viewData.items = []
          }
          this.viewDialogVisible = true
        }
      } catch (error) {
        this.$message.error('获取数据失败')
      }
    },

    handleDelete(row) {
      this.$confirm('确认删除该采购申请?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await delPurRequest(row.requestId)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 打开物料选择弹窗
    openItemSelect(index) {
      this.currentRowIndex = index
      this.itemDialogVisible = true
      this.itemQueryParams.keyword = ''
      this.fetchItemList()
    },

    // 查询物料列表
    async fetchItemList() {
      try {
        const res = await listItem(this.itemQueryParams)
        if (res.code === 200) {
          this.itemTableData = res.rows || []
          this.itemTotal = res.total || 0
        }
      } catch (error) {
        this.$message.error('获取物料列表失败')
      }
    },

    // 搜索物料
    handleItemQuery() {
      this.itemQueryParams.pageNum = 1
      this.fetchItemList()
    },

    // 物料分页
    handleItemSizeChange(val) {
      this.itemQueryParams.pageSize = val
      this.fetchItemList()
    },

    handleItemCurrentChange(val) {
      this.itemQueryParams.pageNum = val
      this.fetchItemList()
    },

    // 选择物料
    selectItem(row) {
      const item = this.form.items[this.currentRowIndex]
      item.itemId = row.itemId
      item.itemCode = row.itemCode
      item.itemName = row.itemName
      item.itemSpec = row.itemSpec || row.specification || ''
      item.unit = row.unit || row.unitName || ''
      this.itemDialogVisible = false
      this.$message.success('已选择物料：' + row.itemName)
    },

    // 提交审批
    handleSubmit(row) {
      this.$confirm('确认提交该采购申请进行审批?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          await submitPurRequest(row.requestId)
          this.$message.success('提交成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('提交失败')
        }
      })
    },

    // 审批通过
    handleApprove(row) {
      this.$prompt('请输入审批意见（可选）', '审批通过', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '审批通过'
      }).then(async ({ value }) => {
        try {
          await approvePurRequest(row.requestId, value)
          this.$message.success('审批通过')
          this.fetchData()
        } catch (error) {
          this.$message.error('审批失败')
        }
      }).catch(() => {})
    },

    // 审批拒绝
    handleReject(row) {
      this.$prompt('请输入拒绝原因', '审批拒绝', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入拒绝原因'
      }).then(async ({ value }) => {
        try {
          await rejectPurRequest(row.requestId, value)
          this.$message.success('已拒绝')
          this.fetchData()
        } catch (error) {
          this.$message.error('操作失败')
        }
      }).catch(() => {})
    },

    // 生成采购订单
    async handleGenerateOrder(row) {
      try {
        await this.$confirm('确定要根据该采购申请生成采购订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await createOrderFromRequest(row.requestId)
        this.$message.success('订单生成成功')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          const msg = error.response && error.response.data && error.response.data.msg
          this.$message.error(msg || '订单生成失败')
        }
      }
    },

    // 添加明细行
    handleAddItem() {
      if (!this.form.items) {
        this.form.items = []
      }
      this.form.items.push({
        itemId: null,
        requestId: null,
        itemCode: '',
        itemName: '',
        itemSpec: '',
        unit: '',
        quantity: 1,
        price: 0,
        amount: 0,
        requiredDate: null,
        remark: ''
      })
    },

    // 删除明细行
    handleDeleteItem(index) {
      this.form.items.splice(index, 1)
    },

    // 计算明细金额
    calculateItemAmount(item) {
      if (item.quantity && item.price) {
        item.amount = item.quantity * item.price
      } else {
        item.amount = 0
      }
    },

    submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          if (!this.form.items || this.form.items.length === 0) {
            this.$message.warning('请至少添加一条采购明细')
            return
          }
          try {
            // 自动计算总金额
            this.form.totalAmount = this.calculateTotal

            if (this.form.requestId) {
              await updatePurRequest(this.form)
              this.$message.success('更新成功')
            } else {
              await addPurRequest(this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error('操作失败')
          }
        }
      })
    },

    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },

    handleRowDblclick(row) {
      this.handleView(row)
    },

    formatMoney(value) {
      if (!value) return '0.00'
      return Number(value).toFixed(2)
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
      &.orange { color: #e6a23c; }
      &.green { color: #67c23a; }
    }
  }

  .stat-divider {
    width: 1px;
    height: 20px;
    background: #ebeef5;
    margin: 0 20px;
  }
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;
  }
}

.item-table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;

  .total-amount {
    font-size: 16px;
    font-weight: bold;
    color: #f56c6c;
  }
}

.no-mask-dialog {
  .el-dialog {
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3) !important;
  }
}
</style>
