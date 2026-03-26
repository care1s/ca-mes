<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header cal-header">
      <div class="title-section">
        <i class="el-icon-s-custom"></i>
        <span class="title">班组管理</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增班组</el-button>
        <el-button type="danger" icon="el-icon-delete" size="small" :disabled="selectedRows.length === 0" @click="handleBatchDelete">批量删除</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="filter-card" shadow="never">
      <el-form :model="queryParams" inline size="small">
        <el-form-item label="班组编码">
          <el-input v-model="queryParams.teamCode" placeholder="请输入班组编码" clearable />
        </el-form-item>
        <el-form-item label="班组名称">
          <el-input v-model="queryParams.teamName" placeholder="请输入班组名称" clearable />
        </el-form-item>
        <el-form-item label="所属车间">
          <el-input v-model="queryParams.workshopName" placeholder="请输入车间名称" clearable />
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
        <el-table-column label="班组编码" prop="teamCode" width="120" />
        <el-table-column label="班组名称" prop="teamName" width="150" />
        <el-table-column label="所属车间" prop="workshopName" width="150" />
        <el-table-column label="班组长" prop="leaderName" width="120" />
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
        <el-form-item label="班组编码" prop="teamCode">
          <el-input v-model="form.teamCode" placeholder="请输入班组编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="班组名称" prop="teamName">
          <el-input v-model="form.teamName" placeholder="请输入班组名称" />
        </el-form-item>
        <el-form-item label="所属车间">
          <el-input v-model="form.workshopName" placeholder="请输入所属车间" />
        </el-form-item>
        <el-form-item label="班组长">
          <el-input v-model="form.leaderName" placeholder="请输入班组长姓名" />
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
import { listTeam, addTeam, updateTeam, deleteTeam, deleteTeamBatch } from '@/api/cal'

export default {
  name: 'CalTeam',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        teamCode: '',
        teamName: '',
        workshopName: '',
        status: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      form: {
        teamId: null,
        teamCode: '',
        teamName: '',
        workshopId: null,
        workshopName: '',
        leaderId: null,
        leaderName: '',
        status: '0',
        remark: ''
      },
      rules: {
        teamCode: [{ required: true, message: '请输入班组编码', trigger: 'blur' }],
        teamName: [{ required: true, message: '请输入班组名称', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listTeam(this.queryParams).then(res => {
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
        teamCode: '',
        teamName: '',
        workshopName: '',
        status: ''
      }
      this.fetchData()
    },
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增班组'
      this.form = {
        teamId: null,
        teamCode: '',
        teamName: '',
        workshopId: null,
        workshopName: '',
        leaderId: null,
        leaderName: '',
        status: '0',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑班组'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该班组?', '提示', { type: 'warning' }).then(() => {
        deleteTeam(row.teamId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.selectedRows.map(row => row.teamId)
      this.$confirm('确认删除选中的 ' + ids.length + ' 个班组?', '提示', { type: 'warning' }).then(() => {
        deleteTeamBatch(ids).then(() => {
          this.$message.success('批量删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const api = this.isEdit ? updateTeam : addTeam
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
