<template>
  <div class="mobile-container">
    <!-- 头部 -->
    <div class="mobile-header">
      <div class="header-title">
        <i class="el-icon-full-screen"></i>
        <span>扫码报工</span>
      </div>
      <div class="user-info" v-if="userInfo">
        <i class="el-icon-user"></i>
        <span>{{ userInfo.userName }}</span>
      </div>
    </div>

    <!-- 主要内容区 -->
    <div class="mobile-content">
      <!-- 扫码区域 -->
      <div class="scan-section" v-if="!scannedTask">
        <div class="scan-box" @click="handleScan">
          <i class="el-icon-full-screen"></i>
          <span>点击扫码或输入条码</span>
        </div>
        <el-input
          v-model="barcode"
          placeholder="请输入工单码/任务码/工序码"
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
          <span class="task-code">{{ scannedTask.taskCode }}</span>
          <el-tag :type="getStatusType(scannedTask.status)" size="medium">
            {{ getStatusText(scannedTask.status) }}
          </el-tag>
        </div>
        <div class="card-body">
          <div class="info-row">
            <span class="label">工单：</span>
            <span class="value">{{ scannedTask.workorderCode }}</span>
          </div>
          <div class="info-row">
            <span class="label">物料：</span>
            <span class="value">{{ scannedTask.itemName }}</span>
          </div>
          <div class="info-row">
            <span class="label">规格：</span>
            <span class="value">{{ scannedTask.specification }}</span>
          </div>
          <div class="info-row">
            <span class="label">工序：</span>
            <span class="value">{{ scannedTask.processName }}</span>
          </div>
          <div class="info-row">
            <span class="label">工作站：</span>
            <span class="value">{{ scannedTask.workstationName }}</span>
          </div>
          <div class="progress-row">
            <div class="progress-info">
              <span>进度：{{ scannedTask.completedQuantity || 0 }} / {{ scannedTask.planQuantity }}</span>
              <span class="progress-percent">{{ scannedTask.progress || 0 }}%</span>
            </div>
            <el-progress :percentage="scannedTask.progress || 0" :color="progressColors"></el-progress>
          </div>
        </div>
      </div>

      <!-- 报工输入区 -->
      <div class="feedback-section" v-if="scannedTask">
        <div class="section-title">
          <i class="el-icon-edit-outline"></i>
          <span>本次报工数量</span>
        </div>
        <div class="quantity-input">
          <el-input-number
            v-model="feedbackQty"
            :min="0.01"
            :max="maxQty"
            :precision="2"
            :step="1"
            size="large"
            class="qty-input"
          ></el-input-number>
          <span class="unit">件</span>
        </div>
        <div class="remain-info">
          剩余可报工：<span class="remain-qty">{{ remainQty }}</span> 件
        </div>

        <!-- 快捷数量按钮 -->
        <div class="quick-btns">
          <el-button
            v-for="qty in quickQtys"
            :key="qty"
            size="medium"
            @click="feedbackQty = qty"
            :disabled="qty > remainQty"
          >
            {{ qty }}
          </el-button>
          <el-button size="medium" @click="feedbackQty = remainQty" type="warning">全部</el-button>
        </div>

        <!-- 操作按钮 -->
        <div class="action-btns">
          <el-button type="info" size="large" @click="resetScan" plain class="btn-reset">
            <i class="el-icon-refresh-left"></i> 重新扫码
          </el-button>
          <el-button type="success" size="large" @click="submitFeedback" class="btn-submit">
            <i class="el-icon-check"></i> 确认报工
          </el-button>
        </div>
      </div>

      <!-- 最近报工记录 -->
      <div class="recent-section" v-if="recentFeedbacks.length > 0">
        <div class="section-title">
          <i class="el-icon-time"></i>
          <span>最近报工记录</span>
        </div>
        <div class="recent-list">
          <div
            v-for="item in recentFeedbacks"
            :key="item.id"
            class="recent-item"
          >
            <div class="item-main">
              <span class="item-code">{{ item.taskCode }}</span>
              <span class="item-qty">+{{ item.quantity }}</span>
            </div>
            <div class="item-time">{{ item.time }}</div>
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

    <!-- 报工成功提示 -->
    <el-dialog
      title="报工成功"
      :visible.sync="successDialogVisible"
      width="90%"
      center
      :show-close="false"
      :close-on-click-modal="false"
    >
      <div class="success-content">
        <i class="el-icon-success success-icon"></i>
        <div class="success-title">报工提交成功！</div>
        <div class="success-detail">
          <div>本次报工：<span class="highlight">{{ feedbackQty }}</span> 件</div>
          <div>累计完成：<span class="highlight">{{ completedQty }}</span> / {{ scannedTask && scannedTask.planQuantity }} 件</div>
          <div>完成进度：<span class="highlight">{{ progress }}%</span></div>
        </div>
      </div>
      <div slot="footer">
        <el-button type="primary" @click="continueFeedback" size="large" class="btn-full">继续报工</el-button>
        <el-button @click="finishFeedback" size="large" class="btn-full">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { scanTask, scanFeedback } from '@/api/mobile'

export default {
  name: 'MobileFeedback',
  data() {
    return {
      barcode: '',
      dialogBarcode: '',
      scannedTask: null,
      feedbackQty: 1,
      scanDialogVisible: false,
      successDialogVisible: false,
      completedQty: 0,
      progress: 0,
      recentFeedbacks: [],
      quickQtys: [1, 5, 10, 20, 50, 100],
      progressColors: [
        { color: '#f56c6c', percentage: 20 },
        { color: '#e6a23c', percentage: 50 },
        { color: '#67c23a', percentage: 80 },
        { color: '#409eff', percentage: 100 }
      ]
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.user || JSON.parse(localStorage.getItem('mes-user') || '{}')
    },
    maxQty() {
      if (!this.scannedTask) return 0
      const planQty = this.scannedTask.planQuantity || 0
      const completedQty = this.scannedTask.completedQuantity || 0
      return Math.max(0, planQty - completedQty)
    },
    remainQty() {
      return this.maxQty.toFixed(2)
    }
  },
  mounted() {
    // 自动聚焦到扫码输入
    setTimeout(() => {
      this.handleScan()
    }, 500)
  },
  methods: {
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
        const res = await scanTask(this.barcode)
        if (res.code === 200 && res.data) {
          this.scannedTask = res.data
          this.feedbackQty = Math.min(1, this.maxQty)
          this.$message.success('扫码成功，请确认任务信息后报工')
        } else {
          this.$message.error(res.msg || '未找到对应任务')
          this.scannedTask = null
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
        this.scannedTask = null
      }
    },

    // 提交报工
    async submitFeedback() {
      if (!this.scannedTask) {
        this.$message.warning('请先扫描任务条码')
        return
      }
      if (!this.feedbackQty || this.feedbackQty <= 0) {
        this.$message.warning('请输入报工数量')
        return
      }
      if (this.feedbackQty > this.maxQty) {
        this.$message.warning('报工数量不能超过剩余数量')
        return
      }

      try {
        const data = {
          barcode: this.barcode,
          quantity: this.feedbackQty,
          operatorId: this.userInfo.userId,
          operatorName: this.userInfo.userName
        }
        const res = await scanFeedback(data)
        if (res.code === 200) {
          this.completedQty = res.data.completedQuantity
          this.progress = res.data.progress
          this.successDialogVisible = true

          // 添加到最近记录
          this.recentFeedbacks.unshift({
            id: Date.now(),
            taskCode: this.scannedTask.taskCode,
            quantity: this.feedbackQty,
            time: new Date().toLocaleString()
          })
          if (this.recentFeedbacks.length > 5) {
            this.recentFeedbacks.pop()
          }
        } else {
          this.$message.error(res.msg || '报工失败')
        }
      } catch (error) {
        this.$message.error('报工失败：' + error.message)
      }
    },

    // 继续报工
    continueFeedback() {
      this.successDialogVisible = false
      // 更新任务信息
      if (this.scannedTask) {
        this.scannedTask.completedQuantity = this.completedQty
        this.scannedTask.progress = this.progress
      }
      this.feedbackQty = Math.min(1, this.maxQty)
    },

    // 完成报工
    finishFeedback() {
      this.successDialogVisible = false
      this.resetScan()
    },

    // 重新扫码
    resetScan() {
      this.scannedTask = null
      this.barcode = ''
      this.feedbackQty = 1
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
        'PENDING': '待执行',
        'PROCESSING': '执行中',
        'COMPLETED': '已完成'
      }
      return map[status] || status
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;

  .header-title {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: 600;

    i {
      margin-right: 8px;
      font-size: 20px;
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    font-size: 14px;
    opacity: 0.9;

    i {
      margin-right: 4px;
    }
  }
}

// 内容区
.mobile-content {
  padding: 16px;
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

    .progress-row {
      margin-top: 12px;
      padding-top: 12px;
      border-top: 1px solid #ebeef5;

      .progress-info {
        display: flex;
        justify-content: space-between;
        font-size: 13px;
        color: #606266;
        margin-bottom: 8px;

        .progress-percent {
          color: #409eff;
          font-weight: 600;
        }
      }
    }
  }
}

// 报工区域
.feedback-section {
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
    margin-bottom: 12px;

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

  .remain-info {
    font-size: 14px;
    color: #909399;
    text-align: center;
    margin-bottom: 16px;

    .remain-qty {
      color: #f56c6c;
      font-weight: 600;
    }
  }

  .quick-btns {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 20px;

    .el-button {
      flex: 1;
      min-width: 60px;
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

    .btn-submit {
      font-weight: 600;
    }
  }
}

// 最近记录
.recent-section {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);

  .section-title {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 12px;

    i {
      margin-right: 8px;
      color: #909399;
      font-size: 18px;
    }
  }

  .recent-list {
    .recent-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #ebeef5;

      &:last-child {
        border-bottom: none;
      }

      .item-main {
        display: flex;
        align-items: center;

        .item-code {
          font-size: 14px;
          color: #303133;
          margin-right: 12px;
        }

        .item-qty {
          font-size: 14px;
          color: #67c23a;
          font-weight: 600;
        }
      }

      .item-time {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

// 成功提示
.success-content {
  text-align: center;
  padding: 20px;

  .success-icon {
    font-size: 64px;
    color: #67c23a;
    margin-bottom: 16px;
  }

  .success-title {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
    margin-bottom: 20px;
  }

  .success-detail {
    font-size: 14px;
    color: #606266;
    line-height: 2;

    .highlight {
      color: #409eff;
      font-weight: 600;
      font-size: 16px;
    }
  }
}

.btn-full {
  width: 100%;
  margin-left: 0 !important;
  margin-top: 8px;
}
</style>
