<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header md">
      <div class="title-section">
        <i class="el-icon-s-grid"></i>
        <span class="title">物料类型管理</span>
        <span class="subtitle">Item Type Management</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增类型</el-button>
        <el-button icon="el-icon-refresh" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="queryParams" class="search-form">
        <el-form-item label="类型编码">
          <el-input v-model="queryParams.typeCode" placeholder="请输入类型编码" clearable />
        </el-form-item>
        <el-form-item label="类型名称">
          <el-input v-model="queryParams.typeName" placeholder="请输入类型名称" clearable />
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
          <i class="el-icon-s-grid"></i>
          物料类型列表
        </span>
      </div>
      
      <el-table
        
        :data="tableData"
        border
        stripe
        row-key="typeId"
        default-expand-all
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
        style="width: 100%"
      >
        <el-table-column prop="typeCode" label="类型编码" width="180" show-overflow-tooltip />
        <el-table-column prop="typeName" label="类型名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="itemAttr" label="物料属性" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getItemAttrType(scope.row.itemAttr)">
              {{ getItemAttrText(scope.row.itemAttr) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="0"
              inactive-value="1"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-plus" @click="handleAddChild(scope.row)">添加子类</el-button>
            <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="上级类型">
          <el-cascader
            v-model="form.parentId"
            :options="typeOptions"
            :props="{ checkStrictly: true, value: 'typeId', label: 'typeName' }"
            clearable
            style="width: 100%"
            placeholder="请选择上级类型（不选则为顶级）"
          />
        </el-form-item>
        <el-form-item label="类型编码" prop="typeCode">
          <el-input v-model="form.typeCode" placeholder="请输入类型编码" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="物料属性" prop="itemAttr">
          <el-select v-model="form.itemAttr" placeholder="请选择物料属性" style="width: 100%">
            <el-option label="原材料" value="RAW" />
            <el-option label="半成品" value="SEMI" />
            <el-option label="产成品" value="PRODUCT" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
/**
 * 物料类型管理 - carels
 * @author carels
 * @version V9.0
 * @date 2026-03-15
 */
export default {
  name: 'MdItemType',
  data() {
    return {
      loading: false,
      queryParams: {
        typeCode: '',
        typeName: ''
      },
      tableData: [
        {
          typeId: 1,
          typeCode: 'RAW',
          typeName: '原材料',
          itemAttr: 'RAW',
          parentId: 0,
          status: '0',
          remark: '生产用原材料',
          children: [
            {
              typeId: 11,
              typeCode: 'RAW-METAL',
              typeName: '金属材料',
              itemAttr: 'RAW',
              parentId: 1,
              status: '0',
              remark: '各类金属原材料'
            },
            {
              typeId: 12,
              typeCode: 'RAW-PLASTIC',
              typeName: '塑料材料',
              itemAttr: 'RAW',
              parentId: 1,
              status: '0',
              remark: '各类塑料原材料'
            }
          ]
        },
        {
          typeId: 2,
          typeCode: 'SEMI',
          typeName: '半成品',
          itemAttr: 'SEMI',
          parentId: 0,
          status: '0',
          remark: '生产过程中半成品',
          children: []
        },
        {
          typeId: 3,
          typeCode: 'PRODUCT',
          typeName: '产成品',
          itemAttr: 'PRODUCT',
          parentId: 0,
          status: '0',
          remark: '最终产品',
          children: [
            {
              typeId: 31,
              typeCode: 'PRODUCT-A',
              typeName: 'A类产品',
              itemAttr: 'PRODUCT',
              parentId: 3,
              status: '0',
              remark: 'A系列成品'
            }
          ]
        }
      ],
      typeOptions: [],
      dialogVisible: false,
      dialogTitle: '新增物料类型',
      form: {
        typeId: null,
        parentId: null,
        typeCode: '',
        typeName: '',
        itemAttr: '',
        remark: ''
      },
      rules: {
        typeCode: [{ required: true, message: '请输入类型编码', trigger: 'blur' }],
        typeName: [{ required: true, message: '请输入类型名称', trigger: 'blur' }],
        itemAttr: [{ required: true, message: '请选择物料属性', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchData()
    this.loadTypeOptions()
  },
  methods: {
    fetchData() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
      }, 500)
    },
    loadTypeOptions() {
      // 加载类型选项用于级联选择
      this.typeOptions = this.tableData
    },
    handleQuery() {
      this.fetchData()
    },
    resetQuery() {
      this.queryParams = {
        typeCode: '',
        typeName: ''
      }
      this.fetchData()
    },
    handleAdd() {
      this.dialogTitle = '新增物料类型'
      this.form = {
        typeId: null,
        parentId: null,
        typeCode: '',
        typeName: '',
        itemAttr: '',
        remark: ''
      }
      this.dialogVisible = true
    },
    handleAddChild(row) {
      this.dialogTitle = '添加子类型'
      this.form = {
        typeId: null,
        parentId: row.typeId,
        typeCode: '',
        typeName: '',
        itemAttr: row.itemAttr,
        remark: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑物料类型'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确认删除物料类型 "${row.typeName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
      })
    },
    handleStatusChange(row) {
      const status = row.status === '0' ? '启用' : '停用'
      this.$message.success(`已${status}物料类型：${row.typeName}`)
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.fetchData()
        }
      })
    },
    getItemAttrType(attr) {
      const map = {
        'RAW': 'primary',
        'SEMI': 'warning',
        'PRODUCT': 'success'
      }
      return map[attr] || 'info'
    },
    getItemAttrText(attr) {
      const map = {
        'RAW': '原材料',
        'SEMI': '半成品',
        'PRODUCT': '产成品'
      }
      return map[attr] || attr
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
</style>
