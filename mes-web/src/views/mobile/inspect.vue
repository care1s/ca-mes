<template>
  <div class="mobile-container">
    <!-- 头部 -->
    <div class="mobile-header">
      <div class="header-title">
        <i class="el-icon-check"></i>
        <span>移动检验</span>
      </div>
      <div class="qc-tabs">
        <div
          v-for="type in qcTypes"
          :key="type.value"
          :class="['qc-tab', { active: currentQcType === type.value }]"
          @click="switchQcType(type.value)"
        >
          {{ type.label }}
        </div>
      </div>
    </div>

    <!-- 主要内容区 -->
    <div class="mobile-content">
      <!-- 模式切换 -->
      <div class="mode-tabs">
        <div
          :class="['mode-tab', { active: currentMode === 'scan' }]"
          @click="currentMode = 'scan'"
        >
          <i class="el-icon-full-screen"></i>
          <span>扫码检验</span>
        </div>
        <div
          :class="['mode-tab', { active: currentMode === 'pending' }]"
          @click="currentMode = 'pending'"
        >
          <i class="el-icon-tickets"></i>
          <span>待检列表</span>
        </div>
        <div
          :class="['mode-tab', { active: currentMode === 'my' }]"
          @click="currentMode = 'my'"
        >
          <i class="el-icon-user"></i>
          <span>我的检验</span>
        </div>
      </div>

      <!-- 扫码检验模式 -->
      <div v-if="currentMode === 'scan'" class="scan-mode">
        <!-- 扫码区域 -->
        <div class="scan-section" v-if="!scannedTask">
          <div class="scan-box" @click="handleScan">
            <i class="el-icon-full-screen"></i>
            <span>点击扫码或输入条码</span>
            <span class="scan-tip">支持工单码、物料码</span>
          </div>
          <el-input
            v-model="barcode"
            placeholder="请输入工单码/物料码"
            size="large"
            clearable
            @keyup.enter.native="handleQuery"
            class="barcode-input"
          >
            <el-button slot="append" type="primary" @click="handleQuery">查询</el-button>
          </el-input>
        </div>

        <!-- 任务信息卡片 -->
        <div class="task-card" v-if="scannedTask">
          <div class="card-header">
            <span class="task-code">{{ scannedTask.workorderCode }}</span>
            <el-tag size="medium">{{ currentQcType }}</el-tag>
          </div>
          <div class="card-body">
            <div class="info-row">
              <span class="label">物料：</span>
              <span class="value">{{ scannedTask.itemName }}</span>
            </div>
            <div class="info-row">
              <span class="label">规格：</span>
              <span class="value">{{ scannedTask.specification }}</span>
            </div>
            <div class="info-row">
              <span class="label">供应商：</span>
              <span class="value">{{ scannedTask.supplierName || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">计划数量：</span>
              <span class="value">{{ scannedTask.planQuantity }}</span>
            </div>
          </div>
        </div>

        <!-- 报检输入区 -->
        <div class="inspect-section" v-if="scannedTask">
          <div class="section-title">
            <i class="el-icon-edit-outline"></i>
            <span>报检数量</span>
          </div>
          <div class="quantity-input">
            <el-input-number
              v-model="inspectQty"
              :min="1"
              :precision="2"
              size="large"
              class="qty-input"
            ></el-input-number>
            <span class="unit">件</span>
          </div>

          <!-- 操作按钮 -->
          <div class="action-btns">
            <el-button type="info" size="large" @click="resetScan" plain>
              <i class="el-icon-refresh-left"></i> 重新扫码
            </el-button>
            <el-button type="success" size="large" @click="submitInspect">
              <i class="el-icon-check"></i> 提交报检
            </el-button>
          </div>
        </div>
      </div>

      <!-- 待检列表模式 -->
      <div v-if="currentMode === 'pending'" class="list-mode">
        <div v-if="pendingList.length === 0" class="empty-state">
          <i class="el-icon-document"></i>
          <span>暂无待检验任务</span>
        </div>
        <div v-else class="pending-list">
          <div
            v-for="item in pendingList"
            :key="item.recordId"
            class="pending-item"
            @click="startInspect(item.recordId)"
          >
            <div class="item-header">
              <span class="item-code">{{ item.recordCode }}</span>
              <el-tag :type="getStatusType(item.status)" size="small">
                {{ getStatusText(item.status) }}
              </el-tag>
            </div>
            <div class="item-body">
              <div class="item-row">
                <span class="label">工单：</span>
                <span class="value">{{ item.workorderCode }}</span>
              </div>
              <div class="item-row">
                <span class="label">物料：</span>
                <span class="value">{{ item.itemName }}</span>
              </div>
              <div class="item-row">
                <span class="label">报检数量：</span>
                <span class="value highlight">{{ item.inspectQuantity }}</span>
              </div>
              <div class="item-row" v-if="item.waitHours > 0">
                <span class="label">等待：</span>
                <span class="value warn">{{ item.waitHours }}小时</span>
              </div>
            </div>
            <div class="item-footer">
              <el-button type="primary" size="small" @click.stop="startInspect(item.recordId)">
                <i class="el-icon-edit"></i> 开始检验
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 我的检验模式 -->
      <div v-if="currentMode === 'my'" class="list-mode">
        <div v-if="myQcList.length === 0" class="empty-state">
          <i class="el-icon-document"></i>
          <span>暂无检验记录</span>
        </div>
        <div v-else class="qc-list">
          <div
            v-for="item in myQcList"
            :key="item.recordId"
            class="qc-item"
          >
            <div class="item-header">
              <span class="item-code">{{ item.recordCode }}</span>
              <el-tag :type="getResultType(item.result)" size="small">
                {{ getResultText(item.result) }}
              </el-tag>
            </div>
            <div class="item-body">
              <div class="item-row">
                <span class="label">工单：</span>
                <span class="value">{{ item.workorderCode }}</span>
              </div>
              <div class="item-row">
                <span class="label">物料：</span>
                <span class="value">{{ item.itemName }}</span>
              </div>
              <div class="item-row" v-if="item.checkedQuantity">
                <span class="label">检验结果：</span>
                <span class="value">
                  <span class="success">合格 {{ item.qualifiedQuantity || 0 }}</span>
                  <span v-if="item.defectiveQuantity > 0" class="danger"> / 不合格 {{ item.defectiveQuantity }}</span>
                </span>
              </div>
            </div>
            <div class="item-footer">
              <span class="time">{{ item.inspectTime }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 扫码输入对话框 -->
    <el-dialog
      title="请输入条码"
      :visible.sync="scanDialogVisible"
      width="90%"
      center
      :show-close="false"
    >
      <el-input
        v-model="dialogBarcode"
        placeholder="请扫描或输入条码"
        size="large"
        ref="scanInput"
        @keyup.enter.native="confirmScan"
      ></el-input>
      <div slot="footer">
        <el-button @click="scanDialogVisible = false" size="large">取消</el-button>
        <el-button type="primary" @click="confirmScan" size="large">确认</el-button>
      </div>
    </el-dialog>

    <!-- 检验结果录入对话框 -->
    <el-dialog
      title="录入检验结果"
      :visible.sync="resultDialogVisible"
      width="95%"
      center
      :show-close="false"
      :close-on-click-modal="false"
    >
      <div class="result-form" v-if="currentRecord">
        <div class="form-item">
          <label>合格数量</label>
          <el-input-number
            v-model="resultForm.qualifiedQty"
            :min="0"
            :max="currentRecord.inspectQuantity"
            :precision="2"
            size="large"
            class="full-width"
          ></el-input-number>
        </div>
        <div class="form-item">
          <label>不合格数量</label>
          <el-input-number
            v-model="resultForm.defectiveQty"
            :min="0"
            :max="currentRecord.inspectQuantity"
            :precision="2"
            size="large"
            class="full-width"
          ></el-input-number>
        </div>
        <div class="form-item" v-if="resultForm.defectiveQty > 0">
          <label>不良描述</label>
          <el-input
            v-model="resultForm.defectDesc"
            type="textarea"
            :rows="2"
            placeholder="请输入不良描述"
          ></el-input>
        </div>
        <div class="form-item" v-if="resultForm.defectiveQty > 0">
          <label>不良原因</label>
          <el-input
            v-model="resultForm.defectReason"
            placeholder="请输入不良原因"
          ></el-input>
        </div>
        <div class="form-item" v-if="resultForm.defectiveQty > 0">
          <label>处理措施</label>
          <el-select v-model="resultForm.handleMethod" placeholder="请选择处理措施" class="full-width">
            <el-option label="返工" value="返工"></el-option>
            <el-option label="返修" value="返修"></el-option>
            <el-option label="报废" value="报废"></el-option>
            <el-option label="特采" value="特采"></el-option>
            <el-option label="退货" value="退货"></el-option>
          </el-select>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="resultDialogVisible = false" size="large">取消</el-button>
        <el-button type="success" @click="submitResult" size="large">
          <i class="el-icon-check"></i> 提交结果
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  scanQcTask,
  scanInspect,
  getPendingQcList,
  getMyQcList,
  startInspect as apiStartInspect,
  submitQcResult
} from '@/api/mobile'

export default {
  name: 'MobileInspect',
  data() {
    return {
      qcTypes: [
        { label: '来料检验', value: 'IQC' },
        { label: '过程检验', value: 'IPQC' },
        { label: '出货检验', value: 'OQC' }
      ],
      currentQcType: 'IQC',
      currentMode: 'scan',
      barcode: '',
      dialogBarcode: '',
      scannedTask: null,
      inspectQty: 1,
      scanDialogVisible: false,
      resultDialogVisible: false,
      pendingList: [],
      myQcList: [],
      currentRecord: null,
      resultForm: {
        qualifiedQty: 0,
        defectiveQty: 0,
        defectDesc: '',
        defectReason: '',
        handleMethod: ''
      }
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.user || JSON.parse(localStorage.getItem('mes-user') || '{}')
    }
  },
  watch: {
    currentQcType() {
      if (this.currentMode === 'pending') {
        this.fetchPendingList()
      }
    },
    currentMode(val) {
      if (val === 'pending') {
        this.fetchPendingList()
      } else if (val === 'my') {
        this.fetchMyList()
      }
    }
  },
  mounted() {
    setTimeout(() => {
      this.handleScan()
    }, 500)
  },
  methods: {
    // 切换检验类型
    switchQcType(type) {
      this.currentQcType = type
      this.resetScan()
    },

    // 打开扫码对话框
    handleScan() {
      this.dialogBarcode = ''
      this.scanDialogVisible = true
      setTimeout(() => {
        this.$refs.scanInput && this.$refs.scanInput.focus()
      }, 100)
    },

    // 确认扫码
    confirmScan() {
      if (!this.dialogBarcode.trim()) {
        this.$message.warning('请输入条码')
        return
      }
      this.barcode = this.dialogBarcode
      this.scanDialogVisible = false
      this.queryTask()
    },

    // 查询任务
    handleQuery() {
      if (!this.barcode.trim()) {
        this.$message.warning('请输入条码')
        return
      }
      this.queryTask()
    },

    // 调用API查询任务
    async queryTask() {
      try {
        const res = await scanQcTask(this.barcode, this.currentQcType)
        if (res.code === 200 && res.data) {
          this.scannedTask = res.data
          this.inspectQty = this.scannedTask.planQuantity || 1
          this.$message.success('扫码成功')
        } else {
          this.$message.error(res.msg || '未找到对应任务')
          this.scannedTask = null
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
        this.scannedTask = null
      }
    },

    // 提交报检
    async submitInspect() {
      if (!this.scannedTask) {
        this.$message.warning('请先扫描条码')
        return
      }
      if (!this.inspectQty || this.inspectQty <= 0) {
        this.$message.warning('请输入报检数量')
        return
      }

      try {
        const data = {
          barcode: this.barcode,
          qcType: this.currentQcType,
          quantity: this.inspectQty,
          inspectorId: this.userInfo.userId,
          inspectorName: this.userInfo.userName
        }
        const res = await scanInspect(data)
        if (res.code === 200) {
          this.$message.success('报检成功')
          this.resetScan()
          // 切换到待检列表
          this.currentMode = 'pending'
        } else {
          this.$message.error(res.msg || '报检失败')
        }
      } catch (error) {
        this.$message.error('报检失败：' + error.message)
      }
    },

    // 获取待检列表
    async fetchPendingList() {
      try {
        const res = await getPendingQcList(this.currentQcType, this.userInfo.userId)
        if (res.code === 200) {
          this.pendingList = res.data || []
        }
      } catch (error) {
        console.error('获取待检列表失败:', error)
      }
    },

    // 获取我的检验列表
    async fetchMyList() {
      try {
        const res = await getMyQcList(this.userInfo.userId)
        if (res.code === 200) {
          this.myQcList = res.data || []
        }
      } catch (error) {
        console.error('获取我的检验列表失败:', error)
      }
    },

    // 开始检验
    async startInspect(recordId) {
      try {
        const res = await apiStartInspect(recordId)
        if (res.code === 200) {
          // 找到当前记录
          const record = this.pendingList.find(item => item.recordId === recordId)
          if (record) {
            this.currentRecord = record
            // 初始化结果表单
            this.resultForm = {
              qualifiedQty: record.inspectQuantity,
              defectiveQty: 0,
              defectDesc: '',
              defectReason: '',
              handleMethod: ''
            }
            this.resultDialogVisible = true
          }
        }
      } catch (error) {
        this.$message.error('开始检验失败：' + error.message)
      }
    },

    // 提交检验结果
    async submitResult() {
      const totalQty = this.resultForm.qualifiedQty + this.resultForm.defectiveQty
      if (totalQty !== this.currentRecord.inspectQuantity) {
        this.$message.warning(`检验数量(${totalQty})不等于报检数量(${this.currentRecord.inspectQuantity})`)
        return
      }

      if (this.resultForm.defectiveQty > 0 && !this.resultForm.defectDesc) {
        this.$message.warning('请输入不良描述')
        return
      }

      try {
        const data = {
          recordId: this.currentRecord.recordId,
          qualifiedQty: this.resultForm.qualifiedQty,
          defectiveQty: this.resultForm.defectiveQty,
          defectDesc: this.resultForm.defectDesc,
          defectReason: this.resultForm.defectReason,
          handleMethod: this.resultForm.handleMethod
        }
        const res = await submitQcResult(data)
        if (res.code === 200) {
          this.$message.success('检验结果提交成功')
          this.resultDialogVisible = false
          this.fetchPendingList()
        } else {
          this.$message.error(res.msg || '提交失败')
        }
      } catch (error) {
        this.$message.error('提交失败：' + error.message)
      }
    },

    // 重新扫码
    resetScan() {
      this.scannedTask = null
      this.barcode = ''
      this.inspectQty = 1
      this.handleScan()
    },

    // 状态样式
    getStatusType(status) {
      const map = {
        'PENDING': 'info',
        'PROCESSING': 'warning',
        'COMPLETED': 'success'
      }
      return map[status] || 'info'
    },

    // 状态文本
    getStatusText(status) {
      const map = {
        'PENDING': '待检验',
        'PROCESSING': '检验中',
        'COMPLETED': '已完成'
      }
      return map[status] || status
    },

    // 结果样式
    getResultType(result) {
      const map = {
        'PASS': 'success',
        'FAIL': 'danger',
        'PENDING': 'info'
      }
      return map[result] || 'info'
    },

    // 结果文本
    getResultText(result) {
      const map = {
        'PASS': '合格',
        'FAIL': '不合格',
        'PENDING': '待检'
      }
      return map[result] || result
    }
  }
}
</script>

<style lang="scss" scoped>
.mobile-container {
  min-height: 100vh;
  background: #f5f7fa;
}

// 头部
.mobile-header {
  padding: 12px 16px;
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: #fff;

  .header-title {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 12px;

    i {
      margin-right: 8px;
      font-size: 20px;
    }
  }

  .qc-tabs {
    display: flex;
    gap: 8px;

    .qc-tab {
      flex: 1;
      text-align: center;
      padding: 8px 0;
      border-radius: 20px;
      font-size: 14px;
      background: rgba(255,255,255,0.2);
      cursor: pointer;
      transition: all 0.3s;

      &.active {
        background: #fff;
        color: #11998e;
        font-weight: 600;
      }
    }
  }
}

// 内容区
.mobile-content {
  padding: 16px;
}

// 模式切换
.mode-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  background: #fff;
  border-radius: 12px;
  padding: 8px;

  .mode-tab {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 12px 0;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;

    i {
      font-size: 24px;
      margin-bottom: 4px;
      color: #909399;
    }

    span {
      font-size: 12px;
      color: #606266;
    }

    &.active {
      background: #e6f7ff;

      i, span {
        color: #409eff;
      }
    }
  }
}

// 扫码区域
.scan-section {
  margin-bottom: 16px;

  .scan-box {
    background: #fff;
    border: 2px dashed #dcdfe6;
    border-radius: 12px;
    padding: 40px 20px;
    text-align: center;
    margin-bottom: 16px;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      border-color: #409eff;
      background: #f5f7fa;
    }

    i {
      font-size: 48px;
      color: #409eff;
      margin-bottom: 12px;
    }

    span {
      display: block;
      font-size: 16px;
      color: #606266;

      &.scan-tip {
        font-size: 12px;
        color: #909399;
        margin-top: 8px;
      }
    }
  }

  .barcode-input {
    ::v-deep .el-input__inner {
      font-size: 16px;
      height: 48px;
    }

    ::v-deep .el-button {
      height: 48px;
      padding: 0 24px;
    }
  }
}

// 任务卡片
.task-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #ebeef5;

    .task-code {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .card-body {
    .info-row {
      display: flex;
      margin-bottom: 8px;
      font-size: 14px;

      .label {
        color: #909399;
        width: 70px;
        flex-shrink: 0;
      }

      .value {
        color: #303133;
        flex: 1;
      }
    }
  }
}

// 报检区域
.inspect-section {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);

  .section-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 16px;

    i {
      margin-right: 8px;
      color: #409eff;
      font-size: 18px;
    }
  }

  .quantity-input {
    display: flex;
    align-items: center;
    margin-bottom: 20px;

    .qty-input {
      flex: 1;

      ::v-deep .el-input__inner {
        font-size: 28px;
        font-weight: 600;
        text-align: center;
        height: 60px;
      }
    }

    .unit {
      margin-left: 12px;
      font-size: 18px;
      color: #606266;
    }
  }

  .action-btns {
    display: flex;
    gap: 12px;

    .el-button {
      flex: 1;
      height: 48px;
      font-size: 16px;

      i {
        margin-right: 4px;
      }
    }
  }
}

// 列表模式
.list-mode {
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 60px 20px;
    color: #909399;

    i {
      font-size: 48px;
      margin-bottom: 16px;
    }

    span {
      font-size: 14px;
    }
  }

  .pending-list, .qc-list {
    .pending-item, .qc-item {
      background: #fff;
      border-radius: 12px;
      padding: 16px;
      margin-bottom: 12px;
      box-shadow: 0 2px 12px rgba(0,0,0,0.06);
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 16px rgba(0,0,0,0.1);
      }

      .item-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
        padding-bottom: 12px;
        border-bottom: 1px solid #ebeef5;

        .item-code {
          font-size: 15px;
          font-weight: 600;
          color: #303133;
        }
      }

      .item-body {
        .item-row {
          display: flex;
          margin-bottom: 6px;
          font-size: 13px;

          .label {
            color: #909399;
            width: 70px;
            flex-shrink: 0;
          }

          .value {
            color: #606266;
            flex: 1;

            &.highlight {
              color: #409eff;
              font-weight: 600;
            }

            &.warn {
              color: #e6a23c;
            }

            .success {
              color: #67c23a;
            }

            .danger {
              color: #f56c6c;
            }
          }
        }
      }

      .item-footer {
        margin-top: 12px;
        padding-top: 12px;
        border-top: 1px solid #ebeef5;
        display: flex;
        justify-content: flex-end;

        .time {
          font-size: 12px;
          color: #909399;
        }
      }
    }
  }
}

// 结果表单
.result-form {
  .form-item {
    margin-bottom: 16px;

    label {
      display: block;
      font-size: 14px;
      color: #606266;
      margin-bottom: 8px;
    }

    .full-width {
      width: 100%;

      ::v-deep .el-input__inner {
        font-size: 18px;
        height: 48px;
      }
    }
  }
}
</style>
