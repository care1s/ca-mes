<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-menu"></i>
        <span class="title">菜单管理</span>
        <span class="subtitle">Menu Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon blue">
            <i class="el-icon-s-grid"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">菜单总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon green">
            <i class="el-icon-folder-opened"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.directory }}</div>
            <div class="stat-label">目录</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon orange">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.menu }}</div>
            <div class="stat-label">菜单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-icon purple">
            <i class="el-icon-mouse"></i>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.button }}</div>
            <div class="stat-label">按钮</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item label="菜单状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable>
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

    <!-- 树形表格 -->
    <el-card class="table-card" shadow="never">
      <div slot="header" class="card-header">
        <span class="header-title">
          <i class="el-icon-menu"></i>
          菜单列表
        </span>
        <div class="header-actions">
          <el-radio-group v-model="expandAll" size="small" @change="toggleExpand">
            <el-radio-button :label="true">展开全部</el-radio-button>
            <el-radio-button :label="false">收起全部</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      
      <el-table
        v-loading="loading"
        :data="tableData"
        row-key="menuId"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        :expand-row-keys="expandRowKeys"
        border
        stripe
        highlight-current-row
        style="width: 100%"
      >
        <el-table-column prop="menuName" label="菜单名称" min-width="200" show-overflow-tooltip>
          <template slot-scope="scope">
            <i :class="scope.row.icon || getMenuIcon(scope.row.menuType)" style="margin-right: 8px;"></i>
            <span>{{ scope.row.menuName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="menuType" label="类型" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="getMenuTypeStyle(scope.row.menuType)" size="small">
              {{ getMenuTypeLabel(scope.row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由地址" min-width="150" show-overflow-tooltip />
        <el-table-column prop="component" label="组件路径" min-width="150" show-overflow-tooltip />
        <el-table-column prop="perms" label="权限标识" min-width="150" show-overflow-tooltip />
        <el-table-column prop="orderNum" label="排序" width="70" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="0"
              :inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="visible" label="显示" width="70" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.visible === 0" type="success" size="mini">显示</el-tag>
            <el-tag v-else type="info" size="mini">隐藏</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-plus" @click="handleAddChild(scope.row)">新增</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-select 
                v-model="form.parentId" 
                placeholder="选择上级菜单（不选则为顶级菜单）" 
                clearable 
                style="width: 100%"
                :disabled="isEdit"
              >
                <el-option :value="0" label="主类目" />
                <el-option 
                  v-for="item in menuList" 
                  :key="item.menuId" 
                  :label="item.menuName" 
                  :value="item.menuId"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="form.menuType">
                <el-radio-button label="M">目录</el-radio-button>
                <el-radio-button label="C">菜单</el-radio-button>
                <el-radio-button label="F">按钮</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="form.orderNum" :min="0" :max="999" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单图标" prop="icon">
              <el-select v-model="form.icon" placeholder="选择图标" clearable style="width: 100%">
                <el-option
                  v-for="icon in iconOptions"
                  :key="icon.value"
                  :label="icon.label"
                  :value="icon.value"
                >
                  <span style="float: left"><i :class="icon.value"></i></span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ icon.label }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.menuType !== 'F'">
          <el-col :span="12">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="form.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.menuType === 'C'">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限标识" prop="perms">
              <el-input v-model="form.perms" placeholder="如: system:user:list" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单状态">
              <el-radio-group v-model="form.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.menuType !== 'F'">
          <el-col :span="12">
            <el-form-item label="显示状态">
              <el-radio-group v-model="form.visible">
                <el-radio :label="0">显示</el-radio>
                <el-radio :label="1">隐藏</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 菜单管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
import { listSysMenu, addSysMenu, updateSysMenu, delSysMenu, changeMenuStatus } from '@/api/md'

export default {
  name: 'SysMenu',
  data() {
    return {
      loading: false,
      submitLoading: false,
      isEdit: false,
      expandAll: false,
      expandRowKeys: [],
      stats: {
        total: 0,
        directory: 0,
        menu: 0,
        button: 0
      },
      queryParams: {
        menuName: '',
        status: null
      },
      tableData: [],
      menuList: [],
      dialogVisible: false,
      dialogTitle: '新增菜单',
      form: {
        menuId: null,
        menuName: '',
        parentId: 0,
        orderNum: 0,
        path: '',
        component: '',
        menuType: 'M',
        visible: 0,
        status: 0,
        perms: '',
        icon: '',
        remark: ''
      },
      rules: {
        menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
        orderNum: [{ required: true, message: '请输入显示排序', trigger: 'blur' }],
        path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }],
        menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
      },
      iconOptions: [
        { value: 'el-icon-s-home', label: '首页' },
        { value: 'el-icon-s-grid', label: '网格' },
        { value: 'el-icon-s-data', label: '数据' },
        { value: 'el-icon-document', label: '文档' },
        { value: 'el-icon-folder', label: '文件夹' },
        { value: 'el-icon-folder-opened', label: '打开文件夹' },
        { value: 'el-icon-setting', label: '设置' },
        { value: 'el-icon-user', label: '用户' },
        { value: 'el-icon-user-solid', label: '用户(实)' },
        { value: 'el-icon-menu', label: '菜单' },
        { value: 'el-icon-s-tools', label: '工具' },
        { value: 'el-icon-s-custom', label: '客户' },
        { value: 'el-icon-s-goods', label: '商品' },
        { value: 'el-icon-s-shop', label: '商店' },
        { value: 'el-icon-s-order', label: '订单' },
        { value: 'el-icon-s-ticket', label: '票据' },
        { value: 'el-icon-s-marketing', label: '营销' },
        { value: 'el-icon-s-release', label: '发布' },
        { value: 'el-icon-s-flag', label: '标记' },
        { value: 'el-icon-s-claim', label: '声明' },
        { value: 'el-icon-s-finance', label: '财务' },
        { value: 'el-icon-s-opportunity', label: '机会' },
        { value: 'el-icon-s-check', label: '检查' },
        { value: 'el-icon-edit', label: '编辑' },
        { value: 'el-icon-delete', label: '删除' },
        { value: 'el-icon-plus', label: '添加' },
        { value: 'el-icon-search', label: '搜索' },
        { value: 'el-icon-view', label: '查看' },
        { value: 'el-icon-lock', label: '锁定' },
        { value: 'el-icon-unlock', label: '解锁' },
        { value: 'el-icon-mouse', label: '鼠标' },
        { value: 'el-icon-monitor', label: '显示器' },
        { value: 'el-icon-cpu', label: 'CPU' },
        { value: 'el-icon-printer', label: '打印' },
        { value: 'el-icon-truck', label: '卡车' },
        { value: 'el-icon-box', label: '盒子' },
        { value: 'el-icon-suitcase', label: '手提箱' },
        { value: 'el-icon-shopping-cart-full', label: '购物车' }
      ]
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    // 获取表格数据
    fetchData() {
      this.loading = true
      listSysMenu(this.queryParams).then(response => {
        this.menuList = response.data || []
        this.tableData = this.handleTree(response.data || [], 'menuId')
        this.updateStats(response.data || [])
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 更新统计数据
    updateStats(data) {
      this.stats.total = data.length
      this.stats.directory = data.filter(item => item.menuType === 'M').length
      this.stats.menu = data.filter(item => item.menuType === 'C').length
      this.stats.button = data.filter(item => item.menuType === 'F').length
    },
    // 处理树形结构
    handleTree(data, id, parentId = 'parentId', children = 'children', rootId = 0) {
      id = id || 'id'
      parentId = parentId || 'parentId'
      children = children || 'children'
      rootId = rootId || Math.min.apply(Math, data.map(item => { return item[parentId] })) || 0
      const obj = {}
      data.forEach(item => {
        item[children] = []
        obj[item[id]] = item
      })
      const treeData = []
      data.forEach(item => {
        if (Number(item[parentId]) !== Number(rootId)) {
          if (obj[item[parentId]]) {
            obj[item[parentId]][children].push(item)
          }
        } else {
          treeData.push(item)
        }
      })
      return treeData
    },
    // 获取菜单类型标签
    getMenuTypeLabel(type) {
      const map = { M: '目录', C: '菜单', F: '按钮' }
      return map[type] || type
    },
    // 获取菜单类型样式
    getMenuTypeStyle(type) {
      const map = { M: 'primary', C: 'success', F: 'warning' }
      return map[type] || ''
    },
    // 获取菜单图标
    getMenuIcon(type) {
      const map = { M: 'el-icon-folder', C: 'el-icon-document', F: 'el-icon-mouse' }
      return map[type] || 'el-icon-menu'
    },
    // 展开/收起全部
    toggleExpand(val) {
      if (val) {
        this.expandRowKeys = this.getAllIds(this.tableData)
      } else {
        this.expandRowKeys = []
      }
    },
    // 获取所有ID
    getAllIds(data) {
      let ids = []
      data.forEach(item => {
        ids.push(item.menuId.toString())
        if (item.children && item.children.length) {
          ids = ids.concat(this.getAllIds(item.children))
        }
      })
      return ids
    },
    // 查询
    handleQuery() {
      this.fetchData()
    },
    // 重置查询
    resetQuery() {
      this.queryParams = {
        menuName: '',
        status: null
      }
      this.fetchData()
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增菜单'
      this.isEdit = false
      this.resetForm()
      this.dialogVisible = true
    },
    // 新增子菜单
    handleAddChild(row) {
      this.dialogTitle = '新增子菜单'
      this.isEdit = false
      this.resetForm()
      this.form.parentId = row.menuId
      this.form.menuType = row.menuType === 'M' ? 'C' : 'F'
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑菜单'
      this.isEdit = true
      this.resetForm()
      Object.assign(this.form, row)
      this.form.parentId = row.parentId || 0
      this.dialogVisible = true
    },
    // 删除
    handleDelete(row) {
      this.$confirm(`确认删除 "${row.menuName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSysMenu(row.menuId).then(() => {
          this.$message.success('删除成功')
          this.fetchData()
        })
      })
    },
    // 状态变更
    handleStatusChange(row) {
      const text = row.status === 0 ? '启用' : '停用'
      this.$confirm(`确认要"${text}""${row.menuName}"菜单吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        changeMenuStatus(row.menuId, row.status).then(() => {
          this.$message.success(text + '成功')
        }).catch(() => {
          row.status = row.status === 0 ? 1 : 0
        })
      }).catch(() => {
        row.status = row.status === 0 ? 1 : 0
      })
    },
    // 重置表单
    resetForm() {
      this.form = {
        menuId: null,
        menuName: '',
        parentId: 0,
        orderNum: 0,
        path: '',
        component: '',
        menuType: 'M',
        visible: 0,
        status: 0,
        perms: '',
        icon: '',
        remark: ''
      }
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const data = { ...this.form }
          if (data.menuId) {
            updateSysMenu(data).then(() => {
              this.$message.success('修改成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          } else {
            addSysMenu(data).then(() => {
              this.$message.success('新增成功')
              this.dialogVisible = false
              this.fetchData()
              this.submitLoading = false
            }).catch(() => {
              this.submitLoading = false
            })
          }
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
  min-height: calc(100vh - 120px);
}

// 页面头部
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 0 0 15px 0;
  border-bottom: 2px solid #EBEEF5;

  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 28px;
      color: #409EFF;
      margin-right: 12px;
    }
    
    .title {
      font-size: 22px;
      font-weight: 600;
      color: #303133;
      margin-right: 10px;
    }
    
    .subtitle {
      font-size: 13px;
      color: #909399;
      font-weight: normal;
    }
  }
}

// 搜索栏
.search-card {
  margin-bottom: 20px;
  
  .search-form {
    .el-form-item {
      margin-bottom: 0;
      margin-right: 20px;
    }
  }
}

// 统计卡片
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }
  
  .stat-icon {
    width: 60px;
    height: 60px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 15px;
    
    i {
      font-size: 28px;
      color: #fff;
    }
    
    &.blue {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
    
    &.green {
      background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
    }
    
    &.orange {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
    }
    
    &.purple {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }
  }
  
  .stat-info {
    flex: 1;
    
    .stat-value {
      font-size: 28px;
      font-weight: 700;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }
    
    .stat-label {
      font-size: 14px;
      color: #909399;
    }
  }
}

// 表格卡片
.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .header-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      
      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }
  }
  
  .el-table {
    margin-top: 15px;
  }
}

// 响应式调整
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    
    .action-section {
      margin-top: 10px;
    }
  }
  
  .stat-row {
    .el-col {
      margin-bottom: 15px;
    }
  }
}
</style>
