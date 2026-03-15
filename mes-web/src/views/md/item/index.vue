<template>
  <div class="app-container">
    <!-- 简洁页面标题 -->
    <div class="page-header">
      <div class="title-section">
        <i class="el-icon-goods"></i>
        <span class="title">物料信息管理</span>
      </div>
      <div class="action-section">
        <el-button type="primary" icon="el-icon-plus" size="small" @click="handleAdd">新增物料</el-button>
        <el-button icon="el-icon-download" size="small">导出</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="fetchData">刷新</el-button>
      </div>
    </div>

    <el-row :gutter="15" class="main-content">
      <!-- 左侧物料类型树 -->
      <el-col :span="4" class="tree-col">
        <el-card class="tree-card" shadow="never" :body-style="{ padding: '0' }">
          <div slot="header" class="tree-header">
            <span><i class="el-icon-s-grid"></i> 物料类型</span>
            <el-tooltip content="添加顶级类型" placement="top">
              <el-button type="text" icon="el-icon-plus" @click="handleAddType(0)"></el-button>
            </el-tooltip>
          </div>
          <div class="tree-search">
            <el-input
              v-model="filterText"
              placeholder="搜索类型"
              size="small"
              prefix-icon="el-icon-search"
              clearable
            />
          </div>
          <el-tree
            ref="tree"
            class="filter-tree"
            :data="typeTreeData"
            :props="defaultProps"
            :filter-node-method="filterNode"
            :expand-on-click-node="false"
            highlight-current
            node-key="typeId"
            default-expand-all
            @node-click="handleNodeClick"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span class="node-content" :class="{ active: currentTypeId === data.typeId }">
                <i :class="getTypeIcon(data.itemAttr)" :style="{ color: getTypeColor(data.itemAttr) }"></i>
                <span class="node-label">{{ node.label }}</span>
                <span class="node-count">{{ data.count }}</span>
              </span>
              <span class="node-actions">
                <el-tooltip content="添加子类型" placement="top">
                  <i class="el-icon-circle-plus-outline" @click.stop="handleAddType(data.typeId)"></i>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <i class="el-icon-edit" @click.stop="handleEditType(data)"></i>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <i class="el-icon-delete" @click.stop="handleDeleteType(data)"></i>
                </el-tooltip>
              </span>
            </span>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 右侧物料列表 -->
      <el-col :span="20" class="table-col">
        <!-- 面包屑和搜索 -->
        <el-card class="filter-card" shadow="never" :body-style="{ padding: '12px 15px' }">
          <div class="filter-row">
            <div class="breadcrumb-wrap">
              <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                  <span class="breadcrumb-link" @click="clearTypeFilter">全部物料</span>
                </el-breadcrumb-item>
                <el-breadcrumb-item v-if="breadcrumbPath.length > 0">
                  <el-dropdown v-if="breadcrumbPath.length > 1" trigger="click">
                    <span class="el-dropdown-link">
                      {{ breadcrumbPath[breadcrumbPath.length - 1].typeName }}<i class="el-icon-arrow-down"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="(item, idx) in breadcrumbPath" :key="idx" @click.native="jumpToType(item.typeId)">
                        {{ item.typeName }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                  <span v-else>{{ breadcrumbPath[0].typeName }}</span>
                </el-breadcrumb-item>
              </el-breadcrumb>
              <el-tag v-if="currentTypeName" size="small" closable @close="clearTypeFilter" type="info" effect="plain" style="margin-left: 10px;">
                {{ currentTypeName }}
              </el-tag>
            </div>
            <div class="search-wrap">
              <el-input
                v-model="queryParams.keyword"
                placeholder="搜索物料编码/名称/规格"
                size="small"
                clearable
                style="width: 220px"
                @keyup.enter.native="handleQuery"
              />
              <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
              <el-button icon="el-icon-refresh-right" size="small" @click="resetQuery">重置</el-button>
            </div>
          </div>
        </el-card>

        <!-- 扁平统计栏 -->
        <div class="stat-bar">
          <div class="stat-item">
            <span class="stat-label">当前类型</span>
            <span class="stat-value">{{ currentTypeName || '全部' }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">物料总数</span>
            <span class="stat-value blue">{{ statistics.total }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">启用</span>
            <span class="stat-value green">{{ statistics.active }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">库存预警</span>
            <span class="stat-value orange">{{ statistics.lowStock }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">停用</span>
            <span class="stat-value gray">{{ statistics.inactive }}</span>
          </div>
        </div>

        <!-- 数据表格 -->
        <el-card class="table-card" shadow="never" :body-style="{ padding: '0', display: 'flex', flexDirection: 'column', height: '100%' }">
          <div class="table-scroll-wrapper">
            <el-table
              v-loading="loading"
              :data="tableData"
              border
              stripe
              highlight-current-row
              size="small"
              style="width: 100%; min-width: 1200px;"
              :height="tableHeight"
              @row-dblclick="handleRowDblClick"
            >
              <el-table-column type="index" label="序号" width="45" align="center" fixed="left" />
              <el-table-column prop="itemCode" label="物料编码" min-width="100" show-overflow-tooltip fixed="left">
                <template slot-scope="scope">
                  <el-link type="primary" @click="handleView(scope.row)">{{ scope.row.itemCode }}</el-link>
                </template>
              </el-table-column>
              <el-table-column prop="itemName" label="物料名称" min-width="140" show-overflow-tooltip fixed="left" />
              <el-table-column prop="typeName" label="所属类型" min-width="90" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="getTypeTagType(scope.row.itemAttr)" effect="plain">
                    {{ scope.row.typeName }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="specification" label="规格型号" min-width="100" show-overflow-tooltip />
              <el-table-column prop="unitName" label="单位" min-width="50" align="center" />
              <el-table-column prop="safetyStock" label="安全库存" min-width="80" align="right" />
              <el-table-column prop="quantity" label="当前库存" min-width="80" align="right">
                <template slot-scope="scope">
                  <span :class="getStockClass(scope.row)">{{ scope.row.quantity }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="standardCost" label="标准成本" min-width="85" align="right">
                <template slot-scope="scope">¥{{ scope.row.standardCost }}</template>
              </el-table-column>
              <el-table-column prop="status" label="状态" min-width="60" align="center">
                <template slot-scope="scope">
                  <el-switch
                    v-model="scope.row.status"
                    active-value="0"
                    inactive-value="1"
                    @change="handleStatusChange(scope.row)"
                  />
                </template>
              </el-table-column>
              <el-table-column label="操作" min-width="120" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
                  <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
                  <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 分页在表格下方 -->
          <div class="pagination-wrap">
            <el-pagination
              background
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              :page-sizes="[20, 50, 100, 200]"
              :page-size="queryParams.pageSize"
              :current-page="queryParams.pageNum"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 物料详情弹框 -->
    <el-dialog title="物料详情" :visible.sync="detailDialogVisible" width="600px" :close-on-click-modal="true">
      <div class="detail-content" v-if="currentRow">
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">物料编码：</span>
              <span class="detail-value">{{ currentRow.itemCode }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">物料名称：</span>
              <span class="detail-value">{{ currentRow.itemName }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">所属类型：</span>
              <span class="detail-value">
                <el-tag size="small" :type="getTypeTagType(currentRow.itemAttr)" effect="plain">
                  {{ currentRow.typeName }}
                </el-tag>
              </span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">规格型号：</span>
              <span class="detail-value">{{ currentRow.specification || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">计量单位：</span>
              <span class="detail-value">{{ currentRow.unitName }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">标准成本：</span>
              <span class="detail-value cost">¥{{ currentRow.standardCost }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">安全库存：</span>
              <span class="detail-value">{{ currentRow.safetyStock }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">当前库存：</span>
              <span class="detail-value" :class="getStockClass(currentRow)">{{ currentRow.quantity }}</span>
            </div>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" class="detail-section">
          <el-col :span="12">
            <div class="detail-item">
              <span class="detail-label">状态：</span>
              <span class="detail-value">
                <el-tag size="small" :type="currentRow.status === '0' ? 'success' : 'info'">
                  {{ currentRow.status === '0' ? '启用' : '停用' }}
                </el-tag>
              </span>
            </div>
          </el-col>
        </el-row>
        
        <div class="detail-section">
          <div class="detail-item block">
            <span class="detail-label">备注：</span>
            <span class="detail-value">{{ currentRow.remark || '-' }}</span>
          </div>
        </div>
      </div>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关 闭</el-button>
        <el-button type="primary" @click="handleEdit(currentRow); detailDialogVisible = false">编 辑</el-button>
      </div>
    </el-dialog>

    <!-- 物料对话框 -->
    <el-dialog :title="isEdit ? '编辑物料' : '新增物料'" :visible.sync="dialogVisible" width="680px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="物料编码" prop="itemCode">
              <el-input v-model="form.itemCode" placeholder="请输入" :disabled="isEdit">
                <el-button v-if="!isEdit" slot="append" icon="el-icon-refresh" @click="generateCode"></el-button>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="物料名称" prop="itemName">
              <el-input v-model="form.itemName" placeholder="请输入" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="物料类型" prop="typeId">
          <el-cascader
            v-model="form.typePath"
            :options="typeTreeData"
            :props="{ value: 'typeId', label: 'typeName', children: 'children', emitPath: false, checkStrictly: true }"
            placeholder="选择类型"
            style="width: 100%"
            @change="handleTypeChange"
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规格型号">
              <el-input v-model="form.specification" placeholder="请输入" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计量单位" prop="unitName">
              <el-select v-model="form.unitName" placeholder="选择" style="width: 100%" filterable allow-create>
                <el-option v-for="unit in unitOptions" :key="unit" :label="unit" :value="unit" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="安全库存">
              <el-input-number v-model="form.safetyStock" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大库存">
              <el-input-number v-model="form.maxStock" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标准成本">
              <el-input-number v-model="form.standardCost" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>

    <!-- 类型对话框 -->
    <el-dialog :title="isEditType ? '编辑类型' : '新增类型'" :visible.sync="typeDialogVisible" width="500px">
      <el-form :model="typeForm" :rules="typeRules" ref="typeForm" label-width="100px">
        <el-form-item label="上级类型">
          <span v-if="typeForm.parentName">{{ typeForm.parentName }}</span>
          <span v-else style="color: #909399;">顶级类型</span>
        </el-form-item>
        <el-form-item label="类型编码" prop="typeCode">
          <el-input v-model="typeForm.typeCode" placeholder="请输入编码" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="typeForm.typeName" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="物料属性" prop="itemAttr">
          <el-radio-group v-model="typeForm.itemAttr">
            <el-radio label="RAW">原材料</el-radio>
            <el-radio label="SEMI">半成品</el-radio>
            <el-radio label="PRODUCT">产成品</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="typeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTypeForm">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  listItem, 
  addItem, 
  updateItem, 
  delItem, 
  exportItem,
  getItemTypeTree,
  addItemType,
  updateItemType,
  delItemType
} from '@/api/md'

export default {
  name: 'MdItem',
  data() {
    return {
      loading: false,
      total: 0,
      filterText: '',
      currentTypeId: null,
      currentTypeName: '',
      breadcrumbPath: [],
      tableHeight: 500,
      
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        keyword: ''
      },
      
      statistics: {
        total: 0,
        active: 0,
        lowStock: 0,
        inactive: 0
      },
      
      typeTreeData: [],
      
      defaultProps: {
        children: 'children',
        label: 'typeName'
      },
      
      tableData: [],
      
      dialogVisible: false,
      isEdit: false,
      form: {
        itemId: null,
        itemCode: '',
        itemName: '',
        typeId: null,
        typePath: [],
        specification: '',
        unitName: '',
        safetyStock: 0,
        maxStock: 0,
        standardCost: 0,
        remark: ''
      },
      rules: {
        itemCode: [{ required: true, message: '请输入物料编码', trigger: 'blur' }],
        itemName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }],
        typeId: [{ required: true, message: '请选择物料类型', trigger: 'change' }],
        unitName: [{ required: true, message: '请选择单位', trigger: 'change' }]
      },
      
      // 详情弹框
      detailDialogVisible: false,
      currentRow: null,
      
      unitOptions: ['PC', 'KG', 'M', 'SET', 'BOX', 'L', 'ML', 'G'],
      
      typeDialogVisible: false,
      isEditType: false,
      typeForm: {
        typeId: null,
        parentId: 0,
        parentName: '',
        typeCode: '',
        typeName: '',
        itemAttr: 'RAW'
      },
      typeRules: {
        typeCode: [{ required: true, message: '请输入编码', trigger: 'blur' }],
        typeName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        itemAttr: [{ required: true, message: '请选择属性', trigger: 'change' }]
      }
    }
  },
  
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  
  mounted() {
    this.fetchTypeTree()
    this.fetchData()
    this.calcTableHeight()
    window.addEventListener('resize', this.calcTableHeight)
  },
  
  beforeDestroy() {
    window.removeEventListener('resize', this.calcTableHeight)
  },
  
  methods: {
    calcTableHeight() {
      this.tableHeight = window.innerHeight - 340
    },
    
    // 获取物料类型树
    async fetchTypeTree() {
      try {
        const res = await getItemTypeTree()
        this.typeTreeData = res.data || []
      } catch (error) {
        this.$message.error('获取物料类型失败')
      }
    },
    
    // 获取物料列表
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNum: this.queryParams.pageNum,
          pageSize: this.queryParams.pageSize,
          typeId: this.currentTypeId,
          keyword: this.queryParams.keyword
        }
        const res = await listItem(params)
        this.tableData = res.rows || []
        this.total = res.total || 0
        this.updateStatistics()
      } catch (error) {
        this.$message.error('获取物料列表失败')
      } finally {
        this.loading = false
      }
    },
    
    // 更新统计信息
    updateStatistics() {
      const total = this.tableData.length
      const active = this.tableData.filter(item => item.status === '0').length
      const inactive = this.tableData.filter(item => item.status === '1').length
      const lowStock = this.tableData.filter(item => item.quantity <= item.safetyStock).length
      this.statistics = { total, active, lowStock, inactive }
    },
    
    filterNode(value, data) {
      if (!value) return true
      return data.typeName.indexOf(value) !== -1
    },
    
    handleNodeClick(data) {
      this.currentTypeId = data.typeId
      this.currentTypeName = data.typeName
      this.buildBreadcrumb(data.typeId)
      this.fetchData()
    },
    
    buildBreadcrumb(typeId) {
      this.breadcrumbPath = []
      this.findPath(this.typeTreeData, typeId)
    },
    
    findPath(nodes, typeId, path = []) {
      for (let node of nodes) {
        if (node.typeId === typeId) {
          this.breadcrumbPath = [...path, node]
          return true
        }
        if (node.children && node.children.length > 0) {
          if (this.findPath(node.children, typeId, [...path, node])) {
            return true
          }
        }
      }
      return false
    },
    
    jumpToType(typeId) {
      this.$refs.tree.setCurrentKey(typeId)
      const node = this.findNode(this.typeTreeData, typeId)
      if (node) {
        this.handleNodeClick(node)
      }
    },
    
    findNode(nodes, typeId) {
      for (let node of nodes) {
        if (node.typeId === typeId) return node
        if (node.children) {
          const found = this.findNode(node.children, typeId)
          if (found) return found
        }
      }
      return null
    },
    
    clearTypeFilter() {
      this.currentTypeId = null
      this.currentTypeName = ''
      this.breadcrumbPath = []
      this.$refs.tree.setCurrentKey(null)
      this.fetchData()
    },
    
    handleQuery() {
      this.queryParams.pageNum = 1
      this.fetchData()
    },
    
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 20,
        keyword: ''
      }
      this.fetchData()
    },
    
    handleAdd() {
      this.isEdit = false
      this.form = {
        itemId: null,
        itemCode: '',
        itemName: '',
        typeId: this.currentTypeId,
        typePath: this.currentTypeId ? [this.currentTypeId] : [],
        specification: '',
        unitName: '',
        safetyStock: 0,
        maxStock: 0,
        standardCost: 0,
        remark: ''
      }
      this.dialogVisible = true
    },
    
    handleEdit(row) {
      this.isEdit = true
      this.form = { ...row, typePath: [row.typeId] }
      this.dialogVisible = true
    },
    
    // 双击行打开详情
    handleRowDblClick(row) {
      this.currentRow = row
      this.detailDialogVisible = true
    },
    
    handleView(row) {
      this.$alert(
        `<strong>物料编码：</strong>${row.itemCode}<br>
         <strong>物料名称：</strong>${row.itemName}<br>
         <strong>所属类型：</strong>${row.typeName}<br>
         <strong>规格型号：</strong>${row.specification}<br>
         <strong>当前库存：</strong>${row.quantity}<br>
         <strong>标准成本：</strong>¥${row.standardCost}`,
        '物料详情',
        { dangerouslyUseHTMLString: true }
      )
    },
    
    handleDelete(row) {
      this.$confirm(`删除物料 "${row.itemName}"？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await delItem(row.itemId)
          this.$message.success('删除成功')
          this.fetchData()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    
    handleStatusChange(row) {
      const status = row.status === '0' ? '启用' : '停用'
      updateItem({ ...row, status: row.status }).then(() => {
        this.$message.success(`已${status}：${row.itemName}`)
      }).catch(() => {
        this.$message.error('状态更新失败')
      })
    },
    
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.fetchData()
    },
    
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.fetchData()
    },
    
    handleTypeChange(val) {
      this.form.typeId = val
    },
    
    generateCode() {
      this.form.itemCode = 'MAT' + (1000 + Math.floor(Math.random() * 9000))
    },
    
    submitForm() {
      this.$refs.form.validate(async valid => {
        if (valid) {
          try {
            if (this.isEdit) {
              await updateItem(this.form)
              this.$message.success('更新成功')
            } else {
              await addItem(this.form)
              this.$message.success('新增成功')
            }
            this.dialogVisible = false
            this.fetchData()
          } catch (error) {
            this.$message.error(this.isEdit ? '更新失败' : '新增失败')
          }
        }
      })
    },
    
    // 类型操作
    handleAddType(parentId) {
      this.isEditType = false
      const parent = parentId === 0 ? null : this.findNode(this.typeTreeData, parentId)
      this.typeForm = {
        typeId: null,
        parentId: parentId,
        parentName: parent ? parent.typeName : '',
        typeCode: '',
        typeName: '',
        itemAttr: 'RAW'
      }
      this.typeDialogVisible = true
    },
    
    handleEditType(data) {
      this.isEditType = true
      this.typeForm = {
        typeId: data.typeId,
        parentId: data.parentId || 0,
        parentName: '',
        typeCode: data.typeCode,
        typeName: data.typeName,
        itemAttr: data.itemAttr
      }
      this.typeDialogVisible = true
    },
    
    handleDeleteType(data) {
      if (data.children && data.children.length > 0) {
        this.$message.warning('该类型下有子类型，请先删除子类型')
        return
      }
      this.$confirm(`删除类型 "${data.typeName}"？`, '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await delItemType(data.typeId)
          this.$message.success('删除成功')
          this.fetchTypeTree()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },
    
    submitTypeForm() {
      this.$refs.typeForm.validate(async valid => {
        if (valid) {
          try {
            if (this.isEditType) {
              await updateItemType(this.typeForm)
              this.$message.success('更新成功')
            } else {
              await addItemType(this.typeForm)
              this.$message.success('新增成功')
            }
            this.typeDialogVisible = false
            this.fetchTypeTree()
          } catch (error) {
            this.$message.error(this.isEditType ? '更新失败' : '新增失败')
          }
        }
      })
    },
    
    getTypeIcon(attr) {
      const map = { 'RAW': 'el-icon-box', 'SEMI': 'el-icon-s-operation', 'PRODUCT': 'el-icon-shopping-bag-1' }
      return map[attr] || 'el-icon-s-grid'
    },
    
    getTypeColor(attr) {
      const map = { 'RAW': '#409EFF', 'SEMI': '#E6A23C', 'PRODUCT': '#67C23A' }
      return map[attr] || '#909399'
    },
    
    getTypeTagType(attr) {
      const map = { 'RAW': 'primary', 'SEMI': 'warning', 'PRODUCT': 'success' }
      return map[attr] || 'info'
    },
    
    getStockClass(row) {
      if (row.quantity <= row.safetyStock) return 'stock-low'
      return 'stock-normal'
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 15px;
  height: calc(100vh - 100px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 12px;
  border-bottom: 1px solid #EBEEF5;

  .title-section {
    display: flex;
    align-items: center;
    
    i {
      font-size: 22px;
      color: #409EFF;
      margin-right: 8px;
    }
    
    .title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
}

.main-content {
  height: calc(100% - 50px);
  
  .tree-col {
    height: 100%;
    
    .tree-card {
      height: 100%;
      
      .tree-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px 15px;
        
        span {
          font-weight: 600;
          font-size: 14px;
        }
      }
      
      .tree-search {
        padding: 10px 15px;
      }
      
      .filter-tree {
        padding: 0 10px 15px;
        height: calc(100% - 90px);
        overflow-y: auto;
      }
      
      .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 13px;
        padding-right: 8px;
        
        .node-content {
          display: flex;
          align-items: center;
          flex: 1;
          padding: 4px 6px;
          border-radius: 3px;
          
          i {
            margin-right: 6px;
            font-size: 14px;
          }
          
          .node-label {
            flex: 1;
          }
          
          .node-count {
            color: #909399;
            font-size: 11px;
            margin-left: 4px;
          }
          
          &.active {
            background: #E3F2FD;
          }
        }
        
        .node-actions {
          display: none;
          
          i {
            margin-left: 8px;
            cursor: pointer;
            color: #909399;
            font-size: 13px;
            
            &:hover {
              color: #409EFF;
            }
            
            &.el-icon-delete:hover {
              color: #F56C6C;
            }
          }
        }
        
        &:hover .node-actions {
          display: inline-flex;
        }
      }
    }
  }
  
  .table-col {
    height: 100%;
    display: flex;
    flex-direction: column;
    
    .filter-card {
      margin-bottom: 10px;
      flex-shrink: 0;
      
      .filter-row {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .breadcrumb-wrap {
          display: flex;
          align-items: center;
          
          .breadcrumb-link {
            cursor: pointer;
            color: #409EFF;
            
            &:hover {
              text-decoration: underline;
            }
          }
        }
        
        .search-wrap {
          display: flex;
          gap: 8px;
        }
      }
    }
    
    .stat-bar {
      display: flex;
      gap: 15px;
      margin-bottom: 10px;
      padding: 10px 15px;
      background: #fff;
      border-radius: 4px;
      border: 1px solid #EBEEF5;
      flex-shrink: 0;
      
      .stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 13px;
        
        .stat-label {
          color: #606266;
        }
        
        .stat-value {
          font-weight: 600;
          font-size: 15px;
          
          &.blue { color: #409EFF; }
          &.green { color: #67C23A; }
          &.orange { color: #E6A23C; }
          &.gray { color: #909399; }
        }
      }
    }
    
    .table-card {
      flex: 1;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      
      .table-scroll-wrapper {
        flex: 1;
        overflow: auto;
        position: relative;
        
        /* 自定义滚动条样式 - 优美设计 */
        &::-webkit-scrollbar {
          width: 8px;
          height: 8px;
        }
        
        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 4px;
        }
        
        &::-webkit-scrollbar-thumb {
          background: linear-gradient(135deg, #7C3AED 0%, #A78BFA 100%);
          border-radius: 4px;
          border: 2px solid #f1f1f1;
          
          &:hover {
            background: linear-gradient(135deg, #6D28D9 0%, #8B5CF6 100%);
          }
        }
        
        &::-webkit-scrollbar-corner {
          background: #f1f1f1;
        }
        
        /* Firefox 滚动条 */
        scrollbar-width: thin;
        scrollbar-color: #A78BFA #f1f1f1;
        
        /* 暗色主题滚动条 */
        .theme-dark & {
          &::-webkit-scrollbar-track {
            background: #2d2d3a;
          }
          
          &::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, #A78BFA 0%, #C4B5FD 100%);
            border: 2px solid #2d2d3a;
            
            &:hover {
              background: linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%);
            }
          }
          
          &::-webkit-scrollbar-corner {
            background: #2d2d3a;
          }
          
          scrollbar-color: #A78BFA #2d2d3a;
        }
      }
      
      .el-table {
        border-top: none;
      }
      
      .stock-low {
        color: #F56C6C;
        font-weight: 600;
      }
      
      .stock-normal {
        color: #606266;
      }
      
      .pagination-wrap {
        padding: 12px 15px;
        border-top: 1px solid #EBEEF5;
        display: flex;
        justify-content: flex-end;
        flex-shrink: 0;
        background: #fff;
      }
    }
  }
}

// 详情弹框样式
.detail-content {
  padding: 10px 0;
  
  .detail-section {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .detail-item {
    display: flex;
    align-items: baseline;
    padding: 8px 0;
    
    &.block {
      flex-direction: column;
      
      .detail-label {
        margin-bottom: 8px;
      }
      
      .detail-value {
        padding: 10px 12px;
        background: #f5f7fa;
        border-radius: 4px;
        width: 100%;
        min-height: 40px;
      }
    }
    
    .detail-label {
      color: #606266;
      font-size: 14px;
      min-width: 80px;
      font-weight: 500;
    }
    
    .detail-value {
      color: #303133;
      font-size: 14px;
      flex: 1;
      
      &.cost {
        color: #7C3AED;
        font-weight: 600;
      }
    }
  }
}

::v-deep {
  .el-tree-node__content {
    height: 32px;
  }
  
  .el-card__header {
    padding: 12px 15px;
    border-bottom: 1px solid #EBEEF5;
  }
}
</style>
