<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header cal-header">
      <div class="title-section">
        <i class="el-icon-date"></i>
        <span class="title">排班计划</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增排班</el-button>
        <el-button type="success" icon="el-icon-document-copy" size="small" @click="handleBatchAdd">批量排班</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="queryParams" inline size="small">
        <el-form-item label="排班日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item label="班次">
          <el-select v-model="queryParams.shiftId" placeholder="请选择班次" clearable>
            <el-option v-for="item in shiftList" :key="item.shiftId" :label="item.shiftName" :value="item.shiftId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班组">
          <el-select v-model="queryParams.teamId" placeholder="请选择班组" clearable>
            <el-option v-for="item in teamList" :key="item.teamId" :label="item.teamName" :value="item.teamId" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" style="margin-top: 15px;">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="排班日期" prop="planDate" width="120" align="center" />
        <el-table-column label="班次" prop="shiftName" width="120" />
        <el-table-column label="班次编码" prop="shiftCode" width="100" />
        <el-table-column label="班组" prop="teamName" width="120" />
        <el-table-column label="班组编码" prop="teamCode" width="100" />
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag v-if="row.status === '0'" type="success" size="small">启用</el-tag>
            <el-tag v-else type="info" size="small">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" show-overflow-tooltip />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #f56c6c;" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNum"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-form-item label="排班日期" prop="planDate">
          <el-date-picker v-model="form.planDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="班次" prop="shiftId">
          <el-select v-model="form.shiftId" placeholder="请选择班次" style="width: 100%;" @change="handleShiftChange">
            <el-option v-for="item in shiftList" :key="item.shiftId" :label="item.shiftName" :value="item.shiftId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班组" prop="teamId">
          <el-select v-model="form.teamId" placeholder="请选择班组" style="width: 100%;" @change="handleTeamChange">
            <el-option v-for="item in teamList" :key="item.teamId" :label="item.teamName" :value="item.teamId" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="0">启用</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
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

    <!-- 批量排班对话框 -->
    <el-dialog title="批量排班" :visible.sync="batchDialogVisible" width="700px" :modal="false">
      <el-form ref="batchForm" :model="batchForm" :rules="batchRules" label-width="100px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="batchForm.startDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="batchForm.endDate" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="排班周期">
          <el-checkbox-group v-model="batchForm.weekDays">
            <el-checkbox :label="1">周一</el-checkbox>
            <el-checkbox :label="2">周二</el-checkbox>
            <el-checkbox :label="3">周三</el-checkbox>
            <el-checkbox :label="4">周四</el-checkbox>
            <el-checkbox :label="5">周五</el-checkbox>
            <el-checkbox :label="6">周六</el-checkbox>
            <el-checkbox :label="7">周日</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="班次" prop="shiftId">
          <el-select v-model="batchForm.shiftId" placeholder="请选择班次" style="width: 100%;" @change="handleBatchShiftChange">
            <el-option v-for="item in shiftList" :key="item.shiftId" :label="item.shiftName" :value="item.shiftId" />
          </el-select>
        </el-form-item>
        <el-form-item label="班组" prop="teamId">
          <el-select v-model="batchForm.teamId" placeholder="请选择班组" style="width: 100%;" @change="handleBatchTeamChange">
            <el-option v-for="item in teamList" :key="item.teamId" :label="item.teamName" :value="item.teamId" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="handleBatchSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPlan, addPlan, updatePlan, deletePlan, deletePlanBatch, batchGeneratePlan } from '@/api/cal'
import { listAllShift } from '@/api/cal'
import { listAllTeam } from '@/api/cal'

export default {
  name: 'CalPlan',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      total: 0,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        beginDate: '',
        endDate: '',
        shiftId: null,
        teamId: null
      },
      shiftList: [],
      teamList: [],
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        planId: null,
        planCode: '',
        planName: '',
        planDate: '',
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        teamId: null,
        teamCode: '',
        teamName: '',
        status: '0',
        remark: ''
      },
      rules: {
        planDate: [{ required: true, message: '请选择排班日期', trigger: 'change' }],
        shiftId: [{ required: true, message: '请选择班次', trigger: 'change' }],
        teamId: [{ required: true, message: '请选择班组', trigger: 'change' }]
      },
      batchDialogVisible: false,
      batchForm: {
        startDate: '',
        endDate: '',
        weekDays: [1, 2, 3, 4, 5],
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        teamId: null,
        teamCode: '',
        teamName: ''
      },
      batchRules: {
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
        shiftId: [{ required: true, message: '请选择班次', trigger: 'change' }],
        teamId: [{ required: true, message: '请选择班组', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
    this.loadShiftList()
    this.loadTeamList()
  },
  methods: {
    fetchData() {
      this.loading = true
      listPlan(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    loadShiftList() {
      listAllShift().then(res => {
        this.shiftList = res.data || []
      })
    },
    loadTeamList() {
      listAllTeam().then(res => {
        this.teamList = res.data || []
      })
    },
    handleDateRangeChange(val) {
      if (val) {
        this.queryParams.beginDate = val[0]
        this.queryParams.endDate = val[1]
      } else {
        this.queryParams.beginDate = ''
        this.queryParams.endDate = ''
      }
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        beginDate: '',
        endDate: '',
        shiftId: null,
        teamId: null
      }
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增排班'
      this.form = {
        planId: null,
        planCode: '',
        planName: '',
        planDate: '',
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        teamId: null,
        teamCode: '',
        teamName: '',
        status: '0',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleBatchAdd() {
      this.batchForm = {
        startDate: '',
        endDate: '',
        weekDays: [1, 2, 3, 4, 5],
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        teamId: null,
        teamCode: '',
        teamName: ''
      }
      this.batchDialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑排班'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该排班?', '提示', { type: 'warning' }).then(() => {
        deletePlan(row.planId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selectedRows.map(row => row.planId)
      this.$confirm('确认删除选中的 ' + ids.length + ' 个排班?', '提示', { type: 'warning' }).then(() => {
        deletePlanBatch(ids).then(() => {
          this.$message.success('批量删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleShiftChange(val) {
      const shift = this.shiftList.find(item => item.shiftId === val)
      if (shift) {
        this.form.shiftCode = shift.shiftCode
        this.form.shiftName = shift.shiftName
      }
    },
    handleTeamChange(val) {
      const team = this.teamList.find(item => item.teamId === val)
      if (team) {
        this.form.teamCode = team.teamCode
        this.form.teamName = team.teamName
      }
    },
    handleBatchShiftChange(val) {
      const shift = this.shiftList.find(item => item.shiftId === val)
      if (shift) {
        this.batchForm.shiftCode = shift.shiftCode
        this.batchForm.shiftName = shift.shiftName
      }
    },
    handleBatchTeamChange(val) {
      const team = this.teamList.find(item => item.teamId === val)
      if (team) {
        this.batchForm.teamCode = team.teamCode
        this.batchForm.teamName = team.teamName
      }
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        // 生成计划编码和名称
        if (!this.isEdit) {
          this.form.planCode = 'PLAN' + Date.now()
          this.form.planName = this.form.planDate + ' ' + this.form.shiftName + ' ' + this.form.teamName
        }
        const api = this.isEdit ? updatePlan : addPlan
        api(this.form).then(() => {
          this.$message.success(this.isEdit ? '修改成功' : '新增成功')
          this.dialogVisible = false
          this.fetchData()
        })
      })
    },
    handleBatchSubmit() {
      this.$refs.batchForm.validate(valid => {
        if (!valid) return
        // 生成排班列表
        const planList = []
        const start = new Date(this.batchForm.startDate)
        const end = new Date(this.batchForm.endDate)
        for (let d = new Date(start); d <= end; d.setDate(d.getDate() + 1)) {
          const weekDay = d.getDay() || 7
          if (this.batchForm.weekDays.includes(weekDay)) {
            const dateStr = d.toISOString().split('T')[0]
            planList.push({
              planCode: 'PLAN' + Date.now() + Math.random().toString(36).substr(2, 5),
              planName: dateStr + ' ' + this.batchForm.shiftName + ' ' + this.batchForm.teamName,
              planDate: dateStr,
              shiftId: this.batchForm.shiftId,
              shiftCode: this.batchForm.shiftCode,
              shiftName: this.batchForm.shiftName,
              teamId: this.batchForm.teamId,
              teamCode: this.batchForm.teamCode,
              teamName: this.batchForm.teamName,
              status: '0',
              remark: ''
            })
          }
        }
        if (planList.length === 0) {
          this.$message.warning('所选日期范围内没有符合条件的日期')
          return
        }
        batchGeneratePlan(planList).then(() => {
          this.$message.success('批量排班成功，共生成 ' + planList.length + ' 条记录')
          this.batchDialogVisible = false
          this.fetchData()
        })
      })
    },
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.cal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}
.pagination-wrap {
  margin-top: 15px;
  text-align: right;
}
</style>
