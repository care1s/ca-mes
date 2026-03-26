<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header cal-header">
      <div class="title-section">
        <i class="el-icon-time"></i>
        <span class="title">班次管理</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增班次</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="queryParams" inline size="small">
        <el-form-item label="班次编码">
          <el-input v-model="queryParams.shiftCode" placeholder="请输入班次编码" clearable />
        </el-form-item>
        <el-form-item label="班次名称">
          <el-input v-model="queryParams.shiftName" placeholder="请输入班次名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="0" />
            <el-option label="停用" value="1" />
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
        <el-table-column label="班次编码" prop="shiftCode" width="120" />
        <el-table-column label="班次名称" prop="shiftName" width="150" />
        <el-table-column label="开始时间" prop="startTime" width="100" align="center" />
        <el-table-column label="结束时间" prop="endTime" width="100" align="center" />
        <el-table-column label="休息时段" width="150" align="center">
          <template slot-scope="{row}">
            <span v-if="row.restStartTime && row.restEndTime">
              {{ row.restStartTime }} ~ {{ row.restEndTime }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="工作时长" prop="workHours" width="100" align="center">
          <template slot-scope="{row}">
            <span v-if="row.workHours">{{ row.workHours }} 小时</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
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
        <el-form-item label="班次编码" prop="shiftCode">
          <el-input v-model="form.shiftCode" placeholder="请输入班次编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="班次名称" prop="shiftName">
          <el-input v-model="form.shiftName" placeholder="请输入班次名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker v-model="form.startTime" placeholder="选择时间" value-format="HH:mm:ss" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-picker v-model="form.endTime" placeholder="选择时间" value-format="HH:mm:ss" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="休息开始">
              <el-time-picker v-model="form.restStartTime" placeholder="选择时间" value-format="HH:mm:ss" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="休息结束">
              <el-time-picker v-model="form.restEndTime" placeholder="选择时间" value-format="HH:mm:ss" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="工作时长">
          <el-input-number v-model="form.workHours" :min="0" :max="24" :precision="2" placeholder="小时" />
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
  </div>
</template>

<script>
import { listShift, addShift, updateShift, deleteShift, deleteShiftBatch } from '@/api/cal'

export default {
  name: 'CalShift',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        shiftCode: '',
        shiftName: '',
        status: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        startTime: '',
        endTime: '',
        restStartTime: '',
        restEndTime: '',
        workHours: 8,
        status: '0',
        remark: ''
      },
      rules: {
        shiftCode: [{ required: true, message: '请输入班次编码', trigger: 'blur' }],
        shiftName: [{ required: true, message: '请输入班次名称', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listShift(this.queryParams).then(res => {
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        shiftCode: '',
        shiftName: '',
        status: ''
      }
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增班次'
      this.form = {
        shiftId: null,
        shiftCode: '',
        shiftName: '',
        startTime: '',
        endTime: '',
        restStartTime: '',
        restEndTime: '',
        workHours: 8,
        status: '0',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑班次'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该班次?', '提示', { type: 'warning' }).then(() => {
        deleteShift(row.shiftId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selectedRows.map(row => row.shiftId)
      this.$confirm('确认删除选中的 ' + ids.length + ' 个班次?', '提示', { type: 'warning' }).then(() => {
        deleteShiftBatch(ids).then(() => {
          this.$message.success('批量删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const api = this.isEdit ? updateShift : addShift
        api(this.form).then(() => {
          this.$message.success(this.isEdit ? '修改成功' : '新增成功')
          this.dialogVisible = false
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
