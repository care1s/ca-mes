<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-s-custom"></i>
        <span class="title">角色管理</span>
        <span class="subtitle">Role Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增角色</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计信息 -->
    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-label">角色总数</span>
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
        <el-form-item label="角色名称">
          <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="queryParams.roleCode" placeholder="请输入角色编码" clearable />
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
        <el-table-column prop="roleName" label="角色名称" min-width="150" />
        <el-table-column prop="roleCode" label="角色编码" width="150" />
        <el-table-column prop="orderNum" label="显示顺序" width="100" align="center" />
        <el-table-column prop="dataScope" label="数据范围" width="150" align="center">
          <template slot-scope="scope">
            {{ getDataScopeText(scope.row.dataScope) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
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
            <el-button type="text" size="small" @click="handleAssignMenu(scope.row)">分配菜单</el-button>
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
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="请输入角色编码" :disabled="!!form.roleId" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="数据范围" prop="dataScope">
          <el-select v-model="form.dataScope" placeholder="请选择数据范围" style="width: 100%">
            <el-option label="全部数据" :value="1" />
            <el-option label="本部门数据" :value="2" />
            <el-option label="本部门及以下数据" :value="3" />
            <el-option label="仅本人数据" :value="4" />
          </el-select>
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

    <!-- 分配菜单对话框 -->
    <el-dialog title="分配菜单权限" :visible.sync="assignMenuVisible" width="500px" :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="角色">
          <span>{{ currentRole.roleName }} ({{ currentRole.roleCode }})</span>
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            ref="menuTree"
            :data="menuTreeData"
            show-checkbox
            node-key="menuId"
            :props="{ label: 'menuName', children: 'children' }"
            :default-checked-keys="selectedMenus"
            :check-strictly="false"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="assignMenuVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAssignMenu">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  listSysRole, 
  addSysRole, 
  updateSysRole, 
  delSysRole,
  assignRoleMenus,
  getMenusByRoleId,
  getSysMenuTree
} from '@/api/md'

export default {
  name: 'SysRole',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        roleName: '',
        roleCode: '',
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
        roleId: null,
        roleName: '',
        roleCode: '',
        orderNum: 0,
        dataScope: 1,
        remark: ''
      },
      rules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
      },
      assignMenuVisible: false,
      currentRole: {},
      menuTreeData: [],
      selectedMenus: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      listSysRole(this.queryParams).then(response => {
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
        roleName: '',
        roleCode: '',
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
      this.dialogTitle = '新增角色'
      this.form = {
        roleId: null,
        roleName: '',
        roleCode: '',
        orderNum: 0,
        dataScope: 1,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除角色 "${row.roleName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSysRole(row.roleId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleToggleStatus(row) {
      const newStatus = row.status === 0 ? 1 : 0
      const action = newStatus === 0 ? '启用' : '停用'
      this.$confirm(`确认${action}角色 "${row.roleName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateSysRole({ roleId: row.roleId, status: newStatus }).then(() => {
          this.$message.success(`${action}成功`)
          this.fetchData()
        })
      }).catch(() => {})
    },
    handleAssignMenu(row) {
      this.currentRole = row
      this.selectedMenus = []
      // 获取菜单树
      getSysMenuTree().then(response => {
        this.menuTreeData = this.buildMenuTree(response.data || [])
        // 获取角色当前菜单
        getMenusByRoleId(row.roleId).then(res => {
          const roleMenus = res.data || []
          this.selectedMenus = roleMenus.map(m => m.menuId)
          this.assignMenuVisible = true
        })
      })
    },
    buildMenuTree(menus) {
      // 构建树形结构
      const menuMap = {}
      menus.forEach(menu => {
        menuMap[menu.menuId] = { ...menu, children: [] }
      })
      
      const tree = []
      menus.forEach(menu => {
        if (menu.parentId === 0 || !menuMap[menu.parentId]) {
          tree.push(menuMap[menu.menuId])
        } else {
          const parent = menuMap[menu.parentId]
          if (parent) {
            parent.children.push(menuMap[menu.menuId])
          }
        }
      })
      return tree
    },
    submitAssignMenu() {
      const checkedKeys = this.$refs.menuTree.getCheckedKeys()
      const halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys()
      const menuIds = [...checkedKeys, ...halfCheckedKeys]
      
      assignRoleMenus(this.currentRole.roleId, menuIds).then(() => {
        this.$message.success('菜单权限分配成功')
        this.assignMenuVisible = false
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.roleId) {
            updateSysRole(this.form).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
            })
          } else {
            addSysRole(this.form).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
            })
          }
        }
      })
    },
    getDataScopeText(scope) {
      const scopes = { 1: '全部数据', 2: '本部门数据', 3: '本部门及以下数据', 4: '仅本人数据' }
      return scopes[scope] || '未知'
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

::v-deep .el-tree {
  max-height: 400px;
  overflow-y: auto;
}
</style>
