<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header cal-header">
      <div class="title-section">
        <i class="el-icon-date"></i>
        <span class="title">日历管理</span>
      </div>
      <div class="action-section">
        <el-select v-model="currentYear" size="small" style="width: 100px; margin-right: 10px;" @change="handleYearChange">
          <el-option v-for="year in yearOptions" :key="year" :label="year + '年'" :value="year" />
        </el-select>
        <el-button type="primary" icon="el-icon-refresh" size="small" @click="handleGenerate">生成日历</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 日历展示 -->
    <el-card shadow="never" style="margin-top: 15px;">
      <div class="calendar-header">
        <span class="year-month">{{ currentYear }}年</span>
        <div class="legend">
          <span class="legend-item"><span class="dot workday"></span>工作日</span>
          <span class="legend-item"><span class="dot restday"></span>休息日</span>
          <span class="legend-item"><span class="dot holiday"></span>节假日</span>
        </div>
      </div>

      <el-calendar v-model="calendarValue">
        <template slot="dateCell" slot-scope="{date, data}">
          <div :class="['calendar-cell', getCellClass(date)]">
            <div class="date-num">{{ data.day.split('-')[2] }}</div>
            <div v-if="getCalendarInfo(date).holidayName" class="holiday-name">
              {{ getCalendarInfo(date).holidayName }}
            </div>
            <div v-else class="day-type">
              {{ getCalendarInfo(date).isWorkday === 'N' ? '休' : '班' }}
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑日历" :visible.sync="dialogVisible" width="500px" :modal="false">
      <el-form ref="form" :model="form" label-width="100px" size="small">
        <el-form-item label="日期">
          <span>{{ form.calendarDate }}</span>
        </el-form-item>
        <el-form-item label="是否工作日">
          <el-radio-group v-model="form.isWorkday">
            <el-radio label="Y">是</el-radio>
            <el-radio label="N">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="节假日名称">
          <el-input v-model="form.holidayName" placeholder="请输入节假日名称" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCalendar, updateCalendar, generateYearCalendar } from '@/api/cal'

export default {
  name: 'CalCalendar',
  data() {
    return {
      currentYear: new Date().getFullYear(),
      yearOptions: [],
      calendarValue: new Date(),
      calendarData: [],
      dialogVisible: false,
      form: {
        calendarId: null,
        calendarDate: '',
        isWorkday: 'Y',
        holidayName: '',
        remark: ''
      }
    }
  },
  created() {
    this.initYearOptions()
    this.fetchData()
  },
  methods: {
    initYearOptions() {
      const currentYear = new Date().getFullYear()
      this.yearOptions = [currentYear - 1, currentYear, currentYear + 1, currentYear + 2]
    },
    fetchData() {
      const params = {
        year: this.currentYear,
        pageNum: 1,
        pageSize: 500
      }
      listCalendar(params).then(res => {
        this.calendarData = res.rows || []
      })
    },
    handleYearChange() {
      this.fetchData()
    },
    handleGenerate() {
      this.$confirm('生成 ' + this.currentYear + ' 年度日历，将覆盖已有数据?', '提示', { type: 'warning' }).then(() => {
        generateYearCalendar(this.currentYear).then(() => {
          this.$message.success('生成成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    getCellClass(date) {
      const info = this.getCalendarInfo(date)
      if (info.holidayName) return 'is-holiday'
      if (info.isWorkday === 'N') return 'is-restday'
      return 'is-workday'
    },
    getCalendarInfo(date) {
      const dateStr = date.toISOString().split('T')[0]
      return this.calendarData.find(item => item.calendarDate === dateStr) || { isWorkday: 'Y', holidayName: '' }
    },
    handleDateClick(date) {
      const dateStr = date.toISOString().split('T')[0]
      const info = this.getCalendarInfo(date)
      this.form = {
        calendarId: info.calendarId,
        calendarDate: dateStr,
        isWorkday: info.isWorkday || 'Y',
        holidayName: info.holidayName || '',
        remark: info.remark || ''
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      updateCalendar(this.form).then(() => {
        this.$message.success('修改成功')
        this.dialogVisible = false
        this.fetchData()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  .year-month {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
  }
  .legend {
    display: flex;
    gap: 20px;
    .legend-item {
      display: flex;
      align-items: center;
      gap: 5px;
      font-size: 13px;
      color: #606266;
      .dot {
        width: 10px;
        height: 10px;
        border-radius: 50%;
        &.workday { background-color: #67c23a; }
        &.restday { background-color: #909399; }
        &.holiday { background-color: #f56c6c; }
      }
    }
  }
}
.calendar-cell {
  height: 100%;
  padding: 5px;
  text-align: center;
  .date-num {
    font-size: 16px;
    font-weight: 500;
  }
  .holiday-name {
    font-size: 12px;
    color: #f56c6c;
    margin-top: 5px;
  }
  .day-type {
    font-size: 12px;
    color: #909399;
    margin-top: 5px;
  }
  &.is-holiday {
    background-color: #fef0f0;
    .date-num { color: #f56c6c; }
  }
  &.is-restday {
    background-color: #f4f4f5;
    .date-num { color: #909399; }
  }
  &.is-workday {
    background-color: #f0f9ff;
    .date-num { color: #67c23a; }
  }
}
</style>
