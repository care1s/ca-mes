<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-user"></i>
        <span class="title">用户管理</span>
        <span class="subtitle">User Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">用户总数</span>
        <span class="stat-value blue">{{ stats.total }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">正常</span>
        <span class="stat-value green">{{ stats.active }}</span>
      </div>
      <div class="stat-divider"></div>
      <div class="stat-item">
        <span class="stat-label">停用</span>
        <span class="stat-value red">{{ stats.inactive }}</span>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="queryParams.realName" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="0" />
            <el-option label="停用" :value="1" />
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
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="nickname" label="昵称" width="100" />
        <el-table-column prop="realName" label="真实姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="70" align="center">
          <template slot-scope="scope">
            {{ scope.row.gender === 0 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="150" show-overflow-tooltip />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="post" label="岗位" width="100" />
        <el-table-column prop="userType" label="类型" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.userType === 1 ? 'danger' : 'info'" size="small">
              {{ scope.row.userType === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 0 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleResetPwd(scope.row)">重置密码</el-button>
            <el-button type="text" size="small" @click="handleAssignRole(scope.row)">分配角色</el-button>
            <el-button 
              type="text" 
              size="small" 
              :style="{ color: scope.row.status === 0 ? '#f56c6c' : '#67c23a' }"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.status === 0 ? '停用' : '启用' }}
            </el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="请输入用户名" :disabled="!!form.userId" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password" v-if="!form.userId">
              <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入真实姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio :label="0">男</el-radio>
                <el-radio :label="1">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户类型" prop="userType">
              <el-select v-model="form.userType" placeholder="请选择用户类型" style="width: 100%">
                <el-option label="普通用户" :value="0" />
                <el-option label="管理员" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门" prop="deptName">
              <el-input v-model="form.deptName" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="岗位" prop="post">
              <el-input v-model="form.post" placeholder="请输入岗位" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 重置密码对话框 -->
    <el-dialog title="重置密码" :visible.sync="resetPwdVisible" width="400px" :close-on-click-modal="false">
      <el-form ref="resetPwdForm" :model="resetPwdForm" :rules="resetPwdRules" label-width="100px">
        <el-form-item label="新密码" prop="password">
          <el-input v-model="resetPwdForm.password" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="resetPwdForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPwdVisible = false">取消</el-button>
        <el-button type="primary" @click="submitResetPwd">确定</el-button>
      </div>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <el-dialog title="分配角色" :visible.sync="assignRoleVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="用户">
          <span>{{ currentUser.username }} - {{ currentUser.realName }}</span>
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignRoleVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignRole">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  listSysUser, 
  addSysUser, 
  updateSysUser, 
  delSysUser, 
  resetSysUserPwd,
  listSysRole,
  getRolesByUserId
} from '@/api/md'

export default {
  name: 'SysUser',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        username: '',
        realName: '',
        phone: '',
        status: null
      },
      stats: {
        total: 0,
        active: 0,
        inactive: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      form: {
        userId: null,
        username: '',
        password: '',
        nickname: '',
        realName: '',
        gender: 0,
        phone: '',
        email: '',
        deptName: '',
        post: '',
        userType: 0,
        remark: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }]
      },
      resetPwdVisible: false,
      resetPwdForm: {
        userId: null,
        password: '',
        confirmPassword: ''
      },
      resetPwdRules: {
        password: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请再次输入新密码', trigger: 'blur' }]
      },
      assignRoleVisible: false,
      currentUser: {},
      roleList: [],
      selectedRoles: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listSysUser(this.queryParams).then(response => {
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
      this.stats.active = this.tableData.filter(item => item.status === 0).length
      this.stats.inactive = this.tableData.filter(item => item.status === 1).length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        username: '',
        realName: '',
        phone: '',
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
      this.dialogTitle = '新增用户'
      this.form = {
        userId: null,
        username: '',
        password: '',
        nickname: '',
        realName: '',
        gender: 0,
        phone: '',
        email: '',
        deptName: '',
        post: '',
        userType: 0,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除用户 "${row.username}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSysUser(row.userId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleToggleStatus(row) {
      const newStatus = row.status === 0 ? 1 : 0
      const action = newStatus === 0 ? '启用' : '停用'
      this.$confirm(`确认${action}用户 "${row.username}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateSysUser({ userId: row.userId, status: newStatus }).then(() => {
          this.$message.success(`${action}成功`)
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleResetPwd(row) {
      this.resetPwdForm = {
        userId: row.userId,
        password: '',
        confirmPassword: ''
      }
      this.resetPwdVisible = true
    },
    submitResetPwd() {
      this.$refs.resetPwdForm.validate(valid => {
        if (valid) {
          if (this.resetPwdForm.password !== this.resetPwdForm.confirmPassword) {
            this.$message.error('两次输入的密码不一致')
            return
          }
          resetSysUserPwd(this.resetPwdForm.userId, this.resetPwdForm.password).then(() => {
            this.$message.success('密码重置成功')
            this.resetPwdVisible = false
          })
        }
      })
    },
    handleAssignRole(row) {
      this.currentUser = row
      this.selectedRoles = []
      // 获取所有角色
      listSysRole({ pageNum: 1, pageSize: 100 }).then(response => {
        this.roleList = response.rows || []
        // 获取用户当前角色
        getRolesByUserId(row.userId).then(res => {
          const userRoles = res.data || []
          this.selectedRoles = userRoles.map(r => r.roleId)
          this.assignRoleVisible = true
        })
      })
    },
    submitAssignRole() {
      this.$message.success('角色分配成功')
      this.assignRoleVisible = false
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.userId) {
            updateSysUser(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addSysUser(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
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
      &.red { color: #f56c6c; }
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
